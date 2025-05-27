package com.ars.unit;

public class TicketSystem {

    public void buyTicket(int ticket_id, Passenger passenger) {
        Ticket validTicket = TicketCollection.getTicketInfo(ticket_id);

        if (passenger.getEmail() == null) {
            System.out.println("Invalid passenger info.");
            return;
        }


        if (validTicket == null) {
            System.out.println("This ticket does not exist.");
            return;
        }

        if (validTicket.ticketStatus()) {
            System.out.println("Ticket is already booked.");
            return;
        }

        int flight_id = validTicket.getFlight().getFlightID();
        Flight flight = FlightCollection.getFlightInfo(flight_id);
        if (flight == null) {
            System.out.println("No flight associated with this ticket.");
            return;
        }

        int airplane_id = flight.getAirplane().getAirplaneID();

        Airplane airplane = Airplane.getAirPlaneInfo(airplane_id);

        if (airplane == null) {
            System.out.println("No airplane associated with this ticket.");
            return;
        }

        validTicket.setPassenger(passenger);
        validTicket.setTicketStatus(true);

        if (validTicket.getClassVip()) {
            airplane.setBusinessSitsNumber(airplane.getBusinessSitsNumber() - 1);
        } else {
            airplane.setEconomySitsNumber(airplane.getEconomySitsNumber() - 1);
        }

        System.out.println("Your bill: " + validTicket.getPrice());
    }

    public void chooseTicket(String city1, String city2, Passenger passenger) {
        Flight flight = FlightCollection.getFlightInfo(city1, city2);

        if (flight != null) {
            // If there is a direct flight, find an unbooked ticket on a direct flight
            Ticket ticket = findAvailableTicketForFlight(flight.getFlightID());
            if (ticket != null) {
                buyTicket(ticket.getTicket_id(), passenger);
            } else {
                System.out.println("No available tickets for direct flight.");
            }
        } else {
            int counter = 0;

            // No direct flight, find a connecting flight
            Flight depart_to = FlightCollection.getFlightInfo(city2);

            if (depart_to != null) {
                String connectCity = depart_to.getDepartFrom();

                Flight flightConnectingTwoCities = FlightCollection.getFlightInfo(city1, connectCity);

                if (flightConnectingTwoCities != null) {
                    System.out.println("There is special way to go there. And it is transfer way, like above. Way №" + counter);

                    Ticket firstTicket = findAvailableTicketForFlight(depart_to.getFlightID());
                    Ticket secondTicket = findAvailableTicketForFlight(flightConnectingTwoCities.getFlightID());

                    if (firstTicket != null && secondTicket != null) {
                        buyTicket(firstTicket.getTicket_id(), passenger);
                        buyTicket(secondTicket.getTicket_id(), passenger);
                    } else {
                        System.out.println("No available tickets for transfer flights.");
                    }

                    counter++;
                }
            }

            if (counter == 0) {
                System.out.println("There is no possible variants.");
            }
        }
    }

    public Ticket findAvailableTicketForFlight(int flightId) {
        for (Ticket t : TicketCollection.getTickets()) {
            if (t.getFlight().getFlightID() == flightId && !t.ticketStatus()) {
                return t;
            }
        }
        return null;
    }


}
