package com.ars.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {
    private static final Timestamp FROM = Timestamp.valueOf("2025-04-21 10:00:00");
    private static final Timestamp TO   = Timestamp.valueOf("2025-04-21 12:00:00");

    @Test
    void testValidFlightCreation() {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(FROM, TO);

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

    @ParameterizedTest(name = "invalid departTo=''{0}'', departFrom=''{1}''")
    @MethodSource("invalidDepartArguments")
    void testInvalidDepartParameters(String departTo, String departFrom) {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(FROM, TO);

        assertThrows(IllegalArgumentException.class, () -> {
            new Flight(
                    1,
                    departTo,
                    departFrom,
                    "AF123",
                    "AirFrance",
                    schedule,
                    airplaneMock
            );
        });
    }

    static Stream<Arguments> invalidDepartArguments() {
        return Stream.of(
                Arguments.of("", "London"),
                Arguments.of((String) null, "London"),
                Arguments.of("Paris", ""),
                Arguments.of("Paris", null)
        );
    }

    @Test
    void testInvalidDateRangeInSchedule() {
        assertThrows(IllegalArgumentException.class,
                () -> new Flight.FlightSchedule(TO, FROM)
        );
    }

    @Test
    void testNullAirplane() {
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(FROM, TO);
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
    void testDuplicateFlightInsertion() {
        Airplane airplaneMock = Mockito.mock(Airplane.class);
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(FROM, TO);
        Flight flight1 = new Flight(1, "Paris", "London", "AF123", "AirFrance", schedule, airplaneMock);
        Flight flight2 = new Flight(2, "Paris", "London", "AF123", "AirFrance", schedule, airplaneMock);

        FlightCollection.flights = new ArrayList<>();
        FlightCollection.flights.add(flight1);

        List<Flight> newFlights = Arrays.asList(flight2);
        assertThrows(IllegalArgumentException.class, () -> FlightCollection.addFlights(newFlights));
    }
}
