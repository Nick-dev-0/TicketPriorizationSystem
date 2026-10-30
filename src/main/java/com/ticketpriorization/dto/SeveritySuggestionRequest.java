package com.ticketpriorization.dto;

import jakarta.validation.constraints.NotBlank;

public class SeveritySuggestionRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;

    public SeveritySuggestionRequest() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}