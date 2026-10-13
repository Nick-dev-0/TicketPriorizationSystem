package TicketPriorizationSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    
    public List<Ticket> ticketSeveritySorted() {
        List<Ticket> allTickets = this.ticketRepository.allTickets();
        List<Ticket> sortedBySeverity = new ArrayList<>(allTickets);
        sortedBySeverity.sort(Comparator.comparing(Ticket::getSeverity).thenComparing(Ticket::getTimestamp));
        return sortedBySeverity;
    }
}
