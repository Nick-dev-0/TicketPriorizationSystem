
import { useState } from "react";
import type { SubmitEvent } from "react";
import { updateTicket } from "../services/ticketService";
import type { Ticket, SeverityLevel, TicketStatus } from "../models/ticket";

type EditTicketModalProps = {
    ticket: Ticket;
    onClose: () => void;
    onTicketUpdated: (updatedTicket: Ticket) => void;
};

function EditTicketModal({ ticket, onClose, onTicketUpdated }: EditTicketModalProps) {

    const [title, setTitle] = useState(ticket.title);
    const [description, setDescription] = useState(ticket.description);
    const [severity, setSeverity] = useState<SeverityLevel>(ticket.severity);
    const [status, setStatus] = useState<TicketStatus>(ticket.status);

    async function handleSubmit(event: SubmitEvent<HTMLFormElement>) {
        event.preventDefault();

        const updatedTicket = await updateTicket(ticket.id, {title, description, severity, status});

        onTicketUpdated(updatedTicket);
        onClose();
    }

    return (
        <>
            <div className="modal d-block show" tabIndex={-1} role="dialog" aria-modal="true" aria-labelledby="editTicketModalLabel">
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content">

                        <div className="modal-header">
                            <h4 className="modal-title" id="editTicketModalLabel">Edit Ticket</h4>
                            <button type="button" className="btn-close" aria-label="Close" onClick={onClose} />
                        </div>

                        <form onSubmit={handleSubmit}>
                            <div className="modal-body">

                                <div className="mb-3">
                                    <label htmlFor="title-modal" className="form-label">Title</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="title-modal"
                                        name="title"
                                        value={title}
                                        onChange={(event) => setTitle(event.target.value)}
                                        required
                                    />
                                </div>

                                <div className="mb-3">
                                    <label htmlFor="description-modal" className="form-label">Description</label>
                                    <textarea
                                        className="form-control"
                                        id="description-modal"
                                        name="description"
                                        rows={3}
                                        value={description}
                                        onChange={(event) => setDescription(event.target.value)}
                                        required
                                    />
                                </div>

                                <div className="mb-3">
                                    <label htmlFor="severity-modal" className="form-label">Severity</label>
                                    <select
                                        className="form-select"
                                        id="severity-modal"
                                        name="severity"
                                        value={severity}
                                        onChange={(event) => setSeverity(event.target.value as SeverityLevel)}
                                        required
                                    >
                                        <option value="SEV1">SEV1</option>
                                        <option value="SEV2">SEV2</option>
                                        <option value="SEV3">SEV3</option>
                                        <option value="SEV4">SEV4</option>
                                    </select>
                                </div>

                                <div className="mb-3">
                                    <label htmlFor="status-modal" className="form-label">Status</label>
                                    <select
                                        className="form-select"
                                        id="status-modal"
                                        name="status"
                                        value={status}
                                        onChange={(event) => setStatus(event.target.value as TicketStatus)}
                                        required
                                    >
                                        <option value="OPEN">OPEN</option>
                                        <option value="RESOLVED">RESOLVED</option>
                                    </select>
                                </div>

                            </div>

                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
                                <button type="submit" className="btn btn-primary">Save Changes</button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>

            <div className="modal-backdrop show" onClick={onClose}></div>
        </>
    );
}

export default EditTicketModal;
