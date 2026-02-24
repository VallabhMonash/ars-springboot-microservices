package com.ars.api.controller;

import com.ars.api.dto.FlightResponse;
import com.ars.api.service.FlightService;
import com.ars.unit.Flight;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<FlightResponse> listFlights() {
        List<Flight> flights = flightService.listFlights();
        return flights.stream()
                .map(f -> new FlightResponse(
                        f.getFlightID(),
                        f.getDepartFrom(),
                        f.getDepartTo(),
                        f.getCode(),
                        f.getCompany(),
                        f.getDateFrom() != null ? f.getDateFrom().toString() : null,
                        f.getDateTo() != null ? f.getDateTo().toString() : null,
                        f.getAirplane() != null ? f.getAirplane().getAirplaneID() : 0
                ))
                .collect(Collectors.toList());
    }
}

