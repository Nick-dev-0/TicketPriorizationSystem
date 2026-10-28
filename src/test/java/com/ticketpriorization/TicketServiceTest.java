package com.ticketpriorization;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TicketServiceTest {
    private TicketRepository ticketRepository;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new TicketService(ticketRepository);
    }

    @Test
    @DisplayName("createTicket should create an open ticket")
    void ticketSeveritySorted() {
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

        Ticket ticket4 = new Ticket(
                "Application outage",
                "The main application is unavailable to all users.",
                SeverityLevel.SEV1,
                Instant.parse("2026-10-12T08:30:00Z")
        );

        List<Ticket> testTickets = List.of(ticket1, ticket2, ticket3, ticket4);

        when(ticketRepository.findAll()).thenReturn(testTickets);

        List<Ticket> sortedTickets = ticketService.ticketSeveritySorted();

        List<String> sortedTicketTitles = new ArrayList<>();

        for (Ticket ticket : sortedTickets) {
            sortedTicketTitles.add(ticket.getTitle());
            assertEquals(Status.OPEN, ticket.getStatus());

        }

        assertEquals(
                List.of(
                        "Application outage",
                        "Login page unavailable",
                        "Payment processing failure",
                        "Profile page loading slowly"
                ),
                sortedTicketTitles
        );
    }


    @Test
    @DisplayName("ticketService should add the ticket")
    void createTicket() {
        CreateTicketRequest request = new CreateTicketRequest();

        request.setTitle("Login page unavailable");
        request.setDescription("Users cannot log in to the application.");
        request.setSeverity(SeverityLevel.SEV1);

        when(ticketRepository.save(any(Ticket.class))).thenAnswer((invocation -> invocation.getArgument(0)));

        Ticket ticket = ticketService.createTicket(request);

        assertEquals("Login page unavailable", ticket.getTitle());
        assertEquals("Users cannot log in to the application.", ticket.getDescription());
        assertEquals(SeverityLevel.SEV1, ticket.getSeverity());

        }

    @Test
    @DisplayName("Ticket should be passed down to ticketService")
    void findTicketSuccess() {
        Ticket ticket1 = new Ticket(
                "Login page unavailable",
                "Users cannot log in to the application.",
                SeverityLevel.SEV1,
                Instant.parse("2026-10-12T09:00:00Z")
        );

        String ticketId = "test-id";
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket1));

        Optional<Ticket> ticketResult = ticketService.findTicket(ticketId);

        assertTrue(ticketResult.isPresent());
        assertSame(ticket1, ticketResult.get());
    }

    @Test
    @DisplayName("Ticket update should return true")
    void updateTicket() {
        Ticket ticket1 = new Ticket(
                "Login page unavailable",
                "Users cannot log in to the application.",
                SeverityLevel.SEV1,
                Instant.parse("2026-10-12T09:00:00Z")
        );

        String ticketId = "test-id";

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket1));

        when(ticketRepository.save(any(Ticket.class))).thenAnswer((invocation -> invocation.getArgument(0)));

        String titleChange = "Login page unavailable!";
        String descriptionChange = "Users cannot log in to the application!";
        SeverityLevel severityLevel = SeverityLevel.SEV3;
        Status status = Status.RESOLVED;

        Optional<Ticket> ticketResult = ticketService.updateTicket(titleChange, descriptionChange, severityLevel, ticketId, status);

        assertTrue(ticketResult.isPresent());

        Ticket updatedTicket = ticketResult.get();

        assertEquals(titleChange, updatedTicket.getTitle());
        assertEquals(descriptionChange, updatedTicket.getDescription());
        assertEquals(severityLevel, updatedTicket.getSeverity());
        assertEquals(status, updatedTicket.getStatus());
    }

    @Test
    @DisplayName("Ticket can't be updated if it doesn't exist")
    void updateTicketNotFound() {

        String ticketId = "missing-id";

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        Optional<Ticket> ticketResult = ticketService.updateTicket(
                "Login page unavailable!",
                "Users cannot log in to the application!",
                SeverityLevel.SEV3,
                ticketId,
                Status.RESOLVED
        );

        assertTrue(ticketResult.isEmpty());

    }
}