import { useState } from "react";
import {getSeveritySuggestion} from "../services/ticketService.ts";
import type { SeveritySuggestionResponse } from "../models/severity-suggestion";

function TicketForm() {

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [suggestion, setSuggestion] = useState<SeveritySuggestionResponse | null>(null);
    const [isSuggesting, setIsSuggesting] = useState(false);

    return (
        <section>
            <h2>Create Ticket</h2>

            <form id="create-ticket-form">

                <div>
                    <label htmlFor="title">Title</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(event) => setTitle(event.target.value)}
                        id="title"
                        name="title"
                        placeholder="Write the ticket title..."
                        required
                    />
                </div>

                <div>
                    <label htmlFor="description">Description</label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(event) => setDescription(event.target.value)}
                        name="description"
                        rows={3}
                        placeholder="Write the ticket description..."
                        required
                    />
                </div>

                <div>
                    <div>
                        <label htmlFor="severity">Severity</label>

                        <button type="button" onClick={handleSuggestion} disabled={isSuggesting}>
                            {isSuggesting ? "Awaiting..." : "AI Suggestion"}
                        </button>
                    </div>

                    <select
                        id="severity"
                        name="severity"
                        required
                    >
                        <option value="SEV1">SEV1</option>
                        <option value="SEV2">SEV2</option>
                        <option value="SEV3">SEV3</option>
                        <option value="SEV4">SEV4</option>
                    </select>

                    {suggestion && (
                        <div>
                            <h4>
                                AI Recommended Severity:
                                <span>{suggestion.suggestedSeverity}</span>
                            </h4>

                            <p>{suggestion.reasoning}</p>

                            <button type="button">
                                Use Recommendation
                            </button>
                        </div>
                    )}
                </div>

                <button type="submit">
                    Create Ticket
                </button>

            </form>
        </section>
    );

    async function handleSuggestion() {
        setIsSuggesting(true);

        try {
            const suggestion = await getSeveritySuggestion({
                title,
                description
            });

            setSuggestion(suggestion);
        } finally {
            setIsSuggesting(false);
        }
    }
}
export default TicketForm;
