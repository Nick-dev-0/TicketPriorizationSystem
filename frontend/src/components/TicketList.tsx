import { useState } from "react";
import type { Ticket, SeverityLevel } from "../models/ticket";
import EditTicketModal from "./EditTicketModal";

const severityRank: Record<SeverityLevel, number> = {
    SEV1: 1,
    SEV2: 2,
    SEV3: 3,
    SEV4: 4
};

type TicketListProps = {
    tickets: Ticket[];
    onTicketUpdated: (updatedTicket: Ticket) => void;
};

function TicketList({ tickets, onTicketUpdated }: TicketListProps) {

    const [ticketToEdit, setTicketToEdit] = useState<Ticket | null>(null);
    const [severitySortOrder, setSeveritySortOrder] = useState<"highest" | "lowest">("highest");

    const [showOpenTickets, setShowOpenTickets] = useState(true);
    const [showResolvedTickets, setShowResolvedTickets] = useState(false);

    const openTicketCount = tickets.filter(ticket => ticket.status === "OPEN").length;
    const resolvedTicketCount = tickets.filter(ticket => ticket.status === "RESOLVED").length;

    const filteredTickets = tickets.filter(ticket =>
        (showOpenTickets && ticket.status === "OPEN") ||
        (showResolvedTickets && ticket.status === "RESOLVED")
    );

    const sortedTickets = [...filteredTickets].sort((firstTicket, secondTicket) => {
        const firstSeverityRank = severityRank[firstTicket.severity];
        const secondSeverityRank = severityRank[secondTicket.severity];

        if (severitySortOrder === "highest") {
            return firstSeverityRank - secondSeverityRank;
        }

        return secondSeverityRank - firstSeverityRank;
    });


    return (
        <section className="card">
            <div className="card-body">
                <h2 className="card-title mb-4">
                    {showOpenTickets && showResolvedTickets
                        ? "All Tickets"
                        : showOpenTickets
                            ? "Open Tickets"
                            : showResolvedTickets
                                ? "Resolved Tickets"
                                : "No Tickets Selected"}
                </h2>

                <div className="card mb-3">
                    <div className="card-body p-0">
                        <div className="row g-0">

                            <div className="col-6 text-center border-end py-3">
                                <div className="form-check d-inline-block">
                                    <input
                                        type="checkbox"
                                        className="form-check-input"
                                        id="openTicketFilter"
                                        checked={showOpenTickets}
                                        onChange={(event) => setShowOpenTickets(event.target.checked)}
                                    />
                                    <label className="form-check-label" htmlFor="openTicketFilter">Open</label>
                                </div>

                                <div className="mt-1">
                                    <small className="text-muted">{openTicketCount} tickets</small>
                                </div>
                            </div>

                            <div className="col-6 text-center py-3">
                                <div className="form-check d-inline-block">
                                    <input
                                        type="checkbox"
                                        className="form-check-input"
                                        id="resolvedTicketFilter"
                                        checked={showResolvedTickets}
                                        onChange={(event) => setShowResolvedTickets(event.target.checked)}
                                    />
                                    <label className="form-check-label" htmlFor="resolvedTicketFilter">Resolved</label>
                                </div>

                                <div className="mt-1">
                                    <small className="text-muted">{resolvedTicketCount} tickets</small>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>

                <div className="table-responsive">
                    <table className="table table-striped table-hover align-middle">
                        <thead className="table-light">
                        <tr>
                            <th>Title</th>
                            <th>Description</th>
                            <th>
                                <button type="button" className="btn btn-sm btn-outline-secondary py-0 px-2" onClick={handleSeveritySort}>
                                    <span className="fw-bold">Severity</span>
                                    <span className="ms-1">{severitySortOrder === "highest" ? "↓" : "↑"}</span>
                                </button>
                            </th>
                            <th>Created Date</th>
                            <th>Status</th>
                            <th>Edit</th>
                        </tr>
                        </thead>

                        <tbody>
                        {sortedTickets.length === 0 ? (
                            <tr>
                                <td colSpan={6} className="text-center text-muted py-3">
                                    No tickets match the selected filters.
                                </td>
                            </tr>
                        ) : (
                            sortedTickets.map(ticket => (
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
                            ))
                        )}
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


    function handleSeveritySort() {
        setSeveritySortOrder(currentOrder => currentOrder === "highest" ? "lowest" : "highest");
    }
}

export default TicketList;
