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
        // 1) Try direct flight
        Flight direct = FlightCollection.getFlightInfo(city1, city2);
        if (direct != null) {
            Ticket ticket = findAvailableTicketForFlight(direct.getFlightID());
            if (ticket != null) {
                buyTicket(ticket.getTicketId(), passenger);
            } else {
                logger.log(Level.INFO, "No available tickets for direct flight.");
            }
            return;
        }

        // 2) Try connecting flight via an intermediate city
        Flight secondLeg = FlightCollection.getFlightInfo(city2);
        if (secondLeg != null) {
            String transferCity = secondLeg.getDepartFrom();
            Flight firstLeg = FlightCollection.getFlightInfo(city1, transferCity);
            if (firstLeg != null) {
                Ticket t1 = findAvailableTicketForFlight(firstLeg.getFlightID());
                Ticket t2 = findAvailableTicketForFlight(secondLeg.getFlightID());
                if (t1 != null && t2 != null) {
                    buyTicket(t1.getTicketId(), passenger);
                    buyTicket(t2.getTicketId(), passenger);
                } else {
                    logger.log(Level.INFO, "No available tickets for transfer flights.");
                }
                return;
            }
        }

        // 3) No option found
        logger.log(Level.WARNING, "There is no possible variants.");
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
