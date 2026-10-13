package TicketPriorizationSystem;

import java.util.HashMap;
import java.util.Map;

public class TicketRepository {
    private final Map<Integer, Ticket> tickets = new HashMap<>();
    

    public Map<Integer, Ticket> allTickets() {
        Map<Integer, Ticket> ticketsCopy = new HashMap<>(tickets);
        return ticketsCopy;
  }

  public void addTicket(Ticket ticket) {
    if (!tickets.containsKey(ticket.getId())) {
        tickets.put(ticket.getId(), ticket);
    }
  }

  public Ticket findTicket(int id) {
        if (tickets.get(id) != null) {
            return tickets.get(id);
        }
    return null;
  }

  public boolean updateSeverity(int id, SeverityLevel severity) {
        if (tickets.get(id) != null) {
            tickets.get(id).setSeverity(severity);
            return true;
     }
     return false;
  }
}