package com.ars.api.dto;

public class TicketResponse {
    private int ticketId;
    private int price;
    private boolean classVip;
    private boolean booked;
    private int flightId;
    private PassengerResponse passenger;

    public TicketResponse() {
    }

    public TicketResponse(int ticketId, int price, boolean classVip, boolean booked,
            int flightId, PassengerResponse passenger) {
        this.ticketId = ticketId;
        this.price = price;
        this.classVip = classVip;
        this.booked = booked;
        this.flightId = flightId;
        this.passenger = passenger;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isClassVip() {
        return classVip;
    }

    public void setClassVip(boolean classVip) {
        this.classVip = classVip;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public PassengerResponse getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerResponse passenger) {
        this.passenger = passenger;
    }
}
