package com.ars.Integration;

import com.ars.unit.*;
import com.ars.unit.Flight.FlightSchedule;
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
        // Reset collections
        TicketCollection.tickets.clear();
        FlightCollection.flights.clear();

        // Create airplane
        airplane = new Airplane(1, "Boeing 787", 5, 100, 10);

        // Wrap departure and arrival timestamps into FlightSchedule
        Timestamp dateFrom = Timestamp.valueOf("2025-06-01 10:00:00");
        Timestamp dateTo   = Timestamp.valueOf("2025-06-01 14:00:00");
        FlightSchedule schedule = new FlightSchedule(dateFrom, dateTo);

        // Create flight with new constructor
        flight = new Flight(
                1,
                "Tokyo",
                "Sydney",
                "JL123",
                "Japan Airlines",
                schedule,
                airplane
        );

        // Wrap connecting flight timestamps
        Timestamp connectDateFrom = Timestamp.valueOf("2025-06-01 16:00:00");
        Timestamp connectDateTo   = Timestamp.valueOf("2025-06-01 20:00:00");
        FlightSchedule connectSchedule = new FlightSchedule(connectDateFrom, connectDateTo);

        // Create connecting flight
        Flight connectingFlight = new Flight(
                2,
                "Sydney",
                "Melbourne",
                "JL456",
                "Japan Airlines",
                connectSchedule,
                airplane
        );

        // Add flights to collection
        ArrayList<Flight> flights = new ArrayList<>();
        flights.add(flight);
        flights.add(connectingFlight);
        FlightCollection.addFlights(flights);

        // Create passenger
        passenger = new Passenger(
                "Alice",
                "Wonder",
                30,
                "Female",
                "alice@mail.com",
                "0412345678",
                "G12345678",
                "1234567890123456",
                123
        );

        // Create tickets
        ticket = new Ticket(101, 1000, flight, false, null);
        connectingTicket = new Ticket(102, 800, connectingFlight, false, null);

        ArrayList<Ticket> batch = new ArrayList<>();
        batch.add(ticket);
        batch.add(connectingTicket);
        TicketCollection.addTickets(batch);

        // Initialize TicketSystem
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
        ticketSystem.buyTicket(ticket.getTicket_id(), passenger);
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
        assertEquals(99, airplane.getEconomySitsNumber());
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
        assertEquals(4, airplane.getBusinessSitsNumber());
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