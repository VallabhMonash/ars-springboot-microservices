package com.ars.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ars.api.dto.PassengerRequest;
import com.ars.api.factory.TicketFactory;
import com.ars.unit.Passenger;

@Service
public class PassengerService {
    private final Map<String, Passenger> passengersByEmail = new ConcurrentHashMap<>();
    private final TicketFactory ticketFactory;

    public PassengerService(TicketFactory ticketFactory) {
        this.ticketFactory = ticketFactory;
    }

    public Passenger createPassenger(PassengerRequest request) {
        Passenger passenger = ticketFactory.createPassenger(request);
        passengersByEmail.put(passenger.getEmail(), passenger);
        return passenger;
    }

    public Passenger getByEmail(String email) {
        return passengersByEmail.get(email);
    }

    public List<Passenger> listPassengers() {
        return new ArrayList<>(passengersByEmail.values());
    }
}

