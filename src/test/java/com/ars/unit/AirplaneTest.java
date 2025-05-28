package com.ars.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
//test classes
class AirplaneTest {

    @BeforeEach
    void clearFlights() {
        // Ensure FlightCollection is empty before getAirPlaneInfo tests
        FlightCollection.getFlights().clear();
    }

    @Test
    void testValidAirplaneCreation() {
        Airplane airplane = new Airplane(1, "Airbus A320", 10, 50, 10);
        assertEquals(1, airplane.getAirplaneID());
        assertEquals("Airbus A320", airplane.getAirplaneModel());
    }

    @Test
    void testInvalidAirplaneID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(0, "Boeing", 10, 50, 10);
        });
    }

    @Test
    void testNegativeAirplaneID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(-1, "Boeing", 10, 100, 5);
        });
    }

    @Test
    void testNegativeBusinessSeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(2, "Boeing", -1, 100, 5);
        });
    }

    @Test
    void testNegativeEconomySeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(2, "Boeing", 10, -5, 5);
        });
    }

    @Test
    void testNegativeCrewSeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(2, "Boeing", 10, 100, -2);
        });
    }

    @Test
    void testEmptyModelName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(3, "", 10, 100, 5);
        });
    }

    @Test
    void testNullModelName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(3, null, 10, 100, 5);
        });
    }

    @Test
    void testInvalidTotalSeatsThrows() {
        // 10 + 10 + 10 = 30 != 70
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new Airplane(4, "Test", 10, 10, 10));
        assertTrue(ex.getMessage().contains("Total seats must be exactly"));
    }

    @Test
    void testSeatAssignmentsDistribution() {
        int business = 14, economy = 42, crew = 14;
        Airplane airplane = new Airplane(5, "TestModel", business, economy, crew);

        Map<String, String> assigns = airplane.getSeatAssignments();
        // size
        assertEquals(70, assigns.size());

        // correct counts
        assertEquals(business,
                assigns.values().stream().filter("business"::equals).count());
        assertEquals(economy,
                assigns.values().stream().filter("economy"::equals).count());
        assertEquals(crew,
                assigns.values().stream().filter("crew"::equals).count());

        // boundary checks
        assertEquals("business", assigns.get("A1"));
        assertEquals("business", assigns.get("B7"));
        assertEquals("economy",  assigns.get("C1"));
        assertEquals("economy",  assigns.get("H7"));
        assertEquals("crew",     assigns.get("I1"));
        assertEquals("crew",     assigns.get("J7"));
    }

    @Test
    void testSettersThrowOnInvalidValues() {
        Airplane airplane = new Airplane(1, "ModelX", 10, 50, 10);

        assertThrows(IllegalArgumentException.class,
                () -> airplane.setBusinessSitsNumber(-5));
        assertThrows(IllegalArgumentException.class,
                () -> airplane.setEconomySitsNumber(0));
        assertThrows(IllegalArgumentException.class,
                () -> airplane.setCrewSitsNumber(71));  // would make total != 70
    }

    @Test
    void testGetAirPlaneInfo() {
        Airplane plane = new Airplane(6, "Lookup", 10, 50, 10);
        Flight flight = new Flight(
                100,
                "A", "B", "CODE", "COMP",
                Timestamp.valueOf("2025-06-01 10:00:00"),
                Timestamp.valueOf("2025-06-01 12:00:00"),
                plane
        );
        FlightCollection.addFlights(new ArrayList<>(List.of(flight)));

        assertEquals(plane, Airplane.getAirPlaneInfo(6));
        assertNull(Airplane.getAirPlaneInfo(999));
    }

    @Test
    void testToStringContainsDetails() {
        Airplane airplane = new Airplane(7, "ModelY", 10, 50, 10);
        String report = airplane.toString();
        assertAll(
                () -> assertTrue(report.contains("ModelY")),
                () -> assertTrue(report.contains("business sits=10")),
                () -> assertTrue(report.contains("economy sits=50")),
                () -> assertTrue(report.contains("crew sits=10"))
        );
    }
}
