package com.ars.unit;

import java.util.HashMap;
import java.util.Map;

public class Airplane {
    private int airplaneID;
    private String airplaneModel;
    private int businessSitsNumber;
    private int economySitsNumber;
    private int crewSitsNumber;

    private static final int MAX_SEATS = ( 'J' - 'A' + 1 ) * 7;

    private Map<String, String> seatAssignments; // key: row+seatNumber (e.g., A1), value: type (business/economy/crew)

    public Airplane(int airplaneID, String airplaneModel, int businessSitsNumber, int economySitsNumber,
                    int crewSitsNumber) {
        if (airplaneID <= 0)
            throw new IllegalArgumentException("Airplane ID must be positive.");
        if (businessSitsNumber < 0 || economySitsNumber <= 0 || crewSitsNumber <= 0)
            throw new IllegalArgumentException("Seat counts must be non-negative.");
        if (airplaneModel == null || airplaneModel.isEmpty())
            throw new IllegalArgumentException("Model cannot be null or empty.");

        int total = businessSitsNumber + economySitsNumber + crewSitsNumber;
        final int MAX = 10 * 7;  // A–J × 7 seats
        if (total != MAX)
            throw new IllegalArgumentException(
                    "Total seats must be exactly " + MAX + "; got " + total
            );

        this.airplaneID = airplaneID;
        this.airplaneModel = airplaneModel;
        this.businessSitsNumber = businessSitsNumber;
        this.economySitsNumber = economySitsNumber;
        this.crewSitsNumber = crewSitsNumber;
        this.seatAssignments = initializeSeatAssignments();
    }

    private Map<String,String> initializeSeatAssignments() {
        Map<String,String> assignments = new HashMap<>();
        int totalSits = businessSitsNumber + economySitsNumber + crewSitsNumber;
        // exactly totalSits iterations, no breaks
        for (int i = 0; i < totalSits; i++) {
            char row    = (char) ('A' + (i / 7));      // 0–6 → 'A', 7–13 → 'B'
            int  seatNo = (i % 7) + 1;                 // 0→1, …, 6→7
            String key  = "" + row + seatNo;

            String type;
            if (i < businessSitsNumber) {
                type = "business";
            } else if (i < businessSitsNumber + economySitsNumber) {
                type = "economy";
            } else {
                type = "crew";
            }
            assignments.put(key, type);
        }
        return assignments;
    }

    public int getAirplaneID() {
        return airplaneID;
    }

    public void setAirplaneID(int airplaneID) {
        if (airplaneID <= 0)
            throw new IllegalArgumentException("Airplane ID must be positive.");
        this.airplaneID = airplaneID;
    }

    public String getAirplaneModel() {
        return airplaneModel;
    }

    public void setAirplaneModel(String airplaneModel) {
        if (airplaneModel == null || airplaneModel.isEmpty())
            throw new IllegalArgumentException("Model cannot be null or empty.");
        this.airplaneModel = airplaneModel;
    }

    public int getBusinessSitsNumber()
    {
        return businessSitsNumber;
    }


    public void setBusinessSitsNumber(int businessSitsNumberParam) {
        if (businessSitsNumberParam < 0 || businessSitsNumberParam > MAX_SEATS) {
            throw new IllegalArgumentException("Business seat count must be non-negative.");
        }
        this.businessSitsNumber = businessSitsNumberParam;
        this.seatAssignments    = initializeSeatAssignments();
    }

    public int getEconomySitsNumber()
    {
        return economySitsNumber;
    }

    public void setEconomySitsNumber(int economySitsNumberParam) {
        if (economySitsNumberParam <= 0 || economySitsNumberParam > MAX_SEATS) {
            throw new IllegalArgumentException("Economy seat count must be positive.");
        }
        this.economySitsNumber = economySitsNumberParam;
        this.seatAssignments   = initializeSeatAssignments();
    }

    public int getCrewSitsNumber()
    {
        return crewSitsNumber;
    }

    public void setCrewSitsNumber(int crewSitsNumberParam) {
        if (crewSitsNumberParam <= 0 || crewSitsNumberParam > MAX_SEATS) {
            throw new IllegalArgumentException("Crew seat count must be positive.");
        }
        this.crewSitsNumber = crewSitsNumberParam;
        this.seatAssignments = initializeSeatAssignments();
    }

    public Map<String, String> getSeatAssignments() {
        return seatAssignments;
    }

    public String toString() {
        return "Airplane{" +
                "model=" + getAirplaneModel() + '\'' +
                ", business sits=" + getBusinessSitsNumber() + '\'' +
                ", economy sits=" + getEconomySitsNumber() + '\'' +
                ", crew sits=" + getCrewSitsNumber() + '\'' +
                '}';
    }

    public static Airplane getAirPlaneInfo(int airplaneID) {
        for (Flight flight : FlightCollection.getFlights()) {
            Airplane airplane = flight.getAirplane();
            if (airplane != null && airplane.getAirplaneID() == airplaneID) {
                return airplane;
            }
        }
        return null;
    }

}