package com.ticketpriorization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketsIdController {

    @GetMapping("/api/tickets/{id}")
    public String ticketsById(@PathVariable("id") int id) {
        return "Ticket: " + id + " Placeholder";
    }
}
