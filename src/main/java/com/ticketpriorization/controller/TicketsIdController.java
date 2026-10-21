package com.ticketpriorization.controller;

import com.ticketpriorization.CreateTicketRequest;
import com.ticketpriorization.Ticket;
import com.ticketpriorization.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/api/tickets/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable("id") int id, @RequestBody CreateTicketRequest request) {
        Ticket ticket = ticketService.findTicket(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        boolean ticketUpdateStatus = ticketService.updateTicket(request.getTitle(), request.getDescription(), request.getSeverityLevel(), id);
        if (ticketUpdateStatus) {
            return ResponseEntity.ok(ticket);
        }
        return ResponseEntity.badRequest().build();
    }
}
