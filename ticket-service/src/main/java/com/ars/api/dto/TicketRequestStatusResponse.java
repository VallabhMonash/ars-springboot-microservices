package com.ars.api.dto;

public class TicketRequestStatusResponse {
    private String requestId;
    private String status;
    private String message;
    private TicketResponse ticket;

    public TicketRequestStatusResponse() {
    }

    public TicketRequestStatusResponse(String requestId, String status, String message, TicketResponse ticket) {
        this.requestId = requestId;
        this.status = status;
        this.message = message;
        this.ticket = ticket;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TicketResponse getTicket() {
        return ticket;
    }

    public void setTicket(TicketResponse ticket) {
        this.ticket = ticket;
    }
}
