package com.ars.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketSystemTest {

    @Mock
    Ticket mockTicket;

    @Mock
    Flight mockFlight;

    @Mock
    Airplane mockAirplane;

    @Mock
    Passenger mockPassenger;

    TicketSystem ticketSystem;

    @BeforeEach
    void setup() {
        ticketSystem = new TicketSystem();
    }

    @Test // valid city, available ticket, and complete info
    void testBuyTicketSuccess() {
        try (MockedStatic<TicketCollection> mockedTickets = mockStatic(TicketCollection.class);
             MockedStatic<FlightCollection> mockedFlights = mockStatic(FlightCollection.class);
             MockedStatic<Airplane> mockedAirplanes = mockStatic(Airplane.class)) {

            // TicketCollection.getTicketInfo(ticket_id) -> mockTicket
            mockedTickets.when(() -> TicketCollection.getTicketInfo(1)).thenReturn(mockTicket);

            // FlightCollection.getFlightInfo(flight_id) -> mockFlight
            mockedFlights.when(() -> FlightCollection.getFlightInfo(100)).thenReturn(mockFlight);

            // Airplane.getAirPlaneInfo(airplane_id) -> mockAirplane
            mockedAirplanes.when(() -> Airplane.getAirPlaneInfo(200)).thenReturn(mockAirplane);

            // Passenger
            when(mockPassenger.getEmail()).thenReturn("abc@example.com");

            // Ticket
            when(mockTicket.ticketStatus()).thenReturn(false);
            when(mockTicket.getFlight()).thenReturn(mockFlight);

            // Flight
            when(mockFlight.getFlightID()).thenReturn(100);
            when(mockFlight.getAirplane()).thenReturn(mockAirplane);

            // Airplane
            when(mockAirplane.getAirplaneID()).thenReturn(200); // mock airplane_id

            // Ticket
            when(mockTicket.getClassVip()).thenReturn(false);
            when(mockTicket.getPrice()).thenReturn(1000);

            ticketSystem.buyTicket(1, mockPassenger);

            verify(mockTicket).setPassenger(mockPassenger);
            verify(mockTicket).setTicketStatus(true);
            verify(mockAirplane).setEconomySitsNumber(anyInt());
        }
    }


    @Test // ticket not found
    void testBuyTicketNotFound() {
        try (MockedStatic<TicketCollection> mockedTickets = mockStatic(TicketCollection.class)) {
            mockedTickets.when(() -> TicketCollection.getTicketInfo(2)).thenReturn(null);
            when(mockPassenger.getEmail()).thenReturn("abc@example.com");
            ticketSystem.buyTicket(2, mockPassenger);

            verifyNoInteractions(mockTicket);
        }
    }

    @Test // ticket already booked
    void testBuyTicketAlreadyBooked() {
        try (MockedStatic<TicketCollection> mockedTickets = mockStatic(TicketCollection.class)) {
            mockedTickets.when(() -> TicketCollection.getTicketInfo(3)).thenReturn(mockTicket);
            when(mockPassenger.getEmail()).thenReturn("abc@example.com");
            when(mockTicket.ticketStatus()).thenReturn(true);
            ticketSystem.buyTicket(3, mockPassenger);

            verify(mockTicket, never()).setPassenger(any());
            verify(mockTicket, never()).setTicketStatus(anyBoolean());
            verify(mockTicket, never()).getFlight();
        }
    }

    @Test // chooseTicket with invalid city fallback also fails
    void testChooseTicketWithInvalidCities() {
        try (MockedStatic<FlightCollection> mockedFlights = mockStatic(FlightCollection.class)) {
            mockedFlights.when(() -> FlightCollection.getFlightInfo("A", "B")).thenReturn(null);
            mockedFlights.when(() -> FlightCollection.getFlightInfo("B")).thenReturn(null);
            ticketSystem.chooseTicket("A", "B", mockPassenger);

            verifyNoInteractions(mockPassenger);

        }
    }

    @Test // fallback to transfer route works correctly
    public void testChooseTicket_transferSuccess() {
        // mock Passenger
        Passenger passenger = mock(Passenger.class);

        // mock two flight
        Flight departTo = mock(Flight.class);
        when(departTo.getFlightID()).thenReturn(100);
        when(departTo.getDepartFrom()).thenReturn("CONNECT_CITY");

        Flight connectFlight = mock(Flight.class);
        when(connectFlight.getFlightID()).thenReturn(200);

        // mock two tickets
        Ticket t1 = mock(Ticket.class);
        when(t1.getFlight()).thenReturn(departTo);
        when(t1.ticketStatus()).thenReturn(false);
        when(t1.getTicket_id()).thenReturn(1000);

        Ticket t2 = mock(Ticket.class);
        when(t2.getFlight()).thenReturn(connectFlight);
        when(t2.ticketStatus()).thenReturn(false);
        when(t2.getTicket_id()).thenReturn(2000);

        // Ensuring type matching with ArrayList
        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets.add(t1);
        tickets.add(t2);

        // Static Mock FlightCollection and TicketCollection
        try (MockedStatic<FlightCollection> flightMock = mockStatic(FlightCollection.class);
             MockedStatic<TicketCollection> ticketMock = mockStatic(TicketCollection.class)) {

            // No direct flights
            flightMock.when(() -> FlightCollection.getFlightInfo("CityA", "CityB"))
                    .thenReturn(null);
            // First Trip：CityB → CONNECT_CITY
            flightMock.when(() -> FlightCollection.getFlightInfo("CityB"))
                    .thenReturn(departTo);
            // second Trip：CityA → CONNECT_CITY
            flightMock.when(() -> FlightCollection.getFlightInfo("CityA", "CONNECT_CITY"))
                    .thenReturn(connectFlight);

            // Control getTickets() to return the list prepared
            ticketMock.when(TicketCollection::getTickets)
                    .thenReturn(tickets);

            //  Spy the object under test and stub buyTicket to avoid executing internal logic
            TicketSystem spySystem = spy(ticketSystem);
            doNothing().when(spySystem).buyTicket(anyInt(), any(Passenger.class));

            // implement chooseTicket
            spySystem.chooseTicket("CityA", "CityB", passenger);

            // verify buyTicket used
            verify(spySystem, times(1)).buyTicket(1000, passenger);
            verify(spySystem, times(1)).buyTicket(2000, passenger);
        }
    }



    @Test // TS3: passenger info is invalid
    void testBuyTicketFailsDueToInvalidPassengerInfo() {
        try (MockedStatic<TicketCollection> mockedTickets = mockStatic(TicketCollection.class)) {
            mockedTickets.when(() -> TicketCollection.getTicketInfo(1)).thenReturn(mockTicket);

            when(mockPassenger.getEmail()).thenReturn(null);

            ticketSystem.buyTicket(1, mockPassenger);

            verify(mockTicket, never()).setPassenger(any());
            verify(mockTicket, never()).setTicketStatus(anyBoolean());
        }
    }

}
