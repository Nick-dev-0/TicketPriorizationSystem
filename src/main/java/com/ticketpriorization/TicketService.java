package com.ticketpriorization;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TicketService {
    private final TicketRepository ticketRepository;

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
        return ticketRepository.addTicket(ticket);
    }

    public Ticket findTicket(int id) {
        return ticketRepository.findTicket(id);
    }

    public boolean updateSeverity(int id, SeverityLevel severity) {
        return ticketRepository.updateSeverity(id, severity);
    }

}
