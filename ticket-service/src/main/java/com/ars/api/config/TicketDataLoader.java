package com.ars.api.config;

import com.ars.unit.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TicketDataLoader {

    @Bean
    CommandLineRunner loadTickets() {
        return args -> {
            if (!TicketCollection.getTickets().isEmpty()) {
                return;
            }

            Airplane airplane = new Airplane(1, "Boeing 787", 10, 50, 10);

            Timestamp d1From = Timestamp.valueOf("2026-03-01 10:00:00");
            Timestamp d1To   = Timestamp.valueOf("2026-03-01 14:00:00");
            Flight.FlightSchedule s1 = new Flight.FlightSchedule(d1From, d1To);
            Flight f1 = new Flight(1, "Tokyo", "Sydney", "JL123", "Japan Airlines", s1, airplane);

            List<Flight> flights = new ArrayList<>();
            flights.add(f1);
            FlightCollection.addFlights(flights);

            Ticket t1 = new Ticket(101, 1000, f1, false, null);
            List<Ticket> tickets = new ArrayList<>();
            tickets.add(t1);
            TicketCollection.addTickets(tickets);
        };
    }
}

