
import { useState } from "react";
import type { Ticket } from "../models/ticket";
import EditTicketModal from "./EditTicketModal";

type TicketListProps = {
    tickets: Ticket[];
    onTicketUpdated: (updatedTicket: Ticket) => void;
};

function TicketList({ tickets, onTicketUpdated }: TicketListProps) {

    const [ticketToEdit, setTicketToEdit] = useState<Ticket | null>(null);

    return (
        <section className="card">
            <div className="card-body">
                <h2 className="card-title mb-4">Tickets</h2>

                <div className="table-responsive">
                    <table className="table table-striped table-hover align-middle">
                        <thead className="table-light">
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
                                <td>{new Date(ticket.timestamp).toLocaleString()}</td>
                                <td>{ticket.status}</td>
                                <td>
                                    <button type="button" className="btn btn-outline-primary btn-sm" onClick={() => setTicketToEdit(ticket)}>Edit</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>

                {ticketToEdit && (
                    <EditTicketModal
                        key={ticketToEdit.id}
                        ticket={ticketToEdit}
                        onClose={() => setTicketToEdit(null)}
                        onTicketUpdated={onTicketUpdated}
                    />
                )}
            </div>
        </section>
    );
}

export default TicketList;
