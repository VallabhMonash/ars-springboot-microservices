package com.ars.api.service;

import com.ars.unit.Flight;
import com.ars.unit.FlightCollection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {
    public List<Flight> listFlights() {
        return FlightCollection.getFlights();
    }
}
