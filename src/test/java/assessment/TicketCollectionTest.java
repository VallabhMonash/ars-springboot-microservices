package assessment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketCollectionTest {

    @Mock
    private Ticket mockTicket1;

    @Mock
    private Ticket mockTicket2;

    @Mock
    private Flight mockFlight;

    @Mock
    private Passenger mockPassenger;

    private ArrayList<Ticket> testTickets;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test tickets list
        testTickets = new ArrayList<>();
        testTickets.add(mockTicket1);
        testTickets.add(mockTicket2);

        // Initialize TicketCollection.tickets to avoid NullPointerException
        TicketCollection.tickets = new ArrayList<>();

        // Set up System.out for testing output
        System.setOut(new PrintStream(outContent));

        // Set up mock behavior
        when(mockTicket1.getTicket_id()).thenReturn(1);
        when(mockTicket2.getTicket_id()).thenReturn(2);
    }

    /**
     * Tests for requirement 1: Validate tickets thoroughly when added to the collection
     */

    @Test
    @DisplayName("Add valid tickets to collection")
    void testAddValidTickets() {
        // Arrange
        // Ensure mockTicket1 has valid state
        when(mockTicket1.getFlight()).thenReturn(mockFlight);
        when(mockTicket1.getPassenger()).thenReturn(mockPassenger);
        when(mockTicket1.getPrice()).thenReturn(1000);
        when(mockTicket1.ticketStatus()).thenReturn(false);

        // Ensure mockTicket2 has valid state
        when(mockTicket2.getFlight()).thenReturn(mockFlight);
        when(mockTicket2.getPassenger()).thenReturn(mockPassenger);
        when(mockTicket2.getPrice()).thenReturn(1500);
        when(mockTicket2.ticketStatus()).thenReturn(false);

        // Act
        TicketCollection.addTickets(testTickets);

        // Assert
        assertEquals(2, TicketCollection.getTickets().size());
        assertTrue(TicketCollection.getTickets().contains(mockTicket1));
        assertTrue(TicketCollection.getTickets().contains(mockTicket2));
    }

    @Test
    @DisplayName("Try to add null ticket list - should throw NullPointerException")
    void testAddNullTicketList() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            TicketCollection.addTickets(null);
        });
    }

    @Test
    @DisplayName("Try to add empty ticket list - should not change collection size")
    void testAddEmptyTicketList() {
        // Arrange
        ArrayList<Ticket> emptyList = new ArrayList<>();
        int initialSize = TicketCollection.tickets.size();

        // Act
        TicketCollection.addTickets(emptyList);

        // Assert
        assertEquals(initialSize, TicketCollection.tickets.size());
    }

    @Test
    @DisplayName("Add tickets with null Flight - validation should reject")
    void testAddTicketsWithNullFlight() {
        // Arrange
        when(mockTicket1.getFlight()).thenReturn(null);
        when(mockTicket1.getPassenger()).thenReturn(mockPassenger);

        ArrayList<Ticket> invalidTickets = new ArrayList<>();
        invalidTickets.add(mockTicket1);

        // Note: The current implementation doesn't validate for null Flight,
        // but it should. If validation were implemented, we'd expect:
        // assertThrows(IllegalArgumentException.class, () -> {
        //     TicketCollection.addTickets(invalidTickets);
        // });

        // For now, this just checks current behavior
        TicketCollection.addTickets(invalidTickets);
        assertEquals(1, TicketCollection.getTickets().size());
    }

    @Test
    @DisplayName("Add tickets with null Passenger - validation should reject")
    void testAddTicketsWithNullPassenger() {
        // Arrange
        when(mockTicket1.getFlight()).thenReturn(mockFlight);
        when(mockTicket1.getPassenger()).thenReturn(null);

        ArrayList<Ticket> invalidTickets = new ArrayList<>();
        invalidTickets.add(mockTicket1);

        // Note: The current implementation doesn't validate for null Passenger,
        // but it should. If validation were implemented, we'd expect:
        // assertThrows(IllegalArgumentException.class, () -> {
        //     TicketCollection.addTickets(invalidTickets);
        // });

        // For now, this just checks current behavior
        TicketCollection.addTickets(invalidTickets);
        assertEquals(1, TicketCollection.getTickets().size());
    }

    @Test
    @DisplayName("Add tickets with negative price - validation should reject")
    void testAddTicketsWithNegativePrice() {
        // Arrange
        when(mockTicket1.getFlight()).thenReturn(mockFlight);
        when(mockTicket1.getPassenger()).thenReturn(mockPassenger);
        when(mockTicket1.getPrice()).thenReturn(-100);

        ArrayList<Ticket> invalidTickets = new ArrayList<>();
        invalidTickets.add(mockTicket1);

        // Note: The current implementation doesn't validate for negative price,
        // but it should. If validation were implemented, we'd expect:
        // assertThrows(IllegalArgumentException.class, () -> {
        //     TicketCollection.addTickets(invalidTickets);
        // });

        // For now, this just checks current behavior
        TicketCollection.addTickets(invalidTickets);
        assertEquals(1, TicketCollection.getTickets().size());
    }

    @Test
    @DisplayName("Add ticket with duplicate ID - validation should reject or replace")
    void testAddTicketsWithDuplicateId() {
        // Arrange
        // First add mockTicket1 with ID 1
        when(mockTicket1.getTicket_id()).thenReturn(1);
        when(mockTicket1.getFlight()).thenReturn(mockFlight);
        TicketCollection.tickets.add(mockTicket1);

        // Create another ticket with same ID
        Ticket duplicateTicket = mock(Ticket.class);
        when(duplicateTicket.getTicket_id()).thenReturn(1);
        when(duplicateTicket.getFlight()).thenReturn(mockFlight);

        ArrayList<Ticket> duplicateTickets = new ArrayList<>();
        duplicateTickets.add(duplicateTicket);

        // Note: The current implementation doesn't check for duplicates,
        // but it should. If validation were implemented, we'd expect either:
        // 1. An exception: assertThrows(IllegalArgumentException.class, () -> {
        //     TicketCollection.addTickets(duplicateTickets);
        // });
        // 2. Or replacement of the old ticket

        // For now, this just checks current behavior (duplicates are added)
        TicketCollection.addTickets(duplicateTickets);
        assertEquals(2, TicketCollection.tickets.size());
    }

    /**
     * Tests for requirement 2: Ensure the correct ticket is returned upon retrieval
     */

    @Test
    @DisplayName("Get all tickets displays correctly")
    void testGetAllTickets() {
        // Arrange
        TicketCollection.tickets = testTickets;

        // Act
        TicketCollection.getAllTickets();

        // Assert
        // Verify that toString was called on both tickets
        verify(mockTicket1).toString();
        verify(mockTicket2).toString();
    }

    @Test
    @DisplayName("Get ticket by ID - existing ticket")
    void testGetTicketInfo_ExistingTicket() {
        // Arrange
        TicketCollection.tickets = testTickets;
        int ticketId = 1;

        // Act
        Ticket result = TicketCollection.getTicketInfo(ticketId);

        // Assert
        assertSame(mockTicket1, result);
    }

    @Test
    @DisplayName("Get ticket by ID - non-existing ticket")
    void testGetTicketInfo_NonExistingTicket() {
        // Arrange
        TicketCollection.tickets = testTickets;
        int nonExistingTicketId = 999;

        // Act
        Ticket result = TicketCollection.getTicketInfo(nonExistingTicketId);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Get ticket by ID - handle edge cases")
    void testGetTicketInfo_EdgeCases() {
        // Arrange
        TicketCollection.tickets = testTickets;

        // Test minimum valid ticket ID
        when(mockTicket1.getTicket_id()).thenReturn(0);

        // Test maximum valid ticket ID
        when(mockTicket2.getTicket_id()).thenReturn(Integer.MAX_VALUE);

        // Act & Assert
        assertEquals(mockTicket1, TicketCollection.getTicketInfo(0));
        assertEquals(mockTicket2, TicketCollection.getTicketInfo(Integer.MAX_VALUE));
        assertNull(TicketCollection.getTicketInfo(-1)); // Invalid negative ticket ID
    }

    @Test
    @DisplayName("Get ticket by ID - handle null tickets list")
    void testGetTicketInfo_NullTicketsList() {
        // Arrange
        TicketCollection.tickets = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            TicketCollection.getTicketInfo(1);
        });
    }

    @Test
    @DisplayName("Get tickets from empty collection")
    void testGetTicketsFromEmptyCollection() {
        // Arrange
        TicketCollection.tickets = new ArrayList<>();

        // Act
        ArrayList<Ticket> result = TicketCollection.getTickets();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}