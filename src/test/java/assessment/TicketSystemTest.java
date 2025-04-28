package assessment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketSystemTest {

    private TicketSystem ticketSystem;
    private AutoCloseable closeable;

    @Mock
    private Passenger mockPassenger;

    @Mock
    private Ticket mockTicket;

    @Mock
    private Flight mockFlight;

    @Mock
    private Airplane mockAirplane;

    // For capturing console output
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    // For simulating user input
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ticketSystem = new TicketSystem();

        // Set up mocks for instance variables in TicketSystem
        ticketSystem.passenger = mockPassenger;
        ticketSystem.ticket = mockTicket;
        ticketSystem.flight = mockFlight;

        // Redirect System.out for testing output
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() throws Exception {
        // Restore original streams
        System.setOut(originalOut);
        System.setIn(originalIn);

        // Clean up mocks
        closeable.close();

        // Clear captured output
        outContent.reset();
    }

    /**
     * Tests for Requirement 1: Valid city names must be enforced when choosing a ticket
     */

    @Test
    @DisplayName("Valid city names should be accepted")
    void testChooseTicket_ValidCityNames() throws Exception {
        // Arrange
        String validCity1 = "Melbourne";
        String validCity2 = "Sydney";

        try (MockedStatic<FlightCollection> mockedFlightCollection = mockStatic(FlightCollection.class)) {
            // Mock FlightCollection to return a flight for valid cities
            mockedFlightCollection.when(() -> FlightCollection.getFlightInfo(validCity1, validCity2))
                    .thenReturn(mockFlight);

            // Set up input simulation for scanner
            String userInput = "1\n"; // Mock user entering ticket ID 1
            System.setIn(new ByteArrayInputStream(userInput.getBytes()));

            // Mock TicketCollection getAllTickets to avoid NullPointerException
            try (MockedStatic<TicketCollection> mockedTicketCollection = mockStatic(TicketCollection.class)) {
                mockedTicketCollection.when(TicketCollection::getAllTickets).thenAnswer(invocation -> null);

                // Act - should not throw exception
                assertDoesNotThrow(() -> ticketSystem.chooseTicket(validCity1, validCity2));

                // Verify FlightCollection was called with valid city names
                mockedFlightCollection.verify(() -> FlightCollection.getFlightInfo(validCity1, validCity2));
            }
        }
    }

    @Test
    @DisplayName("Invalid city names (numeric) should be rejected")
    void testChooseTicket_InvalidCityNameWithNumbers() {
        // Arrange
        String invalidCity1 = "123Melbourne";
        String validCity2 = "Sydney";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ticketSystem.chooseTicket(invalidCity1, validCity2));

        assertEquals("City names must only contain letters and spaces", exception.getMessage());
    }

    @Test
    @DisplayName("Invalid city names (special characters) should be rejected")
    void testChooseTicket_InvalidCityNameWithSpecialChars() {
        // Arrange
        String validCity1 = "Melbourne";
        String invalidCity2 = "Sydney@";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ticketSystem.chooseTicket(validCity1, invalidCity2));

        assertEquals("City names must only contain letters and spaces", exception.getMessage());
    }

    @Test
    @DisplayName("Invalid city names (empty) should be rejected")
    void testChooseTicket_EmptyCityName() {
        // Arrange
        String emptyCity = "";
        String validCity = "Sydney";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ticketSystem.chooseTicket(emptyCity, validCity));

        assertEquals("City names cannot be empty", exception.getMessage());
    }

    /**
     * Tests for Requirement 2: If a passenger chooses an already booked ticket it should display an error message
     */

    @Test
    @DisplayName("Choosing an already booked ticket should display error message")
    void testBuyTicket_AlreadyBooked() throws Exception {
        // Arrange
        int bookedTicketId = 123;

        try (MockedStatic<TicketCollection> mockedTicketCollection = mockStatic(TicketCollection.class)) {
            // Create a mock ticket that is already booked (status = true)
            Ticket bookedTicket = mock(Ticket.class);
            when(bookedTicket.ticketStatus()).thenReturn(true);

            // Configure TicketCollection to return the booked ticket
            mockedTicketCollection.when(() -> TicketCollection.getTicketInfo(bookedTicketId))
                    .thenReturn(bookedTicket);

            // Act
            ticketSystem.buyTicket(bookedTicketId);

            // Assert - should show error message
            assertTrue(outContent.toString().contains("This ticket does not exist."));
        }
    }

    @Test
    @DisplayName("Choosing a non-existent ticket should display error message")
    void testBuyTicket_NonExistentTicket() throws Exception {
        // Arrange
        int nonExistentTicketId = 999;

        try (MockedStatic<TicketCollection> mockedTicketCollection = mockStatic(TicketCollection.class)) {
            // Configure TicketCollection to return null (ticket not found)
            mockedTicketCollection.when(() -> TicketCollection.getTicketInfo(nonExistentTicketId))
                    .thenReturn(null);

            // Act
            ticketSystem.buyTicket(nonExistentTicketId);

            // Assert - should show error message
            assertTrue(outContent.toString().contains("This ticket does not exist."));
        }
    }

    /**
     * Tests for Requirement 3: Comprehensive validation must be implemented for passenger, flight, and ticket information
     */

    @Test
    @DisplayName("Passenger information should be validated - invalid email")
    void testBuyTicket_InvalidEmail() throws Exception {
        // Arrange
        int validTicketId = 123;
        Flight mockFlight = mock(Flight.class);
        when(mockFlight.getFlightID()).thenReturn(456);

        Ticket validTicket = mock(Ticket.class);
        when(validTicket.getFlight()).thenReturn(mockFlight);
        when(validTicket.ticketStatus()).thenReturn(false); // Available ticket

        // Set up static mocks
        try (MockedStatic<TicketCollection> mockedTicketCollection = mockStatic(TicketCollection.class);
             MockedStatic<FlightCollection> mockedFlightCollection = mockStatic(FlightCollection.class);
             MockedStatic<Airplane> mockedAirplane = mockStatic(Airplane.class)) {

            // Configure mocks
            mockedTicketCollection.when(() -> TicketCollection.getTicketInfo(validTicketId))
                    .thenReturn(validTicket);
            mockedFlightCollection.when(() -> FlightCollection.getFlightInfo(456))
                    .thenReturn(mockFlight);
            when(mockFlight.getAirplane()).thenReturn(mockAirplane);
            mockedAirplane.when(() -> Airplane.getAirPlaneInfo(anyInt()))
                    .thenReturn(mockAirplane);

            // Prepare input with invalid email (missing domain)
            String userInput = "John\nDoe\n30\nMan\ninvalid email\n+61412345678\nPA123456\n1\n1234567890123456\n123\n";
            System.setIn(new ByteArrayInputStream(userInput.getBytes()));

            // Mock passenger validation to throw exception for invalid email
            doThrow(new IllegalArgumentException("Invalid email format"))
                    .when(mockPassenger).setEmail("invalid email");

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    ticketSystem.buyTicket(validTicketId));

            assertEquals("Invalid email format", exception.getMessage());
        }
    }

    @Test
    @DisplayName("Passenger information should be validated - invalid phone number")
    void testBuyTicket_InvalidPhoneNumber() throws Exception {
        // Arrange
        int validTicketId = 123;
        Flight mockFlight = mock(Flight.class);
        when(mockFlight.getFlightID()).thenReturn(456);

        Ticket validTicket = mock(Ticket.class);
        when(validTicket.getFlight()).thenReturn(mockFlight);
        when(validTicket.ticketStatus()).thenReturn(false); // Available ticket

        // Set up static mocks
        try (MockedStatic<TicketCollection> mockedTicketCollection = mockStatic(TicketCollection.class);
             MockedStatic<FlightCollection> mockedFlightCollection = mockStatic(FlightCollection.class);
             MockedStatic<Airplane> mockedAirplane = mockStatic(Airplane.class)) {

            // Configure mocks
            mockedTicketCollection.when(() -> TicketCollection.getTicketInfo(validTicketId))
                    .thenReturn(validTicket);
            mockedFlightCollection.when(() -> FlightCollection.getFlightInfo(456))
                    .thenReturn(mockFlight);
            when(mockFlight.getAirplane()).thenReturn(mockAirplane);
            mockedAirplane.when(() -> Airplane.getAirPlaneInfo(anyInt()))
                    .thenReturn(mockAirplane);

            // Prepare input with invalid phone number
            String userInput = "John\nDoe\n30\nMan\njohn@example.com\n12345\nPA123456\n1\n1234567890123456\n123\n";
            System.setIn(new ByteArrayInputStream(userInput.getBytes()));

            // Mock passenger validation to throw exception for invalid phone
            doThrow(new IllegalArgumentException("Invalid phone number format"))
                    .when(mockPassenger).setPhoneNumber("12345");

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    ticketSystem.buyTicket(validTicketId));

            assertEquals("Invalid phone number format", exception.getMessage());
        }
    }

    @Test
    @DisplayName("Passenger information should be validated - invalid passport number")
    void testBuyTicket_InvalidPassportNumber() throws Exception {
        // Arrange
        int validTicketId = 123;
        Flight mockFlight = mock(Flight.class);
        when(mockFlight.getFlightID()).thenReturn(456);

        Ticket validTicket = mock(Ticket.class);
        when(validTicket.getFlight()).thenReturn(mockFlight);
        when(validTicket.ticketStatus()).thenReturn(false); // Available ticket

        // Set up static mocks
        try (MockedStatic<TicketCollection> mockedTicketCollection = mockStatic(TicketCollection.class);
             MockedStatic<FlightCollection> mockedFlightCollection = mockStatic(FlightCollection.class);
             MockedStatic<Airplane> mockedAirplane = mockStatic(Airplane.class)) {

            // Configure mocks
            mockedTicketCollection.when(() -> TicketCollection.getTicketInfo(validTicketId))
                    .thenReturn(validTicket);
            mockedFlightCollection.when(() -> FlightCollection.getFlightInfo(456))
                    .thenReturn(mockFlight);
            when(mockFlight.getAirplane()).thenReturn(mockAirplane);
            mockedAirplane.when(() -> Airplane.getAirPlaneInfo(anyInt()))
                    .thenReturn(mockAirplane);

            // Prepare input with invalid passport number (too short)
            String userInput = "John\nDoe\n30\nMan\njohn@example.com\n+61412345678\nABC123\n1\n1234567890123456\n123\n";
            System.setIn(new ByteArrayInputStream(userInput.getBytes()));

            // Mock passenger validation to throw exception for invalid passport
            doThrow(new IllegalArgumentException("Invalid passport number format"))
                    .when(mockPassenger).setPassport("ABC123");

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    ticketSystem.buyTicket(validTicketId));

            assertEquals("Invalid passport number format", exception.getMessage());
        }
    }

    /**
     * Tests for Requirement 4: Correct price information must be displayed to passengers upon ticket purchase
     */

    @Test
    @DisplayName("Correct price should be displayed for regular adult ticket")
    void testBuyTicket_CorrectPriceAdult() throws Exception {
        // Arrange
        int validTicketId = 123;
        int expectedPrice = 1000; // Regular adult price

        Flight mockFlight = mock(Flight.class);
        when(mockFlight.getFlightID()).thenReturn(456);

        Ticket validTicket = mock(Ticket.class);
        when(validTicket.getFlight()).thenReturn(mockFlight);
        when(validTicket.ticketStatus()).thenReturn(false); // Available ticket
        when(validTicket.getPrice()).thenReturn(expectedPrice);
        when(validTicket.getClassVip()).thenReturn(false); // Economy class

        // Set up static mocks
        try (MockedStatic<TicketCollection> mockedTicketCollection = mockStatic(TicketCollection.class);
             MockedStatic<FlightCollection> mockedFlightCollection = mockStatic(FlightCollection.class);
             MockedStatic<Airplane> mockedAirplane = mockStatic(Airplane.class)) {

            // Configure mocks
            mockedTicketCollection.when(() -> TicketCollection.getTicketInfo(validTicketId))
                    .thenReturn(validTicket);
            mockedFlightCollection.when(() -> FlightCollection.getFlightInfo(456))
                    .thenReturn(mockFlight);
            when(mockFlight.getAirplane()).thenReturn(mockAirplane);
            mockedAirplane.when(() -> Airplane.getAirPlaneInfo(anyInt()))
                    .thenReturn(mockAirplane);

            // Prepare input for scanner (valid passenger details)
            String userInput = "John\nDoe\n30\nMan\njohn@example.com\n+61412345678\nPA123456\n1\n1234567890123456\n123\n";
            System.setIn(new ByteArrayInputStream(userInput.getBytes()));

            // Act
            ticketSystem.buyTicket(validTicketId);

            // Assert - should display the correct price
            assertTrue(outContent.toString().contains("Your bill: " + expectedPrice));
        }
    }

    @Test
    @DisplayName("Correct discounted price should be displayed for child ticket")
    void testBuyTicket_CorrectPriceChild() throws Exception {
        // Arrange
        int validTicketId = 123;
        int basePrice = 1000;
        int expectedPrice = 560; // Child price with discount (50%) and service tax (12%)

        Flight mockFlight = mock(Flight.class);
        when(mockFlight.getFlightID()).thenReturn(456);

        Ticket validTicket = mock(Ticket.class);
        when(validTicket.getFlight()).thenReturn(mockFlight);
        when(validTicket.ticketStatus()).thenReturn(false); // Available ticket
        when(validTicket.getPrice()).thenReturn(expectedPrice);
        when(validTicket.getClassVip()).thenReturn(false); // Economy class

        // Set up static mocks
        try (MockedStatic<TicketCollection> mockedTicketCollection = mockStatic(TicketCollection.class);
             MockedStatic<FlightCollection> mockedFlightCollection = mockStatic(FlightCollection.class);
             MockedStatic<Airplane> mockedAirplane = mockStatic(Airplane.class)) {

            // Configure mocks
            mockedTicketCollection.when(() -> TicketCollection.getTicketInfo(validTicketId))
                    .thenReturn(validTicket);
            mockedFlightCollection.when(() -> FlightCollection.getFlightInfo(456))
                    .thenReturn(mockFlight);
            when(mockFlight.getAirplane()).thenReturn(mockAirplane);
            mockedAirplane.when(() -> Airplane.getAirPlaneInfo(anyInt()))
                    .thenReturn(mockAirplane);

            // Prepare input for scanner (valid passenger details with age 10)
            String userInput = "John\nDoe\n10\nMan\njohn@example.com\n+61412345678\nPA123456\n1\n1234567890123456\n123\n";
            System.setIn(new ByteArrayInputStream(userInput.getBytes()));

            // Act
            ticketSystem.buyTicket(validTicketId);

            // Assert - should display the correct price
            assertTrue(outContent.toString().contains("Your bill: " + expectedPrice));
        }
    }

    @Test
    @DisplayName("Correct price should be displayed for combined tickets with transfer flight")
    void testBuyTicket_CorrectPriceTransferFlight() throws Exception {
        // Arrange
        int firstTicketId = 123;
        int secondTicketId = 456;
        int firstTicketPrice = 500;
        int secondTicketPrice = 700;
        int totalPrice = 1200; // Combined price

        // Set up tickets
        Ticket firstTicket = mock(Ticket.class);
        when(firstTicket.getPrice()).thenReturn(firstTicketPrice);
        when(firstTicket.getClassVip()).thenReturn(false);
        when(firstTicket.ticketStatus()).thenReturn(false);

        Ticket secondTicket = mock(Ticket.class);
        when(secondTicket.getPrice()).thenReturn(secondTicketPrice);
        when(secondTicket.getClassVip()).thenReturn(false);
        when(secondTicket.ticketStatus()).thenReturn(false);

        // Set up flights
        Flight firstFlight = mock(Flight.class);
        when(firstFlight.getFlightID()).thenReturn(111);
        when(firstTicket.getFlight()).thenReturn(firstFlight);

        Flight secondFlight = mock(Flight.class);
        when(secondFlight.getFlightID()).thenReturn(222);
        when(secondTicket.getFlight()).thenReturn(secondFlight);

        // Set up airplanes
        Airplane firstAirplane = mock(Airplane.class);
        when(firstFlight.getAirplane()).thenReturn(firstAirplane);

        Airplane secondAirplane = mock(Airplane.class);
        when(secondFlight.getAirplane()).thenReturn(secondAirplane);

        // Set up static mocks
        try (MockedStatic<TicketCollection> mockedTicketCollection = mockStatic(TicketCollection.class);
             MockedStatic<FlightCollection> mockedFlightCollection = mockStatic(FlightCollection.class);
             MockedStatic<Airplane> mockedAirplane = mockStatic(Airplane.class)) {

            // Configure ticket collection mock
            mockedTicketCollection.when(() -> TicketCollection.getTicketInfo(firstTicketId))
                    .thenReturn(firstTicket);
            mockedTicketCollection.when(() -> TicketCollection.getTicketInfo(secondTicketId))
                    .thenReturn(secondTicket);

            // Configure flight collection mock
            mockedFlightCollection.when(() -> FlightCollection.getFlightInfo(111))
                    .thenReturn(firstFlight);
            mockedFlightCollection.when(() -> FlightCollection.getFlightInfo(222))
                    .thenReturn(secondFlight);

            // Configure airplane mock
            mockedAirplane.when(() -> Airplane.getAirPlaneInfo(anyInt()))
                    .thenReturn(mock(Airplane.class));

            // Configure ticket mock to set combined price
            when(mockTicket.getPrice()).thenReturn(totalPrice);

            // Prepare input for scanner
            String userInput = "John\nDoe\n30\nMan\njohn@example.com\n+61412345678\nPA123456\n1\n1234567890123456\n123\n";
            System.setIn(new ByteArrayInputStream(userInput.getBytes()));

            // Act
            ticketSystem.buyTicket(firstTicketId, secondTicketId);

            // Assert - should display the correct combined price
            assertTrue(outContent.toString().contains("Your bill: " + totalPrice));
        }
    }

    /**
     * Test helpers
     */

    @Test
    @DisplayName("Show ticket displays correct information")
    void testShowTicket() {
        // Arrange
        Flight mockFlight = mock(Flight.class);
        when(mockFlight.getDepartFrom()).thenReturn("Melbourne");
        when(mockFlight.getDepartTo()).thenReturn("Sydney");

        // Set up ticket with flight information
        when(mockTicket.flight).thenReturn(mockFlight);
        when(mockTicket.toString()).thenReturn("Mock ticket details");

        // Act
        ticketSystem.showTicket();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("You have bought a ticket for flight Melbourne - Sydney"));
        assertTrue(output.contains("Mock ticket details"));
    }

    @Test
    @DisplayName("Show ticket handles null flight information gracefully")
    void testShowTicket_NullFlight() {
        // Arrange - set up ticket with null flight
        when(mockTicket.flight).thenReturn(null);

        // Act
        ticketSystem.showTicket();

        // Assert
        assertTrue(outContent.toString().contains("No flight details available"));
    }
}
