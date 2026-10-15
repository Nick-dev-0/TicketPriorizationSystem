package com.ticketpriorization;

import java.time.Instant;
import java.util.Objects;

public class Ticket {
    private final int id;
    private String title;
    private String description;
    private SeverityLevel severity;
    private final Instant timestamp;

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
}
