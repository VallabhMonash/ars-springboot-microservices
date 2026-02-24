package com.ars.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketCollectionTest {

    private Ticket validTicket1;
    private Ticket validTicket2;

    @BeforeEach
    void setUp() {
        TicketCollection.tickets = new ArrayList<>();

        validTicket1 = Mockito.mock(Ticket.class);
        validTicket2 = Mockito.mock(Ticket.class);

        when(validTicket1.getTicketId()).thenReturn(1001);
        when(validTicket2.getTicketId()).thenReturn(1002);
    }

    @Test // TC1: Validate ticket added successfully
    void testAddValidTickets() {
        ArrayList<Ticket> toAdd = new ArrayList<>();
        toAdd.add(validTicket1);
        toAdd.add(validTicket2);

        TicketCollection.addTickets(toAdd);

        assertEquals(2, TicketCollection.getTickets().size());
        assertTrue(TicketCollection.getTickets().contains(validTicket1));
        assertTrue(TicketCollection.getTickets().contains(validTicket2));
    }

    @Test // TC1: Null ticket list should not cause crash
    void testAddNullTicketList() {
        assertDoesNotThrow(() -> TicketCollection.addTickets(new ArrayList<>()));
    }

    @Test // TC1: Duplicate ticket should be rejected
    void testDuplicateTicketsNotAllowed() {
        ArrayList<Ticket> toAdd = new ArrayList<>();
        toAdd.add(validTicket1);
        TicketCollection.addTickets(toAdd);

        ArrayList<Ticket> duplicateList = new ArrayList<>();
        duplicateList.add(validTicket1);

        assertThrows(IllegalStateException.class, () -> TicketCollection.addTickets(duplicateList));
    }

    @Test // TC2: Retrieve existing ticket by ID
    void testRetrieveTicketById() {
        ArrayList<Ticket> toAdd = new ArrayList<>();
        toAdd.add(validTicket1);
        TicketCollection.addTickets(toAdd);

        Ticket result = TicketCollection.getTicketInfo(1001);
        assertEquals(validTicket1, result);
    }

    @Test // TC2: Retrieve non-existent ticket should return null
    void testRetrieveNonExistentTicketReturnsNull() {
        Ticket result = TicketCollection.getTicketInfo(9999);
        assertNull(result);
    }
}
