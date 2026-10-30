package com.ticketpriorization.service;

import com.ticketpriorization.dto.CreateTicketRequest;
import com.ticketpriorization.model.SeverityLevel;
import com.ticketpriorization.model.Status;
import com.ticketpriorization.model.Ticket;
import com.ticketpriorization.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final boolean demoMode;

    public TicketService(TicketRepository ticketRepository, @Value("${app.demo-mode:false}") boolean demoMode) {
        this.ticketRepository = ticketRepository;
        this.demoMode = demoMode;
    }
    
    public List<Ticket> ticketSeveritySorted() {
        List<Ticket> sortedbySeverity = new ArrayList<>(ticketRepository.findAll());

        sortedbySeverity.sort(
                Comparator.comparing(Ticket::getSeverity)
                        .thenComparing(Ticket::getTimestamp));

        return sortedbySeverity;
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }


    public Ticket createTicket(CreateTicketRequest ticketRequest) {

        Instant createdAt = Instant.now();

        Ticket builtTicket = new Ticket(ticketRequest.getTitle(), ticketRequest.getDescription(), ticketRequest.getSeverityLevel(), createdAt);

        if (demoMode) {
            builtTicket.setExpiresAt(createdAt.plus(Duration.ofHours(24)));
        }
        return saveTicket(builtTicket);
    }

    public Optional<Ticket> findTicket(String id) {
        return ticketRepository.findById(id);
    }

    public Optional<Ticket> updateTicket(String title, String description, SeverityLevel severity, String id, Status status) {
        Optional<Ticket> existingTicketResult = findTicket(id);

        if(existingTicketResult.isPresent()) {

            Ticket existingTicket = existingTicketResult.get();
            existingTicket.setTitle(title);
            existingTicket.setDescription(description);
            existingTicket.setSeverity(severity);
            existingTicket.setStatus(status);

            Ticket savedTicket = ticketRepository.save(existingTicket);

            return Optional.of(savedTicket);
        }
        return Optional.empty();

    }
}
