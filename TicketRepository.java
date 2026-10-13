package TicketPriorizationSystem;

import java.util.ArrayList;
import java.util.List;

public class TicketRepository {
    private final List<Ticket> tickets = new ArrayList<>();

    public List<Ticket> allTickets() {
        List<Ticket> ticketsCopy = new ArrayList<>(tickets);
        return ticketsCopy;
  }

  public void addTicket(Ticket ticket) {
        tickets.add(ticket);
  }

  public Ticket findTicket(int id) {
    for (Ticket ticket : tickets) {
        if (ticket.getId() == id) {
            return ticket;
        }
    }
    return null;
  }

  public boolean updateSeverity(int id, SeverityLevel severity) {
    for (Ticket ticket : tickets) {
        if (ticket.getId() == id) {
            ticket.setSeverity(severity);
            return true;
        }
     }
     return false;
  }
}