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
}
