
package com.ticketpriorization.dto;

import com.ticketpriorization.model.SeverityLevel;

public class SeveritySuggestionResponse {

    private SeverityLevel suggestedSeverity;
    private String reasoning;

    public SeveritySuggestionResponse() {

    }

    public SeverityLevel getSuggestedSeverity() {
        return suggestedSeverity;
    }

    public void setSuggestedSeverity(SeverityLevel suggestedSeverity) {
        this.suggestedSeverity = suggestedSeverity;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}