package com.ticketpriorization;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
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
        Ticket builtTicket = new Ticket(ticketRequest.getTitle(), ticketRequest.getDescription(), ticketRequest.getSeverityLevel(), Instant.now());

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
