package com.ticketpriorization;

import java.time.Instant;
public class Main {
    public static void main(String[] args) {
    TicketRepository ticketRepository = new TicketRepository();
    TicketService ticketService = new TicketService(ticketRepository);

    ticketService.addTicket(new Ticket(
            1,
            "Login page unavailable",
            "Users cannot log in to the application.",
            SeverityLevel.SEV1,
            Instant.parse("2026-10-12T09:00:00Z")
    ));

    ticketService.addTicket(new Ticket(
            2,
            "Profile page loading slowly",
            "The profile page takes several seconds to load.",
            SeverityLevel.SEV3,
            Instant.parse("2026-10-12T10:30:00Z")
    ));

    ticketService.addTicket(new Ticket(
            3,
            "Payment processing failure",
            "Some users cannot complete their payments.",
            SeverityLevel.SEV2,
            Instant.parse("2026-10-12T09:45:00Z")
    ));

    ticketService.addTicket(new Ticket(
            4,
            "Application outage",
            "The main application is unavailable to all users.",
            SeverityLevel.SEV1,
            Instant.parse("2026-10-12T08:30:00Z")
    ));


    System.out.println(ticketService.ticketSeveritySorted());

    System.out.println(ticketService.findTicket(5));
    boolean updateResult = ticketService.updateSeverity(1, SeverityLevel.SEV4);

    if (updateResult) {
        System.out.println(ticketService.ticketSeveritySorted());
    }

    }
}
