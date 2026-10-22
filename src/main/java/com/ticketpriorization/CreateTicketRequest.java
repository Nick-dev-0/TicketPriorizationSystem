package com.ticketpriorization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateTicketRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
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
