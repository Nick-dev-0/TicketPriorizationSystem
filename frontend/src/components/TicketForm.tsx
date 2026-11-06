
import { useState } from "react";
import type { SubmitEvent } from "react";
import { createTicket, getSeveritySuggestion } from "../services/ticketService";
import type { SeveritySuggestionResponse } from "../models/severity-suggestion";
import type { Ticket, SeverityLevel } from "../models/ticket";

type TicketFormProps = {
    onTicketCreated: (ticket: Ticket) => void;
};

function TicketForm({ onTicketCreated }: TicketFormProps) {

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [suggestion, setSuggestion] = useState<SeveritySuggestionResponse | null>(null);
    const [severity, setSeverity] = useState<SeverityLevel>("SEV4");
    const [isSuggesting, setIsSuggesting] = useState(false);

    const [submissionError, setSubmissionError] = useState<string | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    async function handleSuggestion() {
        setIsSuggesting(true);

        try {
            const suggestion = await getSeveritySuggestion({title, description});

            setSuggestion(suggestion);
        } finally {
            setIsSuggesting(false);
        }
    }

    async function handleFormSubmit(event: SubmitEvent<HTMLFormElement>) {
        event.preventDefault();

        setSubmissionError(null);
        setIsSubmitting(true);

        try {
            const newTicket = await createTicket({title, description, severity});

            onTicketCreated(newTicket);

            setTitle("");
            setDescription("");
            setSeverity("SEV4");
            setSuggestion(null);

        } catch (error) {
            setSubmissionError(error instanceof Error ? error.message : "Failed to create ticket.");
        } finally {
            setIsSubmitting(false);
        }
    }

    function handleApplySuggestion() {
        if (suggestion) {
            setSeverity(suggestion.suggestedSeverity);
        }
    }

    return (
        <section className="dashboard-panel mb-4">
            <div className="panel-body">
                <div className="panel-heading">
                    <div>
                        <h2 className="panel-title">Create a ticket</h2>
                    </div>
                </div>

                <form id="create-ticket-form" onSubmit={handleFormSubmit}>

                    <div className="mb-3">
                        <label htmlFor="title" className="form-label">Title</label>
                        <input
                            type="text"
                            className="form-control"
                            value={title}
                            onChange={(event) => setTitle(event.target.value)}
                            id="title"
                            name="title"
                            placeholder="Write the ticket title..."
                            required
                        />
                    </div>

                    <div className="mb-3">
                        <label htmlFor="description" className="form-label">Description</label>
                        <textarea
                            className="form-control"
                            id="description"
                            value={description}
                            onChange={(event) => setDescription(event.target.value)}
                            name="description"
                            rows={3}
                            placeholder="Write the ticket description..."
                            required
                        />
                    </div>

                    <div className="mb-3">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <label htmlFor="severity" className="form-label mb-0">Severity</label>
                            <button
                                type="button"
                                className="btn btn-outline-primary btn-sm"
                                onClick={handleSuggestion}
                                disabled={isSuggesting}>
                                {isSuggesting ? "Awaiting..." : "AI Suggestion"}
                            </button>
                        </div>

                        <select
                            id="severity"
                            name="severity"
                            className="form-select"
                            value={severity}
                            onChange={(event) => setSeverity(event.target.value as SeverityLevel)}
                            required>
                            <option value="SEV1">SEV1</option>
                            <option value="SEV2">SEV2</option>
                            <option value="SEV3">SEV3</option>
                            <option value="SEV4">SEV4</option>
                        </select>

                        {suggestion && (
                            <div className="alert alert-info mt-3">
                                <h5>AI Recommended Severity: {suggestion.suggestedSeverity}</h5>
                                <p>{suggestion.reasoning}</p>
                                <button
                                    type="button"
                                    className="btn btn-primary btn-sm"
                                    onClick={handleApplySuggestion}>
                                    Use Recommendation
                                </button>
                            </div>
                        )}
                    </div>

                    {submissionError && (
                        <div className="alert alert-danger" role="alert">{submissionError}</div>
                    )}

                    <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
                        {isSubmitting ? "Creating..." : "Create Ticket"}
                    </button>
                </form>
            </div>
        </section>
    );
}

export default TicketForm;
