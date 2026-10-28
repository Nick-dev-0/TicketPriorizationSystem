package com.ticketpriorization;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;

@DataMongoTest(properties = "spring.mongodb.database=ticket_system_test")
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
    }

    @Test
    @DisplayName("Saving a ticket should generate an ID")
    void saveTicketGeneratesId() {
        Ticket ticket = new Ticket(
            "Login page unavailable",
            "Users cannot log in to the application.",
            SeverityLevel.SEV1,
            Instant.parse("2026-10-12T09:00:00Z")
    );

        Ticket ticketResult = ticketRepository.save(ticket);

        assertNotNull(ticketResult);
        assertNotNull(ticketResult.getId(), "ticketResults ID should not be null, and should have an ID");
        assertEquals(ticket.getTitle(), ticketResult.getTitle(), "Titles should match");
        assertEquals(ticket.getDescription(), ticketResult.getDescription(), "Descriptions should match");
        assertEquals(ticket.getSeverity(), ticketResult.getSeverity(), "Severity should match");
    }

    @Test
    @DisplayName("A saved ticket should be findable by its ID")
    void findByIdReturnsSavedTicket() {
        Ticket ticket = new Ticket(
                "Login page unavailable",
                "Users cannot log in to the application.",
                SeverityLevel.SEV1,
                Instant.parse("2026-10-12T09:00:00Z")
        );

        Ticket ticketResult = ticketRepository.save(ticket);

        assertNotNull(ticketResult);

        String ticketResultId = ticketResult.getId();

        assertNotNull(ticketResultId);

        Optional<Ticket> ticketFound = ticketRepository.findById(ticketResultId);

        assertTrue(ticketFound.isPresent(), "Saved ticket should be found");

        Ticket foundTicket = ticketFound.get();

        assertEquals(ticket.getTitle(), foundTicket.getTitle(), "Titles should match");
        assertEquals(ticket.getDescription(), foundTicket.getDescription(), "Descriptions should match");
        assertEquals(ticket.getSeverity(), foundTicket.getSeverity(), "Severity should match");

    }


    @Test
    @DisplayName("Ticket should be be returned as null when searched")
    void findTicketFailure() {
        Optional<Ticket> ticketFound = ticketRepository.findById("00x000000000x0000");

        assertFalse(ticketFound.isPresent());
    }

    @Test
    @DisplayName("findAll should return all saved tickets")
    void findAllReturnsSavedTickets() {
        Ticket ticket1 = new Ticket(
        "Login page unavailable",
        "Users cannot log in to the application.",
        SeverityLevel.SEV1,
        Instant.parse("2026-10-12T09:00:00Z")
        );

        Ticket ticket2 = new Ticket(
                "Profile page loading slowly",
                "The profile page takes several seconds to load.",
                SeverityLevel.SEV3,
                Instant.parse("2026-10-12T10:30:00Z")
        );

        Ticket ticket3 = new Ticket(
                "Payment processing failure",
                "Some users cannot complete their payments.",
                SeverityLevel.SEV2,
                Instant.parse("2026-10-12T09:45:00Z")
        );

        Ticket ticket1Result = ticketRepository.save(ticket1);
        Ticket ticket2Result = ticketRepository.save(ticket2);
        Ticket ticket3Result = ticketRepository.save(ticket3);

        List<Ticket> allTickets = ticketRepository.findAll();

        List<String> returnedIds = allTickets.stream().map(Ticket::getId).toList();

        assertEquals(3, allTickets.size());

        assertTrue(returnedIds.contains(ticket1Result.getId()));
        assertTrue(returnedIds.contains(ticket2Result.getId()));
        assertTrue(returnedIds.contains(ticket3Result.getId()));
    }

    @Test
    void updateTicketPersistsChange() {
        Ticket ticket = new Ticket(
                "Login page unavailable",
                "Users cannot log in to the application.",
                SeverityLevel.SEV1,
                Instant.parse("2026-10-12T09:00:00Z")
        );

        Ticket ticketResult = ticketRepository.save(ticket);

        String ticketID = ticketResult.getId();

        String titleChange = "Login page unavailable!";
        String descriptionChange = "Users cannot log in to the application!";
        SeverityLevel severityLevel = SeverityLevel.SEV3;
        Status status = Status.RESOLVED;

        ticketResult.setTitle(titleChange);
        ticketResult.setDescription(descriptionChange);
        ticketResult.setSeverity(severityLevel);
        ticketResult.setStatus(status);

        ticketRepository.save(ticketResult);

        Optional<Ticket> ticketFromDatabase = ticketRepository.findById(ticketID);

        assertTrue(ticketFromDatabase.isPresent());

        Ticket retrievedTicket = ticketFromDatabase.get();

        assertEquals(titleChange, retrievedTicket.getTitle());
        assertEquals(descriptionChange, retrievedTicket.getDescription());
        assertEquals(severityLevel, retrievedTicket.getSeverity());
        assertEquals(status, retrievedTicket.getStatus());
    }
}
