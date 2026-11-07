
import { useEffect, useState } from "react";
import type { Ticket } from "./models/ticket";
import { getTickets } from "./services/ticketService";
import { getSystemHealth } from "./services/systemHealthService";
import TicketList from "./components/TicketList";
import TicketForm from "./components/TicketForm";

type SystemStatus = "connecting" | "online" | "unavailable";

const HEALTH_RETRY_INTERVAL = 5000;
const MAX_HEALTH_ATTEMPTS = 24;

function App() {

    const [tickets, setTickets] = useState<Ticket[]>([]);
    const [systemStatus, setSystemStatus] = useState<SystemStatus>("connecting");

    useEffect(() => {
        let isCancelled = false;
        let retryTimer: ReturnType<typeof setTimeout> | undefined;

        async function checkSystemHealth(attempt = 1) {
            try {
                const health = await getSystemHealth();

                if (isCancelled) return;

                if (health.status === "UP") {
                    setSystemStatus("online");
                    return;
                }
            } catch {
                // The backend may still be starting.
            }

            if (isCancelled) return;

            if (attempt >= MAX_HEALTH_ATTEMPTS) {
                setSystemStatus("unavailable");
                return;
            }

            setSystemStatus("connecting");

            retryTimer = setTimeout(() => {
                checkSystemHealth(attempt + 1);
            }, HEALTH_RETRY_INTERVAL);
        }

        checkSystemHealth();

        return () => {
            isCancelled = true;

            if (retryTimer !== undefined) {
                clearTimeout(retryTimer);
            }
        };
    }, []);

    useEffect(() => {
        if (systemStatus !== "online") return;

        let isCancelled = false;

        async function loadTickets() {
            try {
                const initialTickets = await getTickets();

                if (!isCancelled) {
                    setTickets(initialTickets);
                }
            } catch (error) {
                console.error("Failed to retrieve tickets:", error);
            }
        }

        loadTickets();

        return () => {
            isCancelled = true;};}, [systemStatus]);

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

    const statusLabels: Record<SystemStatus, string> = {
        connecting: "Connecting...",
        online: "System Online",
        unavailable: "System Unavailable"
    };

    return (
        <main className="app-shell">
            <div className="container app-container py-5">
                <header className="app-header">
                    <div>
                        <h1 className="app-title">Ticket Prioritization</h1>
                        <p className="app-subtitle">Prioritize, track, and resolve support tickets.</p>
                    </div>

                    <div className={`system-status system-status-${systemStatus}`} role="status">
                        <span className="system-status-dot" aria-hidden="true"></span>
                        {statusLabels[systemStatus]}
                    </div>
                </header>

                <TicketForm onTicketCreated={handleTicketCreated} />

                <TicketList
                    tickets={tickets}
                    onTicketUpdated={handleTicketUpdated}
                    onTicketDeleted={handleTicketDeleted}/>
            </div>
        </main>
    );
}

export default App;
