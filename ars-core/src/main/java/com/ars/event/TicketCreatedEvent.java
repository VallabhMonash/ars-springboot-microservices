package com.ars.event;

public class TicketCreatedEvent {
    private int ticketId;
    private int flightId;
    private String passengerEmail;

    public TicketCreatedEvent() {
    }

    public TicketCreatedEvent(int ticketId, int flightId, String passengerEmail) {
        this.ticketId = ticketId;
        this.flightId = flightId;
        this.passengerEmail = passengerEmail;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }
}
