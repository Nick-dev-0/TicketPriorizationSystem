package com.ticketpriorization.controller;

import com.ticketpriorization.Ticket;
import com.ticketpriorization.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketsIdController {
    private final TicketService ticketService;

    public TicketsIdController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    @GetMapping("/api/tickets/{id}")
    public ResponseEntity<Ticket> ticketsById(@PathVariable("id") int id) {
        Ticket ticket = ticketService.findTicket(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }
}
