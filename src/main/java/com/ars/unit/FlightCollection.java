package com.ars.unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlightCollection {

	public static List<Flight> flights = new ArrayList<>();


	private FlightCollection() {}

	public static List<Flight> getFlights()
	{
		return flights;
	}

	public static void addFlights(List<Flight> newFlights) {
		Set<String> existingFlightKeys = new HashSet<>();
		for (Flight flight : flights) {
			existingFlightKeys.add(flight.getCode() + "_" + flight.getDateFrom());
		}

		for (Flight flight : newFlights) {
			String flightKey = flight.getCode() + "_" + flight.getDateFrom();
			if (!existingFlightKeys.contains(flightKey)) {
				flights.add(flight);
				existingFlightKeys.add(flightKey);
			} else {
				throw new IllegalArgumentException("Duplicate flight skipped: " + flight.getCode());
			}
		}
	}

	public static Flight getFlightInfo(String city1, String city2) {
		if (city1 == null || city2 == null || city1.isBlank() || city2.isBlank()) return null;
		for (Flight flight : flights) {
			if (flight.getDepartFrom().equalsIgnoreCase(city1) &&
					flight.getDepartTo().equalsIgnoreCase(city2)) {
				return flight;
			}
		}
		return null;
	}


	public static Flight getFlightInfo(String city) {
		if (city == null || city.isBlank()) return null;
		for (Flight flight : flights) {
			if (flight.getDepartTo().equalsIgnoreCase(city)) {
				return flight;
			}
		}
		return null;
	}

	public static Flight getFlightInfo(int flightID) {
		for (Flight flight : flights) {
			if (flight.getFlightID() == flightID) {
				return flight;
			}
		}
		return null;
	}

	public static void clearFlights() {
		flights.clear();
	}
}
