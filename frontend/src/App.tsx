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

    return (
        <main>
            <h1>Ticket Prioritization System</h1>

            <TicketForm onTicketCreated={handleTicketCreated} />

            <TicketList tickets={tickets} />
        </main>
    );
}

export default App;