package com.ars.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.Map;
//test
public class AirplaneTest {

    @Test
    public void testValidAirplaneCreation() {
        Airplane airplane = new Airplane(1, "Airbus A320", 10, 100, 5);
        assertEquals(1, airplane.getAirplaneID());
        assertEquals("Airbus A320", airplane.getAirplaneModel());
    }

    @Test
    public void testInvalidAirplaneID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(0, "Boeing", 10, 100, 5);
        });
    }

    @Test
    public void testNegativeAirplaneID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(-1, "Boeing", 10, 100, 5);
        });
    }

    @Test
    public void testNegativeBusinessSeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(2, "Boeing", -1, 100, 5);
        });
    }

    @Test
    public void testNegativeEconomySeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(2, "Boeing", 10, -5, 5);
        });
    }

    @Test
    public void testNegativeCrewSeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(2, "Boeing", 10, 100, -2);
        });
    }

    @Test
    public void testEmptyModelName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(3, "", 10, 100, 5);
        });
    }

    @Test
    public void testNullModelName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(3, null, 10, 100, 5);
        });
    }


    @Test
    public void testValidSeatsAssignments()
    {
        // Given
        int businessSeats = 10;
        int economySeats = 50;
        int crewSeats = 10;
        Airplane airplane = new Airplane(1, "Boeing 737", businessSeats, economySeats, crewSeats);

        // When
        Map<String, String> seatAssignments = airplane.getSeatAssignments();

        // Then
        assertNotNull(seatAssignments, "Seat assignments should not be null.");
        assertEquals(businessSeats + economySeats + crewSeats, seatAssignments.size(),
                "Total number of assigned seats should match.");

        // Check some specific seat types
        int businessCount = (int) seatAssignments.values().stream().filter(type -> type.equals("business")).count();
        int economyCount = (int) seatAssignments.values().stream().filter(type -> type.equals("economy")).count();
        int crewCount = (int) seatAssignments.values().stream().filter(type -> type.equals("crew")).count();

        assertEquals(businessSeats, businessCount, "Business seat count should match.");
        assertEquals(economySeats, economyCount, "Economy seat count should match.");
        assertEquals(crewSeats, crewCount, "Crew seat count should match.");
    }

    @Test
    public void testInValidSeatAssignments() {
        // business + economy + crew = 10 + 10 + 10 = 30 ≠ 70
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Airplane(1, "TestModel", 10, 10, 10)
        );
        assertTrue(
                ex.getMessage().contains("Total seats must be exactly 70"),
                "Expected exception message to mention 70 seats"
        );
    }
}
