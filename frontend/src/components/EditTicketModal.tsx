
import { useState } from "react";
import type { SubmitEvent } from "react";
import { deleteTicket, updateTicket } from "../services/ticketService";
import type { Ticket, SeverityLevel, TicketStatus } from "../models/ticket";

type EditTicketModalProps = {
    ticket: Ticket;
    onClose: () => void;
    onTicketUpdated: (updatedTicket: Ticket) => void;
    onTicketDeleted: (deletedTicketId: string) => void;
};

function EditTicketModal({ ticket, onClose, onTicketUpdated, onTicketDeleted }: EditTicketModalProps) {

    const [title, setTitle] = useState(ticket.title);
    const [description, setDescription] = useState(ticket.description);
    const [severity, setSeverity] = useState<SeverityLevel>(ticket.severity);
    const [status, setStatus] = useState<TicketStatus>(ticket.status);

    const [isDeleting, setIsDeleting] = useState(false);
    const [deleteError, setDeleteError] = useState<string | null>(null);

    async function handleSubmit(event: SubmitEvent<HTMLFormElement>) {
        event.preventDefault();

        const updatedTicket = await updateTicket(ticket.id, {title, description, severity, status});

        onTicketUpdated(updatedTicket);
        onClose();
    }

    async function handleDelete() {
        const confirmed = window.confirm(`Are you sure you want to delete "${ticket.title}"?`);

        if (!confirmed) {
            return;
        }

        setIsDeleting(true);
        setDeleteError(null);

        try {
            await deleteTicket(ticket.id);

            onTicketDeleted(ticket.id);
            onClose();

        } catch (error) {
            setDeleteError(error instanceof Error ? error.message : "Failed to delete ticket.");
        } finally {
            setIsDeleting(false);
        }
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
                                {deleteError && (<div className="alert alert-danger" role="alert">{deleteError}</div>)}
                            </div>

                            <div className="modal-footer">
                                {ticket.expiresAt != null && (
                                    <button type="button" className="btn btn-danger me-auto" onClick={handleDelete} disabled={isDeleting}>
                                        {isDeleting ? "Deleting..." : "Delete Ticket"}
                                    </button>
                                )}
                                <button type="button" className="btn btn-secondary" onClick={onClose} disabled={isDeleting}>Cancel</button>
                                <button type="submit" className="btn btn-primary" disabled={isDeleting}>Save Changes</button>
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
