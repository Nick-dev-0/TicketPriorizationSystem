package TicketPriorizationSystem;

import java.util.List;

public class TicketRepository {
    List<Ticket> tickets;
    
    public TicketRepository(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Ticket> allTickets() {
        return this.tickets;
  }
  
}
