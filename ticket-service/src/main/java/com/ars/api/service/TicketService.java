package com.ars.api.service;

import com.ars.api.dto.DtoMapper;
import com.ars.api.dto.TicketBookingRequest;
import com.ars.api.dto.TicketRequestStatusResponse;
import com.ars.api.dto.TicketResponse;
import com.ars.api.exception.BadRequestException;
import com.ars.api.exception.ConflictException;
import com.ars.api.exception.NotFoundException;
import com.ars.unit.Airplane;
import com.ars.unit.Flight;
import com.ars.unit.FlightCollection;
import com.ars.unit.Passenger;
import com.ars.unit.Ticket;
import com.ars.unit.TicketCollection;
import com.ars.unit.TicketSystem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ars.event.TicketCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.ars.api.config.RabbitConfig;

@Service
public class TicketService {
    private final TicketSystem ticketSystem;
    private final PassengerService passengerService;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Map<String, CompletableFuture<Ticket>> requests = new ConcurrentHashMap<>();
    private final Map<String, String> statuses = new ConcurrentHashMap<>();
    private final Map<String, String> messages = new ConcurrentHashMap<>();

    private final RabbitTemplate rabbitTemplate;

    public TicketService(TicketSystem ticketSystem, PassengerService passengerService, RabbitTemplate rabbitTemplate) {
        this.ticketSystem = ticketSystem;
        this.passengerService = passengerService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public TicketRequestStatusResponse bookTicketAsync(TicketBookingRequest request) {
        String requestId = UUID.randomUUID().toString();

        statuses.put(requestId, "PENDING");
        messages.put(requestId, "Request accepted");

        CompletableFuture<Ticket> future = CompletableFuture.supplyAsync(() -> {
            Ticket ticket = bookTicketInternal(request);
            statuses.put(requestId, "COMPLETED");
            messages.put(requestId, "Ticket booked");
            return ticket;
        }, executor).exceptionally(ex -> {
            statuses.put(requestId, "FAILED");
            messages.put(requestId, ex.getMessage());
            return null;
        });

        requests.put(requestId, future);

        return new TicketRequestStatusResponse(requestId, "PENDING", "Request accepted", null);
    }

    private Ticket bookTicketInternal(TicketBookingRequest request) {
        Passenger passenger = passengerService.createPassenger(request.getPassenger());

        Ticket ticket = TicketCollection.getTicketInfo(request.getTicketId());
        if (ticket == null) {
            throw new NotFoundException("Ticket not found: " + request.getTicketId());
        }
        if (ticket.ticketStatus()) {
            throw new ConflictException("Ticket is already booked: " + request.getTicketId());
        }

        Flight flight = FlightCollection.getFlightInfo(ticket.getFlight().getFlightID());
        if (flight == null) {
            throw new NotFoundException("No flight associated with ticket: " + request.getTicketId());
        }

        Airplane airplane = Airplane.getAirPlaneInfo(flight.getAirplane().getAirplaneID());
        if (airplane == null) {
            throw new NotFoundException("No airplane associated with ticket: " + request.getTicketId());
        }

        ticketSystem.buyTicket(request.getTicketId(), passenger);

        if (!ticket.ticketStatus() || ticket.getPassenger() == null) {
            throw new BadRequestException("Ticket booking failed for ticket: " + request.getTicketId());
        }

        TicketCreatedEvent event = new TicketCreatedEvent(
            ticket.getTicketId(),
            ticket.getFlight().getFlightID(),
            passenger.getEmail()
        );
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, event);

        return ticket;
    }

    public TicketRequestStatusResponse getRequestStatus(String requestId) {
        String status = statuses.get(requestId);
        if (status == null) {
            throw new NotFoundException("Request not found: " + requestId);
        }

        TicketResponse ticketResponse = null;
        if ("COMPLETED".equals(status)) {
            Ticket ticket = requests.get(requestId).join();
            ticketResponse = DtoMapper.toTicketResponse(ticket);
        }

        return new TicketRequestStatusResponse(
                requestId,
                status,
                messages.get(requestId),
                ticketResponse);
    }

    public List<Ticket> listTickets() {
        return TicketCollection.getTickets();
    }
}
