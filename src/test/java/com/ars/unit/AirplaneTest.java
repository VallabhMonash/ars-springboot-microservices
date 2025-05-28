package com.ars.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

class AirplaneTest {

    @BeforeEach
    void clearFlights() {
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
        assertThrows(IllegalArgumentException.class, () -> new Airplane(0, "Boeing", 10, 50, 10));
    }

    @Test
    void testNegativeAirplaneID() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(-1, "Boeing", 10, 100, 5));
    }

    @Test
    void testNegativeBusinessSeats() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(2, "Boeing", -1, 100, 5));
    }

    @Test
    void testNegativeEconomySeats() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(2, "Boeing", 10, -5, 5));
    }

    @Test
    void testNegativeCrewSeats() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(2, "Boeing", 10, 100, -2));
    }

    @Test
    void testZeroEconomySeats() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(2, "Boeing", 10, 0, 5));
    }

    @Test
    void testZeroCrewSeats() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(2, "Boeing", 10, 50, 0));
    }

    @Test
    void testEmptyModelName() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(3, "", 10, 100, 5));
    }

    @Test
    void testNullModelName() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(3, null, 10, 100, 5));
    }

    @Test
    void testInvalidTotalSeatsThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Airplane(4, "Test", 10, 10, 10));
    }

    @Test
    void testSeatAssignmentsDistribution() {
        int business = 14, economy = 42, crew = 14;
        Airplane airplane = new Airplane(5, "TestModel", business, economy, crew);

        Map<String, String> assigns = airplane.getSeatAssignments();
        assertEquals(70, assigns.size());
        assertEquals(business, assigns.values().stream().filter("business"::equals).count());
        assertEquals(economy, assigns.values().stream().filter("economy"::equals).count());
        assertEquals(crew, assigns.values().stream().filter("crew"::equals).count());
        assertEquals("business", assigns.get("A1"));
        assertEquals("business", assigns.get("B7"));
        assertEquals("economy", assigns.get("C1"));
        assertEquals("economy", assigns.get("H7"));
        assertEquals("crew", assigns.get("I1"));
        assertEquals("crew", assigns.get("J7"));
    }

    @Test
    void testSetAirplaneIDInvalid() {
        Airplane airplane = new Airplane(1, "Valid", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneID(0));
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneID(-5));
    }

    @Test
    void testSetAirplaneIDValid() {
        Airplane airplane = new Airplane(1, "Valid", 10, 50, 10);
        airplane.setAirplaneID(99);
        assertEquals(99, airplane.getAirplaneID());
    }

    @Test
    void testSetAirplaneModelToNullThrows() {
        Airplane airplane = new Airplane(1, "ValidModel", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            airplane.setAirplaneModel(null);
        });
    }

    @Test
    void testSetAirplaneModelToEmptyThrows() {
        Airplane airplane = new Airplane(1, "ValidModel", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            airplane.setAirplaneModel("");
        });
    }

    @Test
    void testSetAirplaneModelValid() {
        Airplane airplane = new Airplane(1, "InitialModel", 10, 50, 10);
        airplane.setAirplaneModel("UpdatedModel");
        assertEquals("UpdatedModel", airplane.getAirplaneModel());
    }

    @Test
    void testSetBusinessSeatsNegative() {
        Airplane airplane = new Airplane(1, "Model", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setBusinessSitsNumber(-1));
    }

    @Test
    void testSetBusinessSeatsAboveMax() {
        Airplane airplane = new Airplane(1, "Model", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setBusinessSitsNumber(71));
    }

    @Test
    void testSetBusinessSeatsValid() {
        Airplane airplane = new Airplane(1, "Model", 10, 50, 10);
        airplane.setBusinessSitsNumber(10);
        assertEquals(10, airplane.getBusinessSitsNumber());
    }

    @Test
    void testSetEconomySeatsZero() {
        Airplane airplane = new Airplane(1, "Model", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setEconomySitsNumber(0));
    }

    @Test
    void testSetEconomySeatsAboveMax() {
        Airplane airplane = new Airplane(1, "Model", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setEconomySitsNumber(71));
    }

    @Test
    void testSetEconomySeatsValid() {
        Airplane airplane = new Airplane(1, "Model", 10, 50, 10);
        airplane.setEconomySitsNumber(50);
        assertEquals(50, airplane.getEconomySitsNumber());
    }

    @Test
    void testSetCrewSeatsZero() {
        Airplane airplane = new Airplane(1, "Model", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setCrewSitsNumber(0));
    }

    @Test
    void testSetCrewSeatsAboveMax() {
        Airplane airplane = new Airplane(1, "Model", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setCrewSitsNumber(71));
    }

    @Test
    void testSetCrewSeatsValid() {
        Airplane airplane = new Airplane(1, "Model", 10, 50, 10);
        airplane.setCrewSitsNumber(10);
        assertEquals(10, airplane.getCrewSitsNumber());
    }

    @Test
    void testSettersThrowOnInvalidValues() {
        Airplane airplane = new Airplane(1, "ModelX", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setBusinessSitsNumber(-5));
        assertThrows(IllegalArgumentException.class, () -> airplane.setEconomySitsNumber(0));
        assertThrows(IllegalArgumentException.class, () -> airplane.setCrewSitsNumber(71));
    }

    @Test
    void testSetterAcceptsExactlyMaxSeats() {
        Airplane airplane = new Airplane(1, "ModelZ", 10, 50, 10);
        airplane.setCrewSitsNumber(10);
        assertEquals(10, airplane.getCrewSitsNumber());
    }

    @Test
    void testBusinessSeatSetterUpperBoundary() {
        Airplane airplane = new Airplane(1, "ModelB", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setBusinessSitsNumber(71));
    }

    @Test
    void testEconomySeatSetterZeroValue() {
        Airplane airplane = new Airplane(1, "ModelE", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> airplane.setEconomySitsNumber(0));
    }

    @Test
    void testConstructorFailsOnZeroCrewSeats() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(1, "ZeroCrew", 10, 59, 0);
        });
        assertTrue(ex.getMessage().contains("Seat counts must"));
    }

    @Test
    void testConstructorFailsOnZeroEconomySeats() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(1, "ZeroEconomy", 10, 0, 60);
        });
        assertTrue(ex.getMessage().contains("Seat counts must"));
    }

    @Test
    void testConstructorFailsOnNegativeCrewSeats() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(1, "NegCrew", 10, 60, -1);
        });
        assertTrue(ex.getMessage().contains("Seat counts must"));
    }

    @Test
    void testConstructorFailsOnNegativeEconomySeats() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(1, "NegEco", 10, -1, 61);
        });
        assertTrue(ex.getMessage().contains("Seat counts must"));
    }

    @Test
    void testBusinessSeatsNegative() {
        Airplane a = new Airplane(1, "Test", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> a.setBusinessSitsNumber(-1));
    }

    @Test
    void testBusinessSeatsAboveMax() {
        Airplane a = new Airplane(1, "Test", 10, 50, 10);
        assertThrows(IllegalArgumentException.class, () -> a.setBusinessSitsNumber(71));
    }

    @Test
    void testBusinessSeatsValid() {
        Airplane a = new Airplane(1, "Test", 10, 50, 10);
        a.setBusinessSitsNumber(10);
        assertEquals(10, a.getBusinessSitsNumber());
    }

    @Test
    void testGetAirPlaneInfo() {
        Airplane plane = new Airplane(6, "Lookup", 10, 50, 10);
        Timestamp from = Timestamp.valueOf("2025-06-01 10:00:00");
        Timestamp to = Timestamp.valueOf("2025-06-01 12:00:00");
        Flight.FlightSchedule flightSchedule = new Flight.FlightSchedule(from, to);
        Flight flight = new Flight(100, "A", "B", "CODE", "COMP", flightSchedule, plane);
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
