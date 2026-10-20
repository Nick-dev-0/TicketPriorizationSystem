package com.ticketpriorization;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TicketRepository {
    private final Map<Integer, Ticket> tickets = new HashMap<>();
    
    public Map<Integer, Ticket> allTickets() {
        Map<Integer, Ticket> fullTicketCopy = new HashMap<>(tickets);
        
        for (Map.Entry<Integer, Ticket> tickets : fullTicketCopy.entrySet()) {
            fullTicketCopy.put(tickets.getKey(), new Ticket(tickets.getValue()));
        }
        return fullTicketCopy;
  }

  public boolean addTicket(Ticket ticket) {
    if (!tickets.containsKey(ticket.getId())) {
        tickets.put(ticket.getId(), ticket);
        return true;
    }
    return false;
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