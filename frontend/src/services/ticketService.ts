import type { SeveritySuggestionRequest, SeveritySuggestionResponse } from "../models/severity-suggestion";
import type {Ticket, CreateTicketRequest, UpdateTicketRequest} from "../models/ticket";

import { API_BASE_URL } from "./apiConfig";

export async function getTickets(): Promise<Ticket[]> {

    const response = await fetch(`${API_BASE_URL}/api/tickets`);

    if (!response.ok) {
        throw new Error("Failed to retrieve tickets");
    }

    return (await response.json()) as Ticket[];
}

export async function createTicket(ticketData: CreateTicketRequest): Promise<Ticket> {

    const response = await fetch(`${API_BASE_URL}/api/tickets`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(ticketData)
    });

    if (!response.ok) {
        throw new Error("Failed to create ticket");
    }

    return (await response.json()) as Ticket;
}

export async function getSeveritySuggestion(request: SeveritySuggestionRequest): Promise<SeveritySuggestionResponse> {

    const response = await fetch(`${API_BASE_URL}/api/severity-suggestions`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    if (!response.ok) {
        throw new Error("Failed to get severity suggestion");
    }

    return (await response.json()) as SeveritySuggestionResponse;
}

export async function updateTicket(id: string, ticketData: UpdateTicketRequest): Promise<Ticket> {

    const response = await fetch(`${API_BASE_URL}/api/tickets/${id}`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(ticketData)
    });

    if (!response.ok) {
        throw new Error("Failed to update ticket.");
    }

    return (await response.json()) as Ticket;
}

export async function deleteTicket(id: string): Promise<void> {

    const response = await fetch(`${API_BASE_URL}/api/tickets/${id}`, {
        method: "DELETE"
    });

    if (!response.ok) {
        throw new Error("Failed to delete ticket.");
    }
}