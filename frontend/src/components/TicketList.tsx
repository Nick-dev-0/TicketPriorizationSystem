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
    onTicketDeleted: (deletedTicketId: string) => void;
};

function TicketList({ tickets, onTicketUpdated, onTicketDeleted }: TicketListProps) {

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

    function handleSeveritySort() {
        setSeveritySortOrder(currentOrder => currentOrder === "highest" ? "lowest" : "highest");
    }

    return (
        <section className="dashboard-panel">
            <div className="panel-body">
                <div className="panel-heading">
                    <h2 className="panel-title">
                        {showOpenTickets && showResolvedTickets
                            ? "All tickets"
                            : showOpenTickets
                                ? "Open tickets"
                                : showResolvedTickets
                                    ? "Resolved tickets"
                                    : "No status selected"}
                    </h2>

                    <span className="queue-total">
                        {openTicketCount} open · {resolvedTicketCount} resolved
                    </span>
                </div>

                <div className="ticket-filters" role="group" aria-label="Filter tickets by status">
                    <div className="filter-option">
                        <input
                            type="checkbox"
                            className="form-check-input"
                            id="openTicketFilter"
                            checked={showOpenTickets}
                            onChange={(event) => setShowOpenTickets(event.target.checked)}
                        />
                        <label className="filter-label" htmlFor="openTicketFilter">Open</label>
                        <span className="filter-count">{openTicketCount}</span>
                    </div>

                    <div className="filter-option">
                        <input
                            type="checkbox"
                            className="form-check-input"
                            id="resolvedTicketFilter"
                            checked={showResolvedTickets}
                            onChange={(event) => setShowResolvedTickets(event.target.checked)}
                        />
                        <label className="filter-label" htmlFor="resolvedTicketFilter">Resolved</label>
                        <span className="filter-count">{resolvedTicketCount}</span>
                    </div>
                </div>

                <div className="table-responsive">
                    <table className="table table-hover align-middle ticket-table">
                        <thead>
                        <tr>
                            <th scope="col">Title</th>
                            <th scope="col">Description</th>
                            <th scope="col">
                                <button
                                    type="button"
                                    className="sort-button"
                                    onClick={handleSeveritySort}
                                    aria-label={`Sort by severity, currently ${severitySortOrder} first`}>
                                    Severity <span aria-hidden="true">{severitySortOrder === "highest" ? "↓" : "↑"}</span>
                                </button>
                            </th>
                            <th scope="col">Created</th>
                            <th scope="col">Status</th>
                            <th scope="col"><span className="visually-hidden">Actions</span></th>
                        </tr>
                        </thead>

                        <tbody>
                        {sortedTickets.length === 0 ? (
                            <tr>
                                <td colSpan={6} className="empty-row">
                                    No tickets match the selected filters.
                                </td>
                            </tr>
                        ) : (
                            sortedTickets.map(ticket => (
                                <tr key={ticket.id}>
                                    <td className="cell-title">{ticket.title}</td>
                                    <td className="cell-description">{ticket.description}</td>
                                    <td>
                                        <span className={`tag severity-${ticket.severity.toLowerCase()}`}>
                                            {ticket.severity}
                                        </span>
                                    </td>
                                    <td className="cell-time mono">{new Date(ticket.timestamp).toLocaleString()}</td>
                                    <td>
                                        <span className={`tag status-${ticket.status.toLowerCase()}`}>
                                            {ticket.status}
                                        </span>
                                    </td>
                                    <td className="cell-actions">
                                        <button
                                            type="button"
                                            className="btn btn-outline-primary btn-sm"
                                            aria-label={`Edit ${ticket.title}`}
                                            onClick={() => setTicketToEdit(ticket)}>
                                            Edit
                                        </button>
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
                        onTicketDeleted={onTicketDeleted}/>
                )}
            </div>
        </section>
    );
}

export default TicketList;