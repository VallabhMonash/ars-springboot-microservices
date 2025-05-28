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
        airplane = new Airplane(1, "G", 10, 50, 10);

        Timestamp dateFrom = Timestamp.valueOf("2025-06-01 10:00:00");
        Timestamp dateTo = Timestamp.valueOf("2025-06-01 14:00:00");

        Flight.FlightSchedule flightSchedule = new Flight.FlightSchedule(dateFrom, dateTo);

        flight = new Flight(1, "Tokyo", "Sydney", "JL123", "Japan Airlines", flightSchedule, airplane);

        passenger = new Passenger(
                "Alice",
                "Wonder",
                30,
                "Man",
                new Passenger.ContactInfo("alice@mail.com", "0412345678"),
                "G12345678",
                new Passenger.PaymentInfo("1234567890123456", 123)
        );

        ticket = new Ticket(101, 1000, flight, true, passenger);
        TicketCollection.tickets.clear();
    }

    @Test
    public void testAirplaneFields() {
        assertEquals(1, airplane.getAirplaneID());
        assertEquals("G", airplane.getAirplaneModel());
        assertEquals(10, airplane.getBusinessSitsNumber());
        assertEquals(50, airplane.getEconomySitsNumber());
        assertEquals(10, airplane.getCrewSitsNumber());
    }

    @Test
    public void testFlightFields() {
        assertEquals(1, flight.getFlightID());
        assertEquals("Tokyo", flight.getDepartTo());
        assertEquals("Sydney", flight.getDepartFrom());
        assertEquals("JL123", flight.getCode());
        assertEquals("Japan Airlines", flight.getCompany());
        assertEquals(Timestamp.valueOf("2025-06-01 10:00:00"), flight.getDateFrom());
        assertEquals(Timestamp.valueOf("2025-06-01 14:00:00"), flight.getDateTo());
        assertEquals(airplane, flight.getAirplane());
    }

    @Test
    public void testPassengerFields() {
        assertEquals("Alice", passenger.getFirstName());
        assertEquals("Wonder", passenger.getSecondName());
        assertEquals(30, passenger.getAge());
        assertEquals("Man", passenger.getGender());
        assertEquals("alice@mail.com", passenger.getEmail());
        assertEquals("+61412345678", passenger.getPhoneNumber());
        assertEquals("G12345678", passenger.getPassport());
        assertEquals("1234567890123456", passenger.getCardNumber());
        assertEquals(123, passenger.getSecurityCode());
    }

    @Test
    public void testTicketCreation() {
        assertEquals(101, ticket.getTicketId());
        assertEquals(flight, ticket.getFlight());
        assertEquals(passenger, ticket.getPassenger());
        assertTrue(ticket.isClassVip());
        assertFalse(ticket.ticketStatus());
    }

    @Test
    public void testTicketPriceCalculationWithServiceTaxAndSale() {
        Passenger child = new Passenger(
                "Tom",
                "Kid",
                10,
                "Man",
                new Passenger.ContactInfo("tom@mail.com", "0412345678"),
                "E12345678",
                new Passenger.PaymentInfo("9876543210123456", 123)
        );

        Ticket childTicket = new Ticket(102, 1000, flight, false, child);
        // price should be 1000 -> 500 after age discount -> 560 after tax
        assertEquals(560, childTicket.getPrice());

        Passenger elder = new Passenger(
                "Elder",
                "Lee",
                70,
                "Woman",
                new Passenger.ContactInfo("elder@mail.com", "0412345678"),
                "E12345678",
                new Passenger.PaymentInfo("9876543210123456", 123)
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

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            TicketCollection.addTickets(duplicateBatch);
        });

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