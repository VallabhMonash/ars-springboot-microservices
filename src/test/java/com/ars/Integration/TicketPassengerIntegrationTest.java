package com.ars.Integration;

import static org.junit.jupiter.api.Assertions.*;

import com.ars.unit.Airplane;
import com.ars.unit.Flight;
import com.ars.unit.Flight.FlightSchedule;
import com.ars.unit.Passenger;
import com.ars.unit.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

class TicketPassengerIntegrationTest {

    private Passenger passenger;
    private Flight flight;

    @BeforeEach
    void setUp() {
        passenger = new Passenger(
                "John", "Doe", 30, "Man",
                "john.doe@example.com", "0412345678", "G12345678",
                "1234567890123456", 123
        );

        Airplane airplane = new Airplane(1, "Boeing 737", 10, 50, 10);

        Timestamp dateFrom = Timestamp.valueOf("2025-06-01 08:00:00");
        Timestamp dateTo   = Timestamp.valueOf("2025-06-01 10:00:00");
        FlightSchedule schedule = new FlightSchedule(dateFrom, dateTo);

        flight = new Flight(
                1,
                "Melbourne",
                "Sydney",
                "FL123",
                "Qantas",
                schedule,
                airplane
        );
    }

    @Test
    void testTicketAssociatedWithPassengerCorrectly() {
        Ticket ticket = new Ticket(101, 500, flight, false, passenger);

        assertNotNull(ticket.getPassenger());
        assertEquals("John", ticket.getPassenger().getFirstName());
        assertEquals("Doe", ticket.getPassenger().getSecondName());
        assertEquals("+61412345678", ticket.getPassenger().getPhoneNumber());
        assertEquals("john.doe@example.com", ticket.getPassenger().getEmail());
        assertEquals("G12345678", ticket.getPassenger().getPassport());
    }

    @Test
    void testTicketPriceAdjustedByPassengerAge() {
        Passenger childPassenger = new Passenger(
                "Jane", "Smith", 10, "Woman",
                "jane.smith@example.com", "0412345678", "G12345679",
                "1234567890123457", 456
        );

        Ticket childTicket = new Ticket(102, 500, flight, false, childPassenger);

        double expectedPrice = 500 * 0.5 * 1.12;
        assertEquals((int) expectedPrice, childTicket.getPrice(), "Child fare calculation error");
    }

    @Test
    void testTicketPriceAdjustedForSeniorPassenger() {
        Passenger seniorPassenger = new Passenger(
                "Alice", "Brown", 65, "Woman",
                "alice.brown@example.com", "0412345678", "G12345677",
                "1234567890123458", 789
        );

        Ticket seniorTicket = new Ticket(103, 800, flight, false, seniorPassenger);
        assertEquals(0, seniorTicket.getPrice(), "Senior fare is 0");
    }
}
