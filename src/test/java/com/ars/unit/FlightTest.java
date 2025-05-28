package com.ars.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlightTest {

    @Test
    public void testValidFlightCreation() {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Timestamp from = Timestamp.valueOf("2025-04-21 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-04-21 12:00:00");

        // wrap timestamps into FlightSchedule
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(from, to);

        Flight flight = new Flight(
                1,
                "Paris",
                "London",
                "AF123",
                "AirFrance",
                schedule,
                airplaneMock
        );
        assertEquals("Paris", flight.getDepartTo());
    }

    @Test
    public void testEmptyDepartTo() {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Timestamp from = Timestamp.valueOf("2025-04-21 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-04-21 12:00:00");
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(from, to);

        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(
                    1,
                    "",
                    "London",
                    "AF123",
                    "AirFrance",
                    schedule,
                    airplaneMock
            );
        });
    }

    @Test
    public void testNullDepartTo() {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Timestamp from = Timestamp.valueOf("2025-04-21 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-04-21 12:00:00");
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(from, to);

        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(
                    1,
                    null,
                    "London",
                    "AF123",
                    "AirFrance",
                    schedule,
                    airplaneMock
            );
        });
    }

    @Test
    public void testEmptyDepartFrom() {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Timestamp from = Timestamp.valueOf("2025-04-21 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-04-21 12:00:00");
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(from, to);

        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(
                    1,
                    "Paris",
                    "",
                    "AF123",
                    "AirFrance",
                    schedule,
                    airplaneMock
            );
        });
    }

    @Test
    public void testNullDepartFrom() {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Timestamp from = Timestamp.valueOf("2025-04-21 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-04-21 12:00:00");
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(from, to);

        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(
                    1,
                    "Paris",
                    null,
                    "AF123",
                    "AirFrance",
                    schedule,
                    airplaneMock
            );
        });
    }

    @Test
    public void testInvalidDateRange() {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Timestamp from = Timestamp.valueOf("2025-04-21 14:00:00");
        Timestamp to   = Timestamp.valueOf("2025-04-21 12:00:00");
        // schedule constructor will throw for invalid range
        assertThrows(IllegalArgumentException.class, () -> new Flight.FlightSchedule(from, to));
    }

    @Test
    public void testNullAirplane() {
        Timestamp from = Timestamp.valueOf("2025-04-21 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-04-21 12:00:00");
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(from, to);

        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(
                    1,
                    "Paris",
                    "London",
                    "AF123",
                    "AirFrance",
                    schedule,
                    null
            );
        });
    }

    @Test
    public void testDuplicateFlightInsertion() {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Timestamp from = Timestamp.valueOf("2025-04-21 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-04-21 12:00:00");
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(from, to);

        Flight flight1 = new Flight(
                1,
                "Paris",
                "London",
                "AF123",
                "AirFrance",
                schedule,
                airplaneMock
        );
        Flight flight2 = new Flight(
                2,
                "Paris",
                "London",
                "AF123",
                "AirFrance",
                schedule,
                airplaneMock
        );

        FlightCollection.flights = new ArrayList<>();
        FlightCollection.flights.add(flight1);

        ArrayList<Flight> newFlights = new ArrayList<>();
        newFlights.add(flight2);

        assertThrows(IllegalArgumentException.class, () -> {
            FlightCollection.addFlights(newFlights);
        });
    }
}
