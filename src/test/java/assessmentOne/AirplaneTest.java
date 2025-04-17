package assessmentOne;


import static org.junit.jupiter.api.Assertions.*;

import assessment.one.Airplane;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AirplaneTest {

    /**
     *  VALID CASES 
     */
    @Test
    @DisplayName("Valid airplane creation with proper parameters")
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
    @DisplayName("Throw exception for zero airplane ID")
    void testZeroAirplaneID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(0, "Airbus A380", 21, 42, 10);
        });
    }

    @Test
    @DisplayName("Throw exception for negative airplane ID")
    void testNegativeAirplaneID() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(-1, "Boeing 777", 14, 49, 10);
        });
    }

    /**
     *  INVALID MODEL 
     */
    @Test
    @DisplayName("Throw exception for empty model name")
    void testEmptyModel() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(2, "", 7, 56, 8);
        });
    }

    @Test
    @DisplayName("Throw exception for null model name")
    void testNullModel() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(3, null, 21, 35, 12);
        });
    }

    /**
     *  NEGATIVE SEAT COUNTS 
     */
    @Test
    @DisplayName("Throw exception for negative business seats")
    void testNegativeBusinessSeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(4, "Boeing 737", -1, 49, 10);
        });
    }

    @Test
    @DisplayName("Throw exception for negative economy seats")
    void testNegativeEconomySeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(5, "Airbus A320", 14, -1, 10);
        });
    }

    @Test
    @DisplayName("Throw exception for negative crew seats")
    void testNegativeCrewSeats() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(6, "Embraer E190", 21, 42, -1);
        });
    }

    /**
     *  INVALID PASSENGER SEAT TOTALS 
     */
    @Test
    @DisplayName("Minimum valid passenger seats (7)")
    void testTotalSeatsLessThan7() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(7, "Cessna 172", 3, 3, 2); // 3 + 3 = 6 (<7)
        });
    }

    @Test
    @DisplayName("Invalid total seats")
    void testTotalSeatsNotDivisibleBy7() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(8, "Boeing 787", 14, 55, 12); // 14 + 55 = 69 (not divisible by 7)
        });
    }

    @Test
    @DisplayName("Maximum valid passenger seats (70)")
    void testTotalSeatsExceeds70() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Airplane(9, "Airbus A350", 35, 42, 15); // 35 + 42 = 77 (>70)
        });
    }

    /**
     *  SETTER VALIDATION 
     */
    @Test
    @DisplayName("Invalid business seats")
    void testInvalidBusinessSeatsSetter() {
        Airplane airplane = new Airplane(10, "Boeing 747", 14, 42, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            airplane.setBusinessSitsNumber(-5); // Negative seats
        });
    }

    @Test
    @DisplayName("Invalid total seats update")
    void testInvalidTotalAfterSetter() {
        Airplane airplane = new Airplane(11, "Airbus A380", 21, 42, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            airplane.setEconomySitsNumber(43); // 21 + 43 = 64 (not divisible by 7)
        });
    }
}
