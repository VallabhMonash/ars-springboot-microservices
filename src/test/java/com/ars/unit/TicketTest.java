package com.ars.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketTest {

    private static final int PRICE = 100;

    @Mock
    private Flight mockFlight;
    @Mock
    private Passenger mockPassenger;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        // Creating a Ticket object
        ticket = new Ticket(1, PRICE, mockFlight, true, mockPassenger);
    }

    public void setAge(int age) {
        when(mockPassenger.getAge()).thenReturn(age);
        ticket.setPrice(PRICE);
    }

    // Boundary Value Testing (BVT)
    @Test
    void testBoundaryAgeDiscountChild() {
        // Boundary value for a child age: 15 shouldn't apply a discount (50%)
        setAge(15);
        assertEquals(112, ticket.getPrice(), "Price should not be halved for age = 15");

        // Age below 15 should apply the discount
        setAge(14);
        assertEquals(56, ticket.getPrice(), "Price should be halved for age < 15");

        // Age above 15 should stop the discount (checking boundary)
        setAge(16);
        assertEquals(112, ticket.getPrice(), "Price should not have a discount for adults");
    }

    @Test
    void testBoundaryAgeDiscountSenior() {
        // Boundary value for senior citizens (e.g., age = 60)
        setAge(60);
        assertEquals(0, ticket.getPrice(), "Price should be 0 for seniors (age = 60)");

        // Any age above 60 should still apply the free ticket
        setAge(61);
        assertEquals(0, ticket.getPrice(), "Price should remain 0 for seniors (age > 60)");
    }

    // Equivalence Testing (ET)
    @Test
    void testSaleByAgeForAdult() {
        // Age between 18 and 59 is considered adult (equivalence class)
        setAge(30);
        assertEquals(112, ticket.getPrice(), "Price should include 12% service tax for adult");

        // Another equivalence class: any other adult
        setAge(45);
        assertEquals(112, ticket.getPrice(), "Price should remain the same for an adult with service tax");
    }

    @Test
    void testSaleByAgeForChild() {
        // Age between 0 and 14 is considered a child (equivalence class)
        setAge(10);
        assertEquals(56, ticket.getPrice(), "Price should be halved for a child with service tax");

        // Another equivalence class: any other child
        setAge(5);
        assertEquals(56, ticket.getPrice(), "Price should be halved for a child with service tax");
    }


    @Test
    void testTicketStatus() {
        // Decision table: Checking different ticket status transitions
        assertFalse(ticket.ticketStatus(), "Ticket should not be booked initially");  // Status = false (not booked)

        ticket.setTicketStatus(true);  // Status = true (booked)
        assertTrue(ticket.ticketStatus(), "Ticket should be booked after status change");
    }
}
