package TicketPriorizationSystem;

import java.util.ArrayList;
import java.util.List;

public class TicketRepository {
    private final List<Ticket> tickets;
    
    public TicketRepository(List<Ticket> tickets) {
        this.tickets = new ArrayList<>(tickets);
    }

    public List<Ticket> allTickets() {
        List<Ticket> ticketsCopy = new ArrayList<>(tickets);
        return ticketsCopy;
  }

}
