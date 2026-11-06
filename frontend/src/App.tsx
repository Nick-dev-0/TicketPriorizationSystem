
import { useEffect, useState } from "react";
import type { Ticket } from "./models/ticket";
import { getTickets } from "./services/ticketService";
import TicketList from "./components/TicketList";
import TicketForm from "./components/TicketForm";

function App() {

    const [tickets, setTickets] = useState<Ticket[]>([]);

    useEffect(() => {
        async function loadTickets() {
            const initialTickets = await getTickets();
            setTickets(initialTickets);
        }

        loadTickets();
    }, []);

    function handleTicketCreated(newTicket: Ticket) {
        setTickets(currentTickets => [...currentTickets, newTicket]);
    }

    function handleTicketUpdated(updatedTicket: Ticket) {
        setTickets(currentTickets =>
            currentTickets.map(ticket =>
                ticket.id === updatedTicket.id ? updatedTicket : ticket
            )
        );
    }

    function handleTicketDeleted(deletedTicketId: string) {
        setTickets(currentTickets =>
            currentTickets.filter(ticket => ticket.id !== deletedTicketId)
        );
    }

    return (
        <main className="app-shell">
            <div className="container app-container py-5">
                <header className="app-header">
                    <div>
                        <h1 className="app-title">Ticket Prioritization</h1>
                    </div>

                    <div className="environment-badge" aria-label="Demo environment">
                        <span className="environment-badge-dot" aria-hidden="true"></span>
                        DEMO ENVIRONMENT
                    </div>
                </header>

                <TicketForm onTicketCreated={handleTicketCreated} />

                <TicketList
                    tickets={tickets}
                    onTicketUpdated={handleTicketUpdated}
                    onTicketDeleted={handleTicketDeleted}
                />
            </div>
        </main>
    );
}

export default App;
