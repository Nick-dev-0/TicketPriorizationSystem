package com.ticketpriorization.controller;

import com.ticketpriorization.dto.SeveritySuggestionRequest;
import com.ticketpriorization.dto.SeveritySuggestionResponse;
import com.ticketpriorization.service.SeveritySuggestionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/severity-suggestions")
public class SeveritySuggestionController {

    private final SeveritySuggestionService severitySuggestionService;

    public SeveritySuggestionController(SeveritySuggestionService severitySuggestionService) {
        this.severitySuggestionService = severitySuggestionService;
    }

    @PostMapping
    public SeveritySuggestionResponse suggestSeverity(@Valid @RequestBody SeveritySuggestionRequest request) {

        return severitySuggestionService.suggestSeverity(request);
    }
}