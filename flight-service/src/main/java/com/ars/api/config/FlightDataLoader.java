package com.ars.api.config;

import com.ars.unit.Airplane;
import com.ars.unit.Flight;
import com.ars.unit.FlightCollection;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FlightDataLoader {

    @Bean
    CommandLineRunner loadFlights() {
        return args -> {
            // Avoid duplicating data if app restarts
            if (!FlightCollection.getFlights().isEmpty()) {
                return;
            }

            Airplane airplane = new Airplane(1, "Boeing 787", 10, 50, 10);

            Timestamp d1From = Timestamp.valueOf("2026-03-01 10:00:00");
            Timestamp d1To   = Timestamp.valueOf("2026-03-01 14:00:00");
            Flight.FlightSchedule s1 = new Flight.FlightSchedule(d1From, d1To);
            Flight f1 = new Flight(1, "Tokyo", "Sydney", "JL123", "Japan Airlines", s1, airplane);

            Timestamp d2From = Timestamp.valueOf("2026-03-02 09:00:00");
            Timestamp d2To   = Timestamp.valueOf("2026-03-02 12:30:00");
            Flight.FlightSchedule s2 = new Flight.FlightSchedule(d2From, d2To);
            Flight f2 = new Flight(2, "Melbourne", "Sydney", "QF456", "Qantas", s2, airplane);

            List<Flight> flights = new ArrayList<>();
            flights.add(f1);
            flights.add(f2);

            FlightCollection.addFlights(flights);
        };
    }
}

