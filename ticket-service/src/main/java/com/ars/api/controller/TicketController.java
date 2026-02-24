package com.ars.api.controller;

import com.ars.api.dto.TicketBookingRequest;
import com.ars.api.dto.TicketRequestStatusResponse;
import com.ars.api.dto.TicketResponse;
import com.ars.api.dto.DtoMapper;
import com.ars.api.service.TicketService;
import com.ars.unit.Ticket;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public TicketRequestStatusResponse createTicket(@Valid @RequestBody TicketBookingRequest request) {
        return ticketService.bookTicketAsync(request);
    }

    @GetMapping
    public List<TicketResponse> listTickets() {
        List<Ticket> tickets = ticketService.listTickets();
        return tickets.stream()
                .map(DtoMapper::toTicketResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/requests/{id}")
    public TicketRequestStatusResponse getRequestStatus(@PathVariable("id") String id) {
        return ticketService.getRequestStatus(id);
    }
}
