package com.ticketpriorization.controller;

import com.ticketpriorization.CreateTicketRequest;
import com.ticketpriorization.Ticket;
import com.ticketpriorization.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TicketsIdController {
    private final TicketService ticketService;

    public TicketsIdController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    @GetMapping("/api/tickets/{id}")
    public ResponseEntity<Ticket> ticketsById(@PathVariable("id") String id) {
        Optional<Ticket> ticket = ticketService.findTicket(id);
        if (ticket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket.get());
    }

    @PatchMapping("/api/tickets/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable("id") String id, @Valid @RequestBody CreateTicketRequest request) {
        Optional<Ticket> updatedTicket = ticketService.updateTicket(request.getTitle(), request.getDescription(), request.getSeverityLevel(), id, request.getStatus());

        if (updatedTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTicket.get());

    }
}
