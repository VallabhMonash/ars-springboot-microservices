package assessmentOne;

import assessment.one.Airplane;
import assessment.one.Flight;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for validating Flight and Airplane initialization and functionality {@link Flight}.
 */
public class FlightTest {

    /**
     * Test for verifying proper initialization of a Flight object.
     */
    @Test
    @DisplayName("Test Flight Initialization with All Attributes")
    public void testFlightInitialization() {
        // Create an Airplane instance for the flight
        Airplane airplane = new Airplane(1, "Boeing 747", 10, 100, 8);

        // Create timestamps for flight departure and arrival
        Timestamp dateFrom = new Timestamp(Calendar.getInstance().getTimeInMillis());
        Timestamp dateTo = new Timestamp(dateFrom.getTime() + 3600000); // flight duration of 1 hour

        // Create a Flight instance with sample data
        Flight flight = new Flight(100, "New York", "Los Angeles", "AA101", "American Airlines", dateFrom, dateTo, airplane);

        // Verify that the Flight instance has been correctly initialized
        assertEquals(100, flight.getFlightID());
        assertEquals("New York", flight.getDepartTo());
        assertEquals("Los Angeles", flight.getDepartFrom());
        assertEquals("AA101", flight.getCode());
        assertEquals("American Airlines", flight.getCompany());
        assertEquals(dateFrom, flight.getDateFrom());
        assertEquals(dateTo, flight.getDateTo());
        assertEquals(airplane, flight.getAirplane());
    }

    /**
     * Test for verifying the string representation of a Flight object.
     */
    @Test
    @DisplayName("Test Flight toString Method for Correct Output")
    public void testFlightToString() {
        // Create an Airplane instance for the flight
        Airplane airplane = new Airplane(2, "Airbus A380", 12, 150, 10);

        // Define fixed timestamps for consistency
        Timestamp dateFrom = Timestamp.valueOf("2025-03-21 10:00:00");
        Timestamp dateTo = Timestamp.valueOf("2025-03-21 14:00:00");

        // Create a Flight instance with sample data
        Flight flight = new Flight(200, "London", "Paris", "BA202", "British Airways", dateFrom, dateTo, airplane);

        // Get the string representation of the Flight instance
        String result = flight.toString();

        // Check that the string contains key pieces of information from both Flight and Airplane
        assertTrue(result.contains("Airplane"));
        assertTrue(result.contains("London"));
        assertTrue(result.contains("Paris"));
        assertTrue(result.contains("BA202"));
        assertTrue(result.contains("British Airways"));
    }
}
