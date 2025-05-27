package com.ars.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightCollectionTest {

    private Flight validFlight;

    @BeforeEach
    void setUp() {
        FlightCollection.flights = new ArrayList<>();
        validFlight = mock(Flight.class);
    }

    // BVT: Boundary Value Testing for invalid (empty) city names
    // Requirement 2: City names must be valid
    @Test
    void testGetFlightByEmptyCityNames() {
        assertNull(FlightCollection.getFlightInfo("", "Sydney"));
        assertNull(FlightCollection.getFlightInfo("Melbourne", ""));
    }

    // ET: Equivalence Class Testing for valid city names
    // Requirement 3: Retrieving flight information should correctly return a valid flight object
    @Test
    void testGetFlightByValidCityNames() {
        when(validFlight.getDepartFrom()).thenReturn("Melbourne");
        when(validFlight.getDepartTo()).thenReturn("Sydney");
        ArrayList<Flight> toAdd = new ArrayList<>();
        toAdd.add(validFlight);
        FlightCollection.addFlights(toAdd);

        Flight result = FlightCollection.getFlightInfo("Melbourne", "Sydney");
        assertEquals(validFlight, result);
    }

    // ET: Equivalence Class Testing for valid flight ID
    // Requirement 3: Retrieving flight information should correctly return a valid flight object
    @Test
    void testGetFlightByValidId() {
        when(validFlight.getFlightID()).thenReturn(200);
        ArrayList<Flight> toAdd = new ArrayList<>();
        toAdd.add(validFlight);
        FlightCollection.addFlights(toAdd);

        Flight result = FlightCollection.getFlightInfo(200);
        assertEquals(validFlight, result);
    }

    // DTT: Decision Table Testing - Add flight (valid vs duplicate)
    // Requirement 1: When adding a flight into the system, test if it conforms to the requirements
    @Test
    void testAddNewAndDuplicateFlights() {
        when(validFlight.getCode()).thenReturn("FL123");
        when(validFlight.getDateFrom()).thenReturn(Timestamp.valueOf("2024-10-10 00:00:00"));

        ArrayList<Flight> firstAdd = new ArrayList<>();
        firstAdd.add(validFlight);
        FlightCollection.addFlights(firstAdd); // should pass

        ArrayList<Flight> duplicateAdd = new ArrayList<>();
        duplicateAdd.add(validFlight);

        assertThrows(IllegalArgumentException.class, () -> FlightCollection.addFlights(duplicateAdd));
    }

    // ET: Get by destination city (equivalence class)
    // Requirement 3: Retrieving flight information should correctly return a valid flight object
    @Test
    void testGetFlightByDestinationCity() {
        when(validFlight.getDepartTo()).thenReturn("Brisbane");
        ArrayList<Flight> toAdd = new ArrayList<>();
        toAdd.add(validFlight);
        FlightCollection.addFlights(toAdd);

        Flight result = FlightCollection.getFlightInfo("Brisbane");
        assertEquals(validFlight, result);
    }
}
