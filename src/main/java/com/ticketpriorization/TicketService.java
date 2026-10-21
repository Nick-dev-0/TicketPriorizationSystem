package com.ticketpriorization;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    int id = 0;
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    
    public List<Ticket> ticketSeveritySorted() {
        Map<Integer, Ticket> allTickets = this.ticketRepository.allTickets();
        List<Ticket> sortedBySeverity = new ArrayList<>(allTickets.values());
        sortedBySeverity.sort(Comparator.comparing(Ticket::getSeverity).thenComparing(Ticket::getTimestamp));
        
        return sortedBySeverity;
    }

    public boolean addTicket(Ticket ticket) {
        boolean addTicketStatus = ticketRepository.addTicket(ticket);
        if (addTicketStatus) {
            this.id += 1;
        }
        return addTicketStatus;
    }


    public Ticket createTicket(CreateTicketRequest ticketRequest) {
        Ticket builtTicket = new Ticket(id + 1, ticketRequest.getTitle(), ticketRequest.getDescription(), ticketRequest.getSeverityLevel(), Instant.now());
        if (addTicket(builtTicket)) {
            return builtTicket;
        }
        return null;
    }

    public Ticket findTicket(int id) {
        return ticketRepository.findTicket(id);
    }

    public boolean updateSeverity(int id, SeverityLevel severity) {
        return ticketRepository.updateSeverity(id, severity);
    }

    public boolean updateTicket(String title, String description, SeverityLevel severity, int id) {
        return ticketRepository.updateTicket(title, description, severity, id);
    }
}
