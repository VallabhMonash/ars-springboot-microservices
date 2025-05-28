package com.ars.Integration;

import com.ars.unit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;

public class TicketIntegrationTest {

    private Airplane airplane;
    private Flight flight;
    private Passenger passenger;
    private Ticket ticket;

    @BeforeEach
    public void setup() {
        // Reset collections
        TicketCollection.tickets.clear();

        // Create airplane
        airplane = new Airplane(1, "G", 10, 1, 20);

        // Wrap departure and arrival into FlightSchedule
        Timestamp dateFrom = Timestamp.valueOf("2025-06-01 10:00:00");
        Timestamp dateTo   = Timestamp.valueOf("2025-06-01 14:00:00");
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(dateFrom, dateTo);

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

        // Create passenger
        passenger = new Passenger(
                "Alice",
                "Wonder",
                30,
                "Man",
                "alice@mail.com",
                "0412345678",
                "G12345678",
                "1234567890123456",
                123
        );

        // Create ticket
        ticket = new Ticket(101, 1000, flight, true, passenger);
    }

    @Test
    public void testAirplaneFields() {
        assertEquals(1, airplane.getAirplaneID());
        assertEquals("G", airplane.getAirplaneModel());
        assertEquals(10, airplane.getBusinessSitsNumber());
        assertEquals(1, airplane.getEconomySitsNumber());
        assertEquals(20, airplane.getCrewSitsNumber());
    }

    @Test
    public void testFlightFields() {
        assertEquals(1, flight.getFlightID());
        assertEquals("Tokyo",   flight.getDepartTo());
        assertEquals("Sydney",  flight.getDepartFrom());
        assertEquals("JL123",   flight.getCode());
        assertEquals("Japan Airlines", flight.getCompany());
        assertEquals(Timestamp.valueOf("2025-06-01 10:00:00"), flight.getDateFrom());
        assertEquals(Timestamp.valueOf("2025-06-01 14:00:00"), flight.getDateTo());
        assertEquals(airplane, flight.getAirplane());
    }

    @Test
    public void testPassengerFields() {
        assertEquals("Alice",     passenger.getFirstName());
        assertEquals("Wonder",    passenger.getSecondName());
        assertEquals(30,            passenger.getAge());
        assertEquals("Man",       passenger.getGender());
        assertEquals("alice@mail.com", passenger.getEmail());
        assertEquals("+61412345678",   passenger.getPhoneNumber());
        assertEquals("G12345678",      passenger.getPassport());
        assertEquals("1234567890123456", passenger.getCardNumber());
        assertEquals(123, passenger.getSecurityCode());
    }

    @Test
    public void testTicketCreation() {
        assertEquals(101, ticket.getTicket_id());
        assertEquals(flight, ticket.getFlight());
        assertEquals(passenger, ticket.getPassenger());
        assertTrue(ticket.getClassVip());
        assertFalse(ticket.ticketStatus());
    }

    @Test
    public void testTicketPriceCalculationWithServiceTaxAndSale() {
        Passenger child = new Passenger(
                "Tom", "Kid", 10, "Man",
                "tom@mail.com", "0412345678",
                "E12345678", "9876543210123456", 123
        );
        Ticket childTicket = new Ticket(102, 1000, flight, false, child);
        // price should be 1000 -> 500 after age discount -> 560 after tax
        assertEquals(560, childTicket.getPrice());

        Passenger elder = new Passenger(
                "Elder", "Lee", 70, "Woman",
                "elder@mail.com", "0412345678",
                "E12345678", "9876543210123456", 123
        );
        Ticket elderTicket = new Ticket(103, 1000, flight, false, elder);
        // price should be 0 -> 0 after tax
        assertEquals(0, elderTicket.getPrice());
    }

    @Test
    public void testTicketStatusUpdate() {
        assertFalse(ticket.ticketStatus());
        ticket.setTicketStatus(true);
        assertTrue(ticket.ticketStatus());
    }

    @Test
    public void testAddTicketsToCollection() {
        ArrayList<Ticket> batch = new ArrayList<>();
        batch.add(ticket);

        Ticket ticket2 = new Ticket(102, 1200, flight, false, passenger);
        batch.add(ticket2);

        TicketCollection.addTickets(batch);
        assertEquals(2, TicketCollection.getTickets().size());
    }

    @Test
    public void testDuplicateTicketIdThrows() {
        ArrayList<Ticket> duplicateBatch = new ArrayList<>();
        duplicateBatch.add(ticket);
        duplicateBatch.add(ticket); // same ID

        Exception exception = assertThrows(
                IllegalStateException.class,
                () -> TicketCollection.addTickets(duplicateBatch)
        );

        assertTrue(exception.getMessage().contains("Duplicate ticket ID"));
    }

    @Test
    public void testGetTicketInfo() {
        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        TicketCollection.addTickets(tickets);

        Ticket result = TicketCollection.getTicketInfo(101);
        assertNotNull(result);
        assertEquals(ticket, result);

        assertNull(TicketCollection.getTicketInfo(999)); // not found
    }
}
