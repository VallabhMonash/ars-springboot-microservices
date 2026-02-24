package com.ars.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A utility class for managing Ticket storage and lookup.
 */
public final class TicketCollection {
    // Hide implicit constructor
    private TicketCollection() {
        // prevent instantiation
    }

    // Use interface type, unmodifiable list for consumers
    public static List<Ticket> tickets = new ArrayList<>();

    /**
     * Returns an unmodifiable view of all tickets.
     */
    public static List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }

    /**
     * Adds a batch of tickets, rejecting nulls and duplicates.
     * @param newTickets the tickets to add
     * @throws IllegalArgumentException if newTickets or any ticket is null
     * @throws IllegalStateException if a ticket ID already exists
     */
    public static void addTickets(List<Ticket> newTickets) {
        Objects.requireNonNull(newTickets, "Ticket list must not be null");
        for (Ticket t : newTickets) {
            Objects.requireNonNull(t, "Ticket cannot be null");
            if (tickets.contains(t)) {
                throw new IllegalStateException("Duplicate ticket ID: " + t.getTicketId());
            }
            tickets.add(t);
        }
    }

    /**
     * Finds a ticket by its ID.
     * @param ticketId the ID to lookup
     * @return the matching Ticket or null if not found
     */
    public static Ticket getTicketInfo(int ticketId) {
        for (Ticket t : tickets) {
            if (t.getTicketId() == ticketId) {
                return t;
            }
        }
        return null;
    }
}
