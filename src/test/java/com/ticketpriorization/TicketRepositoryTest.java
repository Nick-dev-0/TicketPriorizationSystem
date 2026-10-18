package com.ticketpriorization;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class TicketRepositoryTest {
    private TicketRepository ticketRepository;
    private TicketService ticketService;
    
    @BeforeEach 
    void setUp() {
        ticketRepository = new TicketRepository();
        ticketService = new TicketService(ticketRepository);
    }
    
    @Test
    @DisplayName("ticketRepository should add the ticket")
    void addTicket() {
        boolean ticketResult = ticketRepository.addTicket(new Ticket(
            2,
            "Login page unavailable",
            "Users cannot log in to the application.",
            SeverityLevel.SEV1,
            Instant.parse("2026-10-12T09:00:00Z")
    ));
        assertTrue(ticketResult, "ticketResult should return true");
    }

    @Test
    @DisplayName("ticketRepository adds ticket and then tests with duplicate")
    void addTicketDuplicateId() {
        boolean ticketResult = ticketRepository.addTicket(new Ticket(
        2,
        "Login page unavailable",
        "Users cannot log in to the application.",
        SeverityLevel.SEV1,
        Instant.parse("2026-10-12T09:00:00Z")));

        assertTrue(ticketResult, "ticketResult should return true");

        boolean ticketResultDuplicate = ticketRepository.addTicket(new Ticket(
                2,
                "Login page unavailable",
                "Users cannot log in to the application.",
                SeverityLevel.SEV1,
                Instant.parse("2026-10-12T09:00:00Z")
        ));
        assertFalse(ticketResultDuplicate, "ticketResult should return false since it's a duplicate");
        }

    @Test
    @DisplayName("Ticket should be found in ticketRepository")
    void findTicketSuccess() {
        int id = 1;
        boolean ticketResult = ticketRepository.addTicket(new Ticket(
            id,
            "Login page unavailable",
            "Users cannot log in to the application.",
            SeverityLevel.SEV1,
            Instant.parse("2026-10-12T09:00:00Z")
    ));

        assertTrue(ticketResult, "ticketResult should return true");
    
        Ticket findTicketResult = ticketRepository.findTicket(id);
        assertNotNull(findTicketResult, "Ticket should be not null");
        assertEquals(id, findTicketResult.getId(), "The found tickets ID should match the searched ID");
    }

    @Test
    @DisplayName("Ticket should be be returned as null when searched")
    void findTicketFailure() {    
        Ticket findTicketResult = ticketRepository.findTicket(1);
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

        boolean ticketResult = ticketRepository.addTicket(ticket);
        assertTrue(ticketResult, "ticketResult should return true");

        boolean ticketUpdateResult = ticketRepository.updateSeverity(id, severity);

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

        boolean ticketResult = ticketRepository.addTicket(ticket);
        assertTrue(ticketResult, "ticketResult should return true");

        boolean ticketUpdateResult = ticketRepository.updateSeverity(id + 1, severity);

        assertFalse(ticketUpdateResult,
            "Updating severity should return false for a non-existing ID");

        assertEquals(SeverityLevel.SEV1, ticket.getSeverity(),
            "The existing ticket's severity should remain unchanged");
    }

    @Test 
    @DisplayName("allTickets should return independent ticket copies")
    void allTickets() {
        Ticket ticket = new Ticket(
        1,
        "Login page unavailable",
        "Users cannot log in to the application.",
        SeverityLevel.SEV1,
        Instant.parse("2026-10-12T09:00:00Z")
        );

        assertTrue(ticketRepository.addTicket(ticket),"Ticket should be added successfully");

        Map<Integer, Ticket> allTickets = ticketRepository.allTickets();
        assertNotNull(allTickets, "Returned map should not be null");

        Ticket retrievedTicket = allTickets.get(1);
        assertNotNull(retrievedTicket, "Returned map should contain the ticket");

        retrievedTicket.setSeverity(SeverityLevel.SEV3);

        Ticket storedTicket = ticketRepository.findTicket(1);

        assertEquals(SeverityLevel.SEV1, storedTicket.getSeverity(), "Modifying a returned ticket should not change the stored ticket");
    }
}
