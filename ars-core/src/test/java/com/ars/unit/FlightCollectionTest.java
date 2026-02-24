package com.ars.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    // Null and “no match” for the two-city lookup
    @Test
    void lookupByCitiesWithNullOrNoMatchReturnsNull() {
        // no flights in collection
        assertNull(FlightCollection.getFlightInfo(null, "Sydney"));
        assertNull(FlightCollection.getFlightInfo("Melbourne", null));
        // add a flight that doesn’t match
        Flight f = mock(Flight.class);
        when(f.getDepartFrom()).thenReturn("X");
        when(f.getDepartTo()).thenReturn("Y");
        FlightCollection.addFlights(List.of(f));
        assertNull(FlightCollection.getFlightInfo("A", "B"));
    }

    // Blank and “no match” for single-city lookup
    @Test
    void lookupByDestinationWithNullOrNoMatchReturnsNull() {
        assertNull(FlightCollection.getFlightInfo((String)null));
        assertNull(FlightCollection.getFlightInfo(""));
        Flight f = mock(Flight.class);
        when(f.getDepartTo()).thenReturn("Brisbane");
        FlightCollection.addFlights(List.of(f));
        assertNull(FlightCollection.getFlightInfo("Perth"));
    }

    // Invalid ID lookup
    @Test
    void lookupByIdNotFoundReturnsNull() {
        Flight f = mock(Flight.class);
        when(f.getFlightID()).thenReturn(99);
        FlightCollection.addFlights(List.of(f));
        assertNull(FlightCollection.getFlightInfo(  0));
        assertNull(FlightCollection.getFlightInfo(100));
    }

    // Case-insensitive matching
    @Test
    void lookupIsCaseInsensitive() {
        Flight f = mock(Flight.class);
        when(f.getDepartFrom()).thenReturn("Melbourne");
        when(f.getDepartTo())  .thenReturn("Sydney");
        FlightCollection.addFlights(List.of(f));
        assertSame(f, FlightCollection.getFlightInfo("melbourne", "SYDNEY"));
        when(f.getDepartTo()).thenReturn("Brisbane");
        FlightCollection.clearFlights();
        FlightCollection.addFlights(List.of(f));
        assertSame(f, FlightCollection.getFlightInfo("bRiSbAnE"));
    }

    // Adding multiple unique flights
    @Test
    void addFlights_multipleUnique_increasesSize() {
        Flight a = mock(Flight.class), b = mock(Flight.class);
        when(a.getCode()).thenReturn("A"); when(a.getDateFrom()).thenReturn(Timestamp.valueOf("2025-01-01 00:00:00"));
        when(b.getCode()).thenReturn("B"); when(b.getDateFrom()).thenReturn(Timestamp.valueOf("2025-01-02 00:00:00"));
        FlightCollection.clearFlights();
        FlightCollection.addFlights(List.of(a, b));
        assertEquals(2, FlightCollection.getFlights().size());
    }

    // Duplicate in the same batch
    @Test
    void addFlights_batchWithDuplicate_throws() {
        Flight dup = mock(Flight.class);
        when(dup.getCode()).thenReturn("DUP");
        when(dup.getDateFrom()).thenReturn(Timestamp.valueOf("2025-01-01 00:00:00"));

        List<Flight> batch = List.of(dup, dup);
        FlightCollection.clearFlights();
        assertThrows(IllegalArgumentException.class, () -> FlightCollection.addFlights(batch));
    }

    // getFlights() accessor
    @Test
    void getFlightsReflectsUnderlyingList() {
        FlightCollection.clearFlights();
        Flight f = mock(Flight.class);
        when(f.getCode()).thenReturn("C"); when(f.getDateFrom()).thenReturn(Timestamp.valueOf("2025-01-01 00:00:00"));
        List<Flight> holder = FlightCollection.getFlights();
        holder.add(f);
        assertEquals(1, FlightCollection.getFlights().size());
    }

}

