import { useEffect, useState } from "react";
import type { Ticket } from "../models/ticket";
import { getTickets } from "../services/ticketService";

function TicketList() {

    const [tickets, setTickets] = useState<Ticket[]>([]);

    useEffect(() => {

        async function loadTickets() {
            const allTickets = await getTickets();
            setTickets(allTickets);
        }

        loadTickets();

    }, []);

    return (
        <section>
            <h2>Tickets</h2>

            <table>
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Severity</th>
                    <th>Status</th>
                </tr>
                </thead>

                <tbody>
                {tickets.map(ticket => (
                    <tr key={ticket.id}>
                        <td>{ticket.title}</td>
                        <td>{ticket.severity}</td>
                        <td>{ticket.status}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </section>
    );
}

export default TicketList;