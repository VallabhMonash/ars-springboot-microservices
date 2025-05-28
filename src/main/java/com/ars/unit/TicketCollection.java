package com.ars.unit;

import java.util.ArrayList;

public class TicketCollection {

    public static ArrayList<Ticket> tickets = new ArrayList<>();

    public static ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public static void addTickets(ArrayList<Ticket> tickets_db) {
        for (Ticket t : tickets_db) {
            if (t == null) throw new IllegalArgumentException("Ticket cannot be null");
            if (tickets.contains(t)) {
                throw new IllegalStateException("Duplicate ticket ID: " + t.getTicketId());
            }
            tickets.add(t);
        }
    }


    public static void getAllTickets() {
        //display all available tickets from the Ticket collection
    }

    public static Ticket getTicketInfo(int ticket_id) {
        for (Ticket t : tickets) {
            if (t.getTicketId() == ticket_id) {
                return t;
            }
        }
        return null;
    }


}
