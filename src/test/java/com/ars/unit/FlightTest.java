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
import static org.mockito.Mockito.mock;

class FlightTest {
    private static final Timestamp FROM = Timestamp.valueOf("2025-04-21 10:00:00");
    private static final Timestamp TO   = Timestamp.valueOf("2025-04-21 12:00:00");

    @Test
    void testValidFlightCreation() {
        Airplane airplaneMock = mock(Airplane.class);
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
        Airplane airplaneMock = mock(Airplane.class);
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
        Airplane airplaneMock = mock(Airplane.class);
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(FROM, TO);
        Flight flight1 = new Flight(1, "Paris", "London", "AF123", "AirFrance", schedule, airplaneMock);
        Flight flight2 = new Flight(2, "Paris", "London", "AF123", "AirFrance", schedule, airplaneMock);

        FlightCollection.flights = new ArrayList<>();
        FlightCollection.flights.add(flight1);

        List<Flight> newFlights = Arrays.asList(flight2);
        assertThrows(IllegalArgumentException.class, () -> FlightCollection.addFlights(newFlights));
    }

    // 1. FlightSchedule: null‐from and null‐to both throw
    @Test
    void testScheduleNullDatesThrow() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Flight.FlightSchedule(null, TO)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Flight.FlightSchedule(FROM, null))
        );
    }

    // 2. FlightSchedule: getters return exactly what was passed
    @Test
    void testScheduleGetters() {
        Flight.FlightSchedule sched = new Flight.FlightSchedule(FROM, TO);
        assertEquals(FROM, sched.getFrom());
        assertEquals(TO,   sched.getTo());
    }

    // 3. Flight constructor: invalid flightID (0 or negative) throws
    @ParameterizedTest(name = "invalid ID={0}")
    @MethodSource("badIds")
    void testFlightConstructorInvalidId(int badId) {
        Flight.FlightSchedule sched = new Flight.FlightSchedule(FROM, TO);
        assertThrows(IllegalArgumentException.class, () ->
                new Flight(badId, "A", "B", "C1", "Comp", sched, mock(Airplane.class))
        );
    }
    static Stream<Arguments> badIds() {
        return Stream.of(Arguments.of(0), Arguments.of(-5));
    }

    // 4. Flight constructor: null or empty code/company throws
    @ParameterizedTest(name = "code=''{0}'', company=''{1}''")
    @MethodSource("badCodeCompany")
    void testFlightConstructorInvalidCodeOrCompany(String code, String company) {
        Flight.FlightSchedule sched = new Flight.FlightSchedule(FROM, TO);
        assertThrows(IllegalArgumentException.class, () ->
                new Flight(1, "A", "B", code, company, sched, mock(Airplane.class))
        );
    }
    static Stream<Arguments> badCodeCompany() {
        return Stream.of(
                Arguments.of("",    "ValidCo"),
                Arguments.of(null,  "ValidCo"),
                Arguments.of("C1",  ""),
                Arguments.of("C1",  null)
        );
    }

    // 5. Flight constructor: null schedule throws
    @Test
    void testFlightConstructorNullSchedule() {
        assertThrows(IllegalArgumentException.class, () ->
                new Flight(1, "A", "B", "C1", "Comp", null, mock(Airplane.class))
        );
    }

    // 6. Flight constructor: valid creation sets all fields
    @Test
    void testValidFlightFieldsAndToString() {
        Airplane plane = mock(Airplane.class);
        Flight.FlightSchedule schedule = new Flight.FlightSchedule(FROM, TO);

        Flight f = new Flight(10, "X", "Y", "Z9", "Co", schedule, plane);
        assertAll("fields",
                () -> assertEquals(10,   f.getFlightID()),
                () -> assertEquals("X",  f.getDepartTo()),
                () -> assertEquals("Y",  f.getDepartFrom()),
                () -> assertEquals("Z9", f.getCode()),
                () -> assertEquals("Co", f.getCompany()),
                () -> assertEquals(FROM, f.getDateFrom()),
                () -> assertEquals(TO,   f.getDateTo()),
                () -> assertSame(plane,  f.getAirplane())
        );

        String s = f.toString();
        assertAll("toString contains",
                () -> assertTrue(s.contains("departFrom='Y'")),
                () -> assertTrue(s.contains("departTo='X'")),
                () -> assertTrue(s.contains("code='Z9'")),
                () -> assertTrue(s.contains("company='Co'"))
        );
    }

    // 7. Setters & getters on no-arg Flight
    @Test
    void testFlightSettersAndGetters() {
        Flight f = new Flight();
        Airplane plane = mock(Airplane.class);
        Timestamp df = Timestamp.valueOf("2025-05-01 08:00:00");
        Timestamp dt = Timestamp.valueOf("2025-05-01 09:30:00");

        f.setFlightID(99);
        f.setDepartTo("New");
        f.setDepartFrom("Old");
        f.setCode("X1");
        f.setCompany("YCo");
        f.setDateFrom(df);
        f.setDateTo(dt);
        f.setAirplane(plane);

        assertAll("test setter/getter",
                () -> assertEquals(99,      f.getFlightID()),
                () -> assertEquals("New",   f.getDepartTo()),
                () -> assertEquals("Old",   f.getDepartFrom()),
                () -> assertEquals("X1",    f.getCode()),
                () -> assertEquals("YCo",   f.getCompany()),
                () -> assertEquals(df,      f.getDateFrom()),
                () -> assertEquals(dt,      f.getDateTo()),
                () -> assertSame(plane,     f.getAirplane())
        );
    }
}
