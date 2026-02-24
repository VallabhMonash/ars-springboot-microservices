package com.ars.api.controller;

import com.ars.api.dto.DtoMapper;
import com.ars.api.dto.PassengerRequest;
import com.ars.api.dto.PassengerResponse;
import com.ars.api.service.PassengerService;
import com.ars.unit.Passenger;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    public PassengerResponse createPassenger(@Valid @RequestBody PassengerRequest request) {
        Passenger passenger = passengerService.createPassenger(request);
        return DtoMapper.toPassengerResponse(passenger);
    }

    @GetMapping
    public List<PassengerResponse> listPassengers() {
        return passengerService.listPassengers()
                .stream()
                .map(DtoMapper::toPassengerResponse)
                .collect(Collectors.toList());
    }
}
