package com.ars.unit;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TicketSystem {

    private static final Logger logger = Logger.getLogger(TicketSystem.class.getName());

    public void buyTicket(int ticketID, Passenger passenger) {
        Ticket validTicket = TicketCollection.getTicketInfo(ticketID);

        if (passenger.getEmail() == null) {
            logger.log(Level.WARNING, "Invalid passenger info: email is null");
            return;
        }

        if (validTicket == null) {
            logger.log(Level.WARNING, "This ticket does not exist.");
            return;
        }


        if (validTicket.ticketStatus()) {
            logger.log(Level.WARNING, "Ticket is already booked.");
            return;
        }


        int flightID = validTicket.getFlight().getFlightID();
        Flight flight = FlightCollection.getFlightInfo(flightID);

        if (flight == null) {
            logger.log(Level.WARNING, "No flight associated with this ticket.");
            return;
        }

        int airplaneID = flight.getAirplane().getAirplaneID();

        Airplane airplane = Airplane.getAirPlaneInfo(airplaneID);

        if (airplane == null) {
            logger.log(Level.WARNING, "No airplane associated with this ticket.");
            return;
        }

        validTicket.setPassenger(passenger);
        validTicket.setTicketStatus(true);

        if (validTicket.isClassVip()) {
            airplane.setBusinessSitsNumber(airplane.getBusinessSitsNumber() - 1);
        } else {
            airplane.setEconomySitsNumber(airplane.getEconomySitsNumber() - 1);
        }

        logger.log(Level.INFO, "Your bill: {0}", validTicket.getPrice());
    }

    public void chooseTicket(String city1, String city2, Passenger passenger) {
        Flight flight = FlightCollection.getFlightInfo(city1, city2);

        if (flight != null) {
            // If there is a direct flight, find an unbooked ticket on a direct flight
            Ticket ticket = findAvailableTicketForFlight(flight.getFlightID());
            if (ticket != null) {
                buyTicket(ticket.getTicketId(), passenger);
            } else {
                logger.log(Level.INFO, "No available tickets for direct flight.");
            }
        } else {
            int counter = 0;

            // No direct flight, find a connecting flight
            Flight departTo = FlightCollection.getFlightInfo(city2);

            if (departTo != null) {
                String connectCity = departTo.getDepartFrom();

                Flight flightConnectingTwoCities = FlightCollection.getFlightInfo(city1, connectCity);

                if (flightConnectingTwoCities != null) {
                    System.out.println("There is special way to go there. And it is transfer way, like above. Way №" + counter);

                    Ticket firstTicket = findAvailableTicketForFlight(departTo.getFlightID());
                    Ticket secondTicket = findAvailableTicketForFlight(flightConnectingTwoCities.getFlightID());

                    if (firstTicket != null && secondTicket != null) {
                        buyTicket(firstTicket.getTicketId(), passenger);
                        buyTicket(secondTicket.getTicketId(), passenger);
                    } else {
                        System.out.println("No available tickets for transfer flights.");
                    }

                    counter++;
                }
            }

            if (counter == 0) {
                logger.log(Level.WARNING, "There is no possible variants.");
                return;
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
