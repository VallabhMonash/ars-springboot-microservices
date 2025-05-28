package com.ars.unit;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Flight {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private int flightID;
    private String departTo;
    private String departFrom;
    private String code;
    private String company;
    private Timestamp dateFrom;
    private Timestamp dateTo;
    private Airplane airplane;

    public Flight() { }

    /**
     * Grouped schedule parameters into a FlightSchedule object to reduce constructor parameters.
     */
    public Flight(int flightID,
                  String departTo,
                  String departFrom,
                  String code,
                  String company,
                  FlightSchedule schedule,
                  Airplane airplane) {
        // Validate simple fields
        if (flightID <= 0)
            throw new IllegalArgumentException("Flight ID must be positive");
        if (departTo == null || departTo.isEmpty())
            throw new IllegalArgumentException("DepartTo is required");
        if (departFrom == null || departFrom.isEmpty())
            throw new IllegalArgumentException("DepartFrom is required");
        if (code == null || code.isEmpty())
            throw new IllegalArgumentException("Code is required");
        if (company == null || company.isEmpty())
            throw new IllegalArgumentException("Company is required");
        if (schedule == null)
            throw new IllegalArgumentException("Schedule is required");
        if (airplane == null)
            throw new IllegalArgumentException("Airplane is required");

        // Assign
        this.flightID   = flightID;
        this.departTo   = departTo;
        this.departFrom = departFrom;
        this.code       = code;
        this.company    = company;
        this.dateFrom   = schedule.getFrom();
        this.dateTo     = schedule.getTo();
        this.airplane   = airplane;
    }

    /**
     * Helper value object to group departure/arrival timestamps
     */
    public static class FlightSchedule {
        private final Timestamp from;
        private final Timestamp to;

        public FlightSchedule(Timestamp from, Timestamp to) {
            if (from == null || to == null)
                throw new IllegalArgumentException("Both dates are required");
            if (!from.before(to))
                throw new IllegalArgumentException("dateFrom must be before dateTo");
            this.from = from;
            this.to   = to;
        }

        public Timestamp getFrom() {
            return from;
        }

        public Timestamp getTo() {
            return to;
        }
    }

    // Getters and setters
    public int getFlightID() { return flightID; }
    public void setFlightID(int flightID) { this.flightID = flightID; }

    public String getDepartTo() { return departTo; }
    public void setDepartTo(String departTo) { this.departTo = departTo; }

    public String getDepartFrom() { return departFrom; }
    public void setDepartFrom(String departFrom) { this.departFrom = departFrom; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public Timestamp getDateFrom() { return dateFrom; }
    public void setDateFrom(Timestamp dateFrom) { this.dateFrom = dateFrom; }

    public Timestamp getDateTo() { return dateTo; }
    public void setDateTo(Timestamp dateTo) { this.dateTo = dateTo; }

    public Airplane getAirplane() { return airplane; }
    public void setAirplane(Airplane airplane) { this.airplane = airplane; }

    @Override
    public String toString() {
        return "Flight{" + airplane.toString() +
                ", dateFrom=" + dateFrom +
                ", dateTo="   + dateTo +
                ", departFrom='" + departFrom + '\'' +
                ", departTo='"   + departTo   + '\'' +
                ", code='"       + code       + '\'' +
                ", company='"    + company    + '\'' +
                '}';
    }

    // Existing parseTimestamp can be left here if used elsewhere
    private static Timestamp parseTimestamp(String dateStr, String timeStr, String fieldName) {
        try {
            LocalDate d = LocalDate.parse(dateStr, DATE_FMT);
            LocalTime t = LocalTime.parse(timeStr, TIME_FMT);
            return Timestamp.valueOf(LocalDateTime.of(d, t));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    fieldName + " must be in formats YYYY-MM-DD and HH:MM:SS; got '"
                            + dateStr + " " + timeStr + "'", ex
            );
        }
    }
}