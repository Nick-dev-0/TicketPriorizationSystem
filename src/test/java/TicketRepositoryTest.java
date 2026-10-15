import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import TicketPriorizationSystem.src.main.java.SeverityLevel;
import TicketPriorizationSystem.src.main.java.Ticket;
import TicketPriorizationSystem.src.main.java.TicketRepository;

public class TicketRepositoryTest {
    private TicketRepository ticketRepository;
    
    @BeforeEach 
    void setUp() {
        ticketRepository = new TicketRepository();
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
}
