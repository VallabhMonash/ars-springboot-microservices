package com.ars.Integration;

import static org.junit.jupiter.api.Assertions.*;

import com.ars.unit.Flight;
import com.ars.unit.Airplane;
import com.ars.unit.FlightCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

class FlightCollectionIntegrationTest {

    private Flight flight;

    @BeforeEach
    void setUp() {
        FlightCollection.flights = new ArrayList<>(); // Make sure to reset before each test

        Airplane airplane = new Airplane(1, "Boeing 737", 10, 50, 10);

        flight = new Flight(
                1, "Melbourne", "Sydney", "FL123", "Qantas",
                Timestamp.valueOf("2025-06-01 08:00:00"), Timestamp.valueOf("2025-06-01 10:00:00"),
                airplane
        );
    }


    @Test
    void testFlightFields() {
        assertEquals(1, flight.getFlightID());
        assertEquals("Sydney", flight.getDepartFrom());
        assertEquals("Melbourne", flight.getDepartTo());
        assertEquals("FL123", flight.getCode());
        assertEquals("Qantas", flight.getCompany());
        assertEquals(Timestamp.valueOf("2025-06-01 08:00:00"), flight.getDateFrom());
        assertEquals(Timestamp.valueOf("2025-06-01 10:00:00"), flight.getDateTo());
        assertEquals(1, flight.getAirplane().getAirplaneID());
    }


    @Test
    void testAddAndRetrieveFlightSuccessfully() {
        ArrayList<Flight> flightsToAdd = new ArrayList<>();
        flightsToAdd.add(flight);

        FlightCollection.addFlights(flightsToAdd);

        Flight retrieved = FlightCollection.getFlightInfo("Sydney", "Melbourne");

        assertNotNull(retrieved, "Should retrieve the flight by departure and arrival cities");

    }

    @Test
    void testAddSingleFlightSuccessfully() {
        ArrayList<Flight> flightsToAdd = new ArrayList<>();
        flightsToAdd.add(flight);

        FlightCollection.addFlights(flightsToAdd);

        assertEquals(1, FlightCollection.getFlights().size());

    }

    @Test
    void testAddDuplicateFlightShouldThrowException() {
        ArrayList<Flight> flightsToAdd = new ArrayList<>();
        flightsToAdd.add(flight);

        FlightCollection.addFlights(flightsToAdd);

        ArrayList<Flight> duplicateFlights = new ArrayList<>();
        duplicateFlights.add(flight);

        assertThrows(IllegalArgumentException.class, () -> FlightCollection.addFlights(duplicateFlights),
                "Adding a duplicate flight should throw an exception");
    }

    @Test
    void testRetrieveFlightByArrivalCitySuccessfully() {
        ArrayList<Flight> flightsToAdd = new ArrayList<>();
        flightsToAdd.add(flight);

        FlightCollection.addFlights(flightsToAdd);

        Flight retrieved = FlightCollection.getFlightInfo("Melbourne"); // Only arrival city

        assertNotNull(retrieved, "Should retrieve flight by arrival city");
        assertEquals("Melbourne", retrieved.getDepartTo(), "Arrival city should match");
    }

    @Test
    void testRetrieveFlightByFlightIdSuccessfully() {
        ArrayList<Flight> flightsToAdd = new ArrayList<>();
        flightsToAdd.add(flight);

        FlightCollection.addFlights(flightsToAdd);

        Flight retrieved = FlightCollection.getFlightInfo(1); // flight_id = 1

        assertNotNull(retrieved, "Should retrieve flight by flight ID");
        assertEquals(1, retrieved.getFlightID(), "Flight ID should match");
    }


}
