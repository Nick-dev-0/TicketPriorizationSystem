package com.ticketpriorization;

public class CreateTicketRequest {
    private String title;
    private String description;
    private SeverityLevel severity;

    public CreateTicketRequest() {

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public SeverityLevel getSeverityLevel() {
        return severity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeverity(SeverityLevel severity) {
        this.severity = severity;
    }
}
