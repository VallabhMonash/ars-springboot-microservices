package com.ars.api.dto;

public class FlightResponse {
    private int flightId;
    private String departFrom;
    private String departTo;
    private String code;
    private String company;
    private String dateFrom;
    private String dateTo;
    private int airplaneId;

    public FlightResponse() {
    }

    public FlightResponse(int flightId, String departFrom, String departTo, String code,
            String company, String dateFrom, String dateTo, int airplaneId) {
        this.flightId = flightId;
        this.departFrom = departFrom;
        this.departTo = departTo;
        this.code = code;
        this.company = company;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.airplaneId = airplaneId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getDepartFrom() {
        return departFrom;
    }

    public void setDepartFrom(String departFrom) {
        this.departFrom = departFrom;
    }

    public String getDepartTo() {
        return departTo;
    }

    public void setDepartTo(String departTo) {
        this.departTo = departTo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public int getAirplaneId() {
        return airplaneId;
    }

    public void setAirplaneId(int airplaneId) {
        this.airplaneId = airplaneId;
    }
}
