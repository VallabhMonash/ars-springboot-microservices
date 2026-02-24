package com.ars.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TicketBookingRequest {
    @NotNull
    @Min(1)
    private Integer ticketId;

    @Valid
    @NotNull
    private PassengerRequest passenger;

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public PassengerRequest getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerRequest passenger) {
        this.passenger = passenger;
    }
}
