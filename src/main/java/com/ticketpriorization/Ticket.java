package com.ticketpriorization;

import java.time.Instant;
import java.util.Objects;

public class Ticket {
    private final int id;
    private String title;
    private String description;
    private SeverityLevel severity;
    private final Instant timestamp;
    private Status status;

    public Ticket(int id, String title, String description, SeverityLevel severity, Instant timestamp) {

        if (id <= 0) {
            throw new IllegalArgumentException("Ticket ID must be positive.");
        }

        this.id = id;
        this.title = validateText(title, "Title");
        this.description = validateText(description, "Description");
        this.severity = Objects.requireNonNull(
                severity, "Severity cannot be null."
        );
        this.timestamp = Objects.requireNonNull(
                timestamp, "Timestamp cannot be null."
        );

        this.status = Status.OPEN;
    }

    public Ticket(Ticket ticket) {
        this(requireTicket(ticket).getId(), requireTicket(ticket).getTitle(), requireTicket(ticket).getDescription(), requireTicket(ticket).getSeverity(), requireTicket(ticket).getTimestamp());
        this.status = requireTicket(ticket).getStatus();
    }

    public static Ticket requireTicket(Ticket ticket) {
        return Objects.requireNonNull(ticket);
    }

    private String validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank.");
        }
        return value;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = validateText(title, "Title");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = validateText(description, "Description");
    }

    public SeverityLevel getSeverity() {
        return severity;
    }

    public void setSeverity(SeverityLevel severity) {
        this.severity = Objects.requireNonNull(
                severity, "Severity cannot be null."
        );
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override 
    public String toString() {
        return "ID: " + id +
                " | Title: " + title +
                " | Description: " + description +
                " | Severity: " + severity +
                " | Timestamp: " + timestamp + "\n";
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
