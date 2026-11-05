import type { Ticket } from "../models/ticket";

type TicketListProps = {
    tickets: Ticket[];
};

function TicketList({ tickets }: TicketListProps) {

    return (
        <section>
            <h2>Tickets</h2>

            <table>
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Severity</th>
                    <th>Created Date</th>
                    <th>Status</th>
                    <th>Edit</th>
                </tr>
                </thead>

                <tbody>
                {tickets.map(ticket => (
                    <tr key={ticket.id}>
                        <td>{ticket.title}</td>
                        <td>{ticket.description}</td>
                        <td>{ticket.severity}</td>
                        <td>
                            {new Date(ticket.timestamp).toLocaleString()}
                        </td>
                        <td>{ticket.status}</td>
                        <td>
                            <button type="button">
                                Edit
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </section>
    );
}

export default TicketList;