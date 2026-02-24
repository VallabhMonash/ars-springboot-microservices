package com.ars.api.dto;

import com.ars.unit.Flight;
import com.ars.unit.Passenger;
import com.ars.unit.Ticket;

public final class DtoMapper {
    private DtoMapper() {}

    public static PassengerResponse toPassengerResponse(Passenger passenger) {
        if (passenger == null) return null;
        return new PassengerResponse(
                passenger.getFirstName(),
                passenger.getSecondName(),
                passenger.getAge(),
                passenger.getGender(),
                passenger.getEmail(),
                passenger.getPhoneNumber(),
                passenger.getPassport(),
                passenger.getCardNumber(),
                passenger.getSecurityCode()
        );
    }

    public static TicketResponse toTicketResponse(Ticket ticket) {
        if (ticket == null) return null;
        return new TicketResponse(
                ticket.getTicketId(),
                ticket.getPrice(),
                ticket.isClassVip(),
                ticket.ticketStatus(),
                ticket.getFlight() != null ? ticket.getFlight().getFlightID() : 0,
                toPassengerResponse(ticket.getPassenger())
        );
    }
}

