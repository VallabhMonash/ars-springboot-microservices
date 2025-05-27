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
        int assigned = 0;

        for (char row = 'A'; row <= 'J'; row++) {
            boolean flag = false;
            for (int seat = 1; seat <= 7; seat++) {
                String key = row + String.valueOf(seat);
                if (assigned < businessSitsNumber) {
                    assignments.put(key, "business");
                } else if (assigned < businessSitsNumber + economySitsNumber) {
                    assignments.put(key, "economy");
                } else if (assigned < totalSits) {
                    assignments.put(key, "crew");
                } else {
                    flag = true;
                    break;
                }
                if (flag)
                    break;
                assigned++;
            }
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

    public int getBusinessSitsNumber() {
        return businessSitsNumber;
    }

    public void setBusinessSitsNumber(int businessSitsNumber) {
        if (businessSitsNumber < 0 || economySitsNumber <= 0 || crewSitsNumber <= 0)
            throw new IllegalArgumentException("Seat counts must be non-negative.");
        this.businessSitsNumber = businessSitsNumber;
        this.seatAssignments = initializeSeatAssignments();
    }

    public int getEconomySitsNumber() {
        return economySitsNumber;
    }

    public void setEconomySitsNumber(int economSitsNumber) {
        if (economySitsNumber <= 0)
            throw new IllegalArgumentException("Seat counts must be non-negative.");
        this.economySitsNumber = economSitsNumber;
        this.seatAssignments = initializeSeatAssignments();
    }

    public int getCrewSitsNumber() {
        return crewSitsNumber;
    }

    public void setCrewSitsNumber(int crewSitsNumber) {
        if (businessSitsNumber < 0 || economySitsNumber <= 0 || crewSitsNumber <= 0)
            throw new IllegalArgumentException("Seat counts must be non-negative.");
        this.crewSitsNumber = crewSitsNumber;
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