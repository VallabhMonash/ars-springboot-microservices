package com.ars.unit;

import java.util.Objects;

public class Ticket {
    private int ticket_id;
    private int price;
    private Flight flight;
    private boolean classVip; //indicates if this is business class ticket or not
    private boolean status; // true: booked, false: available
    private Passenger passenger;

    public Ticket(int ticket_id, int price, Flight flight, boolean classVip, Passenger passenger) {
        this.ticket_id = ticket_id;
//        this.passenger = Objects.requireNonNull(passenger, "Passenger must not be null");
        this.passenger = passenger;
        this.flight = Objects.requireNonNull(flight, "Flight must not be null");
        this.classVip = classVip;
        this.status = false;
        setPrice(price);
    }

    public Ticket() {
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if (price < 0) throw new IllegalArgumentException("Price must be non-negative");
        this.price = price;
        saleByAge(passenger != null ? passenger.getAge() : 30); // default adult price
        serviceTax();
    }

    public void saleByAge(int age) {
        if (age < 15) {
            this.price *= 0.5;
        } else if (age >= 60) {
            this.price = 0; //100% sale for elder people
        }
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = Objects.requireNonNull(flight, "Flight must not be null");
    }

    public boolean getClassVip() {
        return classVip;
    }

    public void setClassVip(boolean classVip) {
        this.classVip = classVip;
    }

    public boolean ticketStatus() {
        return status;
    }

    public void setTicketStatus(boolean status) {
        this.status = status;
    }

    public void serviceTax() {
        this.price *= 1.12;
    } //12% service tax

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = Objects.requireNonNull(passenger, "Passenger must not be null");
    }

    public String toString() {
        return "Ticket{" + '\n' +
                "Price=" + getPrice() + "KZT, " + '\n' +
                getFlight() + '\n' + "Vip status=" + getClassVip() + '\n' +
                getPassenger() + '\n' + "Ticket was purchased=" + ticketStatus() + "\n}";
    }
}
