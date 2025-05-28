package com.ars.Integration;

import org.junit.jupiter.api.Test;
import com.ars.unit.Flight;
import com.ars.unit.Airplane;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class AirplaneFlightIntegrationTest {

    @Test
    public void testAirplaneInjectedIntoFlightCorrectly() {
        // Creating a Real Airplane Object
        Airplane airplane = new Airplane(101, "A320", 10, 50, 10);

        // Create departure and arrival times
        Timestamp from = Timestamp.valueOf("2025-04-30 08:00:00");
        Timestamp to = Timestamp.valueOf("2025-04-30 12:00:00");

        // Create a real Flight object and inject Airplane
        Flight flight = new Flight(
                1,
                "Sydney",
                "Melbourne",
                "QF123",
                "Qantas",
                from,
                to,
                airplane
        );

        // Verify that the Airplane in Flight is saved correctly
        assertNotNull(flight.getAirplane());
        assertEquals(airplane, flight.getAirplane());

        // Validate fields such as departure/arrival city, flight number, etc.
        assertEquals("Melbourne", flight.getDepartFrom());
        assertEquals("Sydney", flight.getDepartTo());
        assertEquals("QF123", flight.getCode());
        assertEquals("Qantas", flight.getCompany());
        assertEquals(from, flight.getDateFrom());
        assertEquals(to, flight.getDateTo());
    }

    @Test
    public void testFlightCreationFailsWithNullAirplane() {
        Timestamp from = Timestamp.valueOf("2025-04-30 08:00:00");
        Timestamp to = Timestamp.valueOf("2025-04-30 12:00:00");

        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(
                    2,
                    "Beijing",
                    "Shanghai",
                    "CA888",
                    "AirChina",
                    from,
                    to,
                    null // null airplane
            );
        });
    }
}
