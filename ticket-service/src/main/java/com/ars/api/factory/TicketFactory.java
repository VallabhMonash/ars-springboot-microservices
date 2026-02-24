package com.ars.api.factory;

import com.ars.api.dto.PassengerRequest;
import com.ars.unit.Passenger;
import org.springframework.stereotype.Component;

@Component
public class TicketFactory {

    public Passenger createPassenger(PassengerRequest request) {
        Passenger.ContactInfo contact = new Passenger.ContactInfo(
                request.getEmail(),
                request.getPhoneNumber()
        );
        Passenger.PaymentInfo payment = new Passenger.PaymentInfo(
                request.getCardNumber(),
                request.getSecurityCode()
        );

        return new Passenger(
                request.getFirstName(),
                request.getSecondName(),
                request.getAge(),
                request.getGender(),
                contact,
                request.getPassport(),
                payment
        );
    }
}

