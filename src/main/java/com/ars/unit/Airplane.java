package com.ars.unit;

import java.util.HashMap;
import java.util.Map;

public class Airplane {
    private int airplaneID;
    private String airplaneModel;
    private int businessSitsNumber;
    private int economySitsNumber;
    private int crewSitsNumber;

    private Map<String, String> seatAssignments; // key: row+seatNumber (e.g., A1), value: type (business/economy/crew)

    public Airplane(int airplaneID, String airplaneModel, int businessSitsNumber, int economySitsNumber,
                    int crewSitsNumber) {
        if (airplaneID <= 0)
            throw new IllegalArgumentException("Airplane ID must be positive.");
        if (businessSitsNumber < 0 || economySitsNumber <= 0 || crewSitsNumber <= 0)
            throw new IllegalArgumentException("Seat counts must be non-negative.");
        if (airplaneModel == null || airplaneModel.isEmpty())
            throw new IllegalArgumentException("Model cannot be null or empty.");

        int totalSits = businessSitsNumber + economySitsNumber + crewSitsNumber;
        int maxSeats  = ('J' - 'A' + 1) * 7;  // 10 rows × 7 seats = 70

        if (totalSits != maxSeats) {
            throw new IllegalArgumentException(
                    "Total seats must be exactly " + maxSeats +
                            " (10 rows A–J × 7 seats each); got " + totalSits
            );
        }

        this.airplaneID = airplaneID;
        this.airplaneModel = airplaneModel;
        this.businessSitsNumber = businessSitsNumber;
        this.economySitsNumber = economySitsNumber;
        this.crewSitsNumber = crewSitsNumber;
        this.seatAssignments = initializeSeatAssignments();
    }

    private Map<String, String> initializeSeatAssignments() {
        Map<String, String> assignments = new HashMap<>();
        int totalSits = businessSitsNumber + economySitsNumber + crewSitsNumber;

        // Loop exactly totalSits times, mapping each index to a row/seat
        for (int i = 0; i < totalSits; i++)
        {
            // Row letter: 0–6 → 'A', 7–13 → 'B', etc.
            char row = (char) ('A' + (i / 7));
            // Seat number within row: 0→1, 1→2, …, 6→7
            int seatNo = (i % 7) + 1;
            String key = "" + row + seatNo;

            // Determine class type by threshold
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

    public void setBusinessSitsNumber(int businessSitsNumber)
    {
        if (businessSitsNumber < 0 || economySitsNumber <= 0 || crewSitsNumber <= 0)
            throw new IllegalArgumentException("Seat counts must be non-negative.");
        this.businessSitsNumber = businessSitsNumber;
        this.seatAssignments = initializeSeatAssignments();
    }

    public int getEconomySitsNumber()
    {
        return economySitsNumber;
    }

    public void setEconomySitsNumber(int economySitsNumber)
    {
        if (economySitsNumber <= 0)
            throw new IllegalArgumentException("Seat counts must be non-negative.");
        this.economySitsNumber = economySitsNumber;
        this.seatAssignments = initializeSeatAssignments();
    }

    public int getCrewSitsNumber()
    {
        return crewSitsNumber;
    }

    public void setCrewSitsNumber(int crewSitsNumber) {
        if (businessSitsNumber < 0 || economySitsNumber <= 0 || crewSitsNumber <= 0)
            throw new IllegalArgumentException("Seat counts must be non-negative.");
        this.crewSitsNumber = crewSitsNumber;
        this.seatAssignments = initializeSeatAssignments();
    }

    public Map<String, String> getSeatAssignments()
    {
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

    public static Airplane getAirPlaneInfo(int airplane_id) {
        for (Flight flight : FlightCollection.getFlights()) {
            Airplane airplane = flight.getAirplane();
            if (airplane != null && airplane.getAirplaneID() == airplane_id) {
                return airplane;
            }
        }
        return null;
    }

}