package com.ars.Integration;

import com.ars.unit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TicketSystemIntegrationTest {
    private Airplane airplane;
    private Flight flight;
    private Passenger passenger;
    private Ticket ticket;
    private Ticket connectingTicket;
    private TicketSystem ticketSystem;

    @BeforeEach
    public void setup() {
        TicketCollection.tickets.clear();
        FlightCollection.flights.clear();

        airplane = new Airplane(1, "Boeing 787", 10, 50, 10);

        Timestamp dateFrom = Timestamp.valueOf("2025-06-01 10:00:00");
        Timestamp dateTo = Timestamp.valueOf("2025-06-01 14:00:00");

        Flight.FlightSchedule flightSchedule = new Flight.FlightSchedule(dateFrom, dateTo);

        flight = new Flight(1, "Tokyo", "Sydney", "JL123", "Japan Airlines", flightSchedule, airplane);

        Timestamp connectDateFrom = Timestamp.valueOf("2025-06-01 16:00:00");
        Timestamp connectDateTo = Timestamp.valueOf("2025-06-01 20:00:00");
        Flight connectingFlight = new Flight(2, "Sydney", "Melbourne", "JL456", "Japan Airlines", flightSchedule, airplane);

        ArrayList<Flight> flights = new ArrayList<>();
        flights.add(flight);
        flights.add(connectingFlight);
        FlightCollection.addFlights(flights);

        passenger = new Passenger(
                "Alice",
                "Wonder",
                30,
                "Female",
                new Passenger.ContactInfo("alice@mail.com", "0412345678"),
                "G12345678",
                new Passenger.PaymentInfo("1234567890123456", 123)
        );

        ticket = new Ticket(101, 1000, flight, false, null);
        connectingTicket = new Ticket(102, 800, connectingFlight, false, null);

        ArrayList<Ticket> batch = new ArrayList<>();
        batch.add(ticket);
        batch.add(connectingTicket);
        TicketCollection.addTickets(batch);

        ticketSystem = new TicketSystem();
    }

    @Test
    public void testBuyTicketWithInvalidTicket() {
        ticketSystem.buyTicket(999, passenger);
        assertNull(TicketCollection.getTicketInfo(999));
    }

    @Test
    public void testBuyTicketAlreadyBooked() {
        ticket.setTicketStatus(true);
        ticketSystem.buyTicket(ticket.getTicketId(), passenger);
        assertNull(ticket.getPassenger());
    }

    @Test
    public void testBuyTicketWithoutFlight() {
        FlightCollection.flights.clear();
        Ticket ticketNoFlight = new Ticket(103, 500, flight, false, null);
        ArrayList<Ticket> tempBatch = new ArrayList<>();
        tempBatch.add(ticketNoFlight);
        TicketCollection.addTickets(tempBatch);
        ticketSystem.buyTicket(103, passenger);
        assertNull(ticketNoFlight.getPassenger());
    }

    @Test
    public void testBuyTicketSuccessEconomy() {
        ticketSystem.buyTicket(101, passenger);
        assertEquals(passenger, ticket.getPassenger());
        assertTrue(ticket.ticketStatus());
        assertEquals(49, airplane.getEconomySitsNumber());
    }

    @Test
    public void testBuyTicketSuccessBusinessVip() {
        Ticket vipTicket = new Ticket(104, 2000, flight, true, null);
        ArrayList<Ticket> tempBatch = new ArrayList<>();
        tempBatch.add(vipTicket);
        TicketCollection.addTickets(tempBatch);

        ticketSystem.buyTicket(104, passenger);
        assertEquals(passenger, vipTicket.getPassenger());
        assertTrue(vipTicket.ticketStatus());
        assertEquals(9, airplane.getBusinessSitsNumber());
    }

    @Test
    public void testChooseTicketDirectFlight() {
        ticketSystem.chooseTicket("Sydney", "Tokyo", passenger);
        assertEquals(passenger, ticket.getPassenger());
        assertTrue(ticket.ticketStatus());
    }

    @Test
    public void testChooseTicketWithConnection() {
        ticketSystem.chooseTicket("Melbourne", "Tokyo", passenger);
        assertTrue(ticket.ticketStatus());
        assertTrue(connectingTicket.ticketStatus());
    }

    @Test
    public void testChooseTicketNoVariants() {
        ticketSystem.chooseTicket("New York", "London", passenger);
        assertFalse(ticket.ticketStatus());
    }
}
