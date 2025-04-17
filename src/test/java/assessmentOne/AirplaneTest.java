package assessmentOne;


import static org.junit.jupiter.api.Assertions.*;

import assessment.one.Airplane;
import org.junit.jupiter.api.Test;

class AirplaneTest {

    /**
     *  VALID CASES 
     */
    @Test
    void testValidAirplane() {
        Airplane airplane = new Airplane(1001, "Airbus 3800",10, 200,15);
        assertEquals(1001, airplane.getAirplaneID());
        assertEquals("Airbus 3800", airplane.getAirplaneModel());
        assertEquals(10, airplane.getBusinessSitsNumber());
        assertEquals(200, airplane.getEconomySitsNumber());
        assertEquals(15, airplane.getCrewSitsNumber());
    }

    /**
     *  INVALID AIRPLANE ID 
     */
    @Test
    void testZeroAirplaneID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(0, "Airbus A380", 21, 42, 10);
        });
    }

    @Test
    void testNegativeAirplaneID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(-1, "Boeing 777", 14, 49, 10);
        });
    }

    /**
     *  INVALID MODEL 
     */
    @Test
    void testEmptyModel() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(2, "", 7, 56, 8);
        });
    }

    @Test
    void testNullModel() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(3, null, 21, 35, 12);
        });
    }

    /**
     *  NEGATIVE SEAT COUNTS 
     */
    @Test
    void testNegativeBusinessSeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(4, "Boeing 737", -1, 49, 10);
        });
    }

    @Test
    void testNegativeEconomySeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(5, "Airbus A320", 14, -1, 10);
        });
    }

    @Test
    void testNegativeCrewSeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(6, "Embraer E190", 21, 42, -1);
        });
    }

    /**
     *  INVALID PASSENGER SEAT TOTALS 
     */
    @Test
    void testTotalSeatsLessThan7() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(7, "Cessna 172", 3, 3, 2); // 3 + 3 = 6 (<7)
        });
    }

    @Test
    void testTotalSeatsNotDivisibleBy7() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(8, "Boeing 787", 14, 55, 12); // 14 + 55 = 69 (not divisible by 7)
        });
    }

    @Test
    void testTotalSeatsExceeds70() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(9, "Airbus A350", 35, 42, 15); // 35 + 42 = 77 (>70)
        });
    }

    /**
     *  SETTER VALIDATION 
     */
    @Test
    void testInvalidBusinessSeatsSetter() {
        Airplane airplane = new Airplane(10, "Boeing 747", 14, 42, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            airplane.setBusinessSitsNumber(-5); // Negative seats
        });
    }

    @Test
    void testInvalidTotalAfterSetter() {
        Airplane airplane = new Airplane(11, "Airbus A380", 21, 42, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            airplane.setEconomySitsNumber(43); // 21 + 43 = 64 (not divisible by 7)
        });
    }
}
