package com.ticketpriorization;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TicketServiceTest {
    private TicketRepository ticketRepository;
    private TicketService ticketService;

    @BeforeEach 
    void setUp() {
        ticketRepository = new TicketRepository();
        ticketService = new TicketService(ticketRepository);
    }

    @Test 
    @DisplayName("ticketSeveritySorted should be sorted by priority, highest first")
    void ticketSeveritySorted() {
        boolean ticketResult1 = ticketService.addTicket(new Ticket(
        1,
        "Login page unavailable",
        "Users cannot log in to the application.",
        SeverityLevel.SEV1,
        Instant.parse("2026-10-12T09:00:00Z")
));

        boolean ticketResult2 = ticketService.addTicket(new Ticket(
                2,
                "Profile page loading slowly",
                "The profile page takes several seconds to load.",
                SeverityLevel.SEV3,
                Instant.parse("2026-10-12T10:30:00Z")
        ));

        boolean ticketResult3 = ticketService.addTicket(new Ticket(
                3,
                "Payment processing failure",
                "Some users cannot complete their payments.",
                SeverityLevel.SEV2,
                Instant.parse("2026-10-12T09:45:00Z")
        ));

        boolean ticketResult4 = ticketService.addTicket(new Ticket(
                4,
                "Application outage",
                "The main application is unavailable to all users.",
                SeverityLevel.SEV1,
                Instant.parse("2026-10-12T08:30:00Z")
        ));
        assertTrue(ticketResult1, "ticketService1 should return true");
        assertTrue(ticketResult2, "ticketService2 should return true");
        assertTrue(ticketResult3, "ticketService3 should return true");
        assertTrue(ticketResult4, "ticketService4 should return true");

        List<Ticket> sortedTickets = ticketService.ticketSeveritySorted();
        List<Integer> ticketsById = new ArrayList<>();

        for (Ticket ticket : sortedTickets) {
                ticketsById.add(ticket.getId());
        }

        assertEquals(List.of(4, 1, 3, 2), ticketsById);
    }
    
    @Test
    @DisplayName("ticketService should add the ticket")
    void addTicket() {
        boolean ticketResult = ticketService.addTicket(new Ticket(
            2,
            "Login page unavailable",
            "Users cannot log in to the application.",
            SeverityLevel.SEV1,
            Instant.parse("2026-10-12T09:00:00Z")
    ));
        assertTrue(ticketResult, "ticketService should return true");
    }

    @Test
    @DisplayName("ticketService adds ticket and then tests with duplicate")
    void addTicketDuplicateId() {
        boolean ticketResult = ticketService.addTicket(new Ticket(
        2,
        "Login page unavailable",
        "Users cannot log in to the application.",
        SeverityLevel.SEV1,
        Instant.parse("2026-10-12T09:00:00Z")));

        assertTrue(ticketResult, "ticketService should return true");

        boolean ticketResultDuplicate = ticketService.addTicket(new Ticket(
                2,
                "Login page unavailable",
                "Users cannot log in to the application.",
                SeverityLevel.SEV1,
                Instant.parse("2026-10-12T09:00:00Z")
        ));
        assertFalse(ticketResultDuplicate, "ticketService should return false since it's a duplicate");
        }

    @Test
    @DisplayName("Ticket should be passed down to ticketService")
    void findTicketSuccess() {
        int id = 1;
        boolean ticketResult = ticketService.addTicket(new Ticket(
            id,
            "Login page unavailable",
            "Users cannot log in to the application.",
            SeverityLevel.SEV1,
            Instant.parse("2026-10-12T09:00:00Z")
    ));

        assertTrue(ticketResult, "ticketService should return true");
    
        Ticket findTicketResult = ticketService.findTicket(id);
        assertNotNull(findTicketResult, "Ticket should be not null");
        assertEquals(id, findTicketResult.getId(), "The found tickets ID should match the searched ID");
    }

    @Test
    @DisplayName("Ticket should be be returned as null when searched")
    void findTicketFailure() {    
        Ticket findTicketResult = ticketService.findTicket(1);
        assertNull(findTicketResult, "Ticket should be null");
    }

    @Test
    @DisplayName("Updating severity should succeed for an existing ticket ID")
    void updateSeverity() {
        int id = 1;
        SeverityLevel severity = SeverityLevel.SEV4;

        Ticket ticket = new Ticket(
            id,
            "Login page unavailable",
            "Users cannot log in to the application.",
            SeverityLevel.SEV1,
            Instant.parse("2026-10-12T09:00:00Z")
        );

        boolean ticketResult = ticketService.addTicket(ticket);
        assertTrue(ticketResult, "ticketResult should return true");

        boolean ticketUpdateResult = ticketService.updateSeverity(id, severity);

        assertTrue(ticketUpdateResult, "ticketUpdateResult should return true");

        assertEquals(severity, ticket.getSeverity(),
            "The ticket's severity should be updated");
    }

    @Test
    @DisplayName("Updating severity should fail for a non-existing ticket ID")
    void updateSeverityFailure() {
        int id = 1;
        SeverityLevel severity = SeverityLevel.SEV4;

        Ticket ticket = new Ticket(
            id,
            "Login page unavailable",
            "Users cannot log in to the application.",
            SeverityLevel.SEV1,
            Instant.parse("2026-10-12T09:00:00Z")
        );

        boolean ticketResult = ticketService.addTicket(ticket);
        assertTrue(ticketResult, "ticketResult should return true");

        boolean ticketUpdateResult = ticketService.updateSeverity(id + 1, severity);

        assertFalse(ticketUpdateResult,
            "Updating severity should return false for a non-existing ID");

        assertEquals(SeverityLevel.SEV1, ticket.getSeverity(),
            "The existing ticket's severity should remain unchanged");
    }
}
