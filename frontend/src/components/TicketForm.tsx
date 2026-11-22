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
                    <h2 className="panel-title">Create ticket</h2>
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
                            placeholder="e.g. Checkout returns 500 after deploy"
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
                            placeholder="What happened, who is affected, and since when?"
                            required
                        />
                    </div>

                    <div className="mb-4">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <label htmlFor="severity" className="form-label mb-0">Severity</label>
                            <button
                                type="button"
                                className="btn btn-outline-primary btn-sm"
                                onClick={handleSuggestion}
                                disabled={isSuggesting}>
                                {isSuggesting ? "Analyzing…" : "Suggest severity"}
                            </button>
                        </div>

                        <select
                            id="severity"
                            name="severity"
                            className="form-select"
                            value={severity}
                            onChange={(event) => setSeverity(event.target.value as SeverityLevel)}
                            required>
                            <option value="SEV1">SEV1 · Critical</option>
                            <option value="SEV2">SEV2 · High</option>
                            <option value="SEV3">SEV3 · Medium</option>
                            <option value="SEV4">SEV4 · Low</option>
                        </select>

                        {suggestion && (
                            <div className="suggestion" role="status">
                                <div className="suggestion-header">
                                    <span className="suggestion-label">AI suggestion</span>
                                    <span className={`tag severity-${suggestion.suggestedSeverity.toLowerCase()}`}>
                                        {suggestion.suggestedSeverity}
                                    </span>
                                </div>
                                <p className="suggestion-reasoning">{suggestion.reasoning}</p>
                                <button
                                    type="button"
                                    className="btn btn-outline-primary btn-sm"
                                    onClick={handleApplySuggestion}>
                                    Apply {suggestion.suggestedSeverity}
                                </button>
                            </div>
                        )}
                    </div>

                    {submissionError && (
                        <div className="notice notice-error" role="alert">{submissionError}</div>
                    )}

                    <button type="submit" className="btn btn-primary" disabled={isSubmitting}>
                        {isSubmitting ? "Creating…" : "Create ticket"}
                    </button>
                </form>
            </div>
        </section>
    );
}

export default TicketForm;