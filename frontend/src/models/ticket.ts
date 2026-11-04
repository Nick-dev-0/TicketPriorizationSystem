export type SeverityLevel = "SEV1" | "SEV2" | "SEV3" | "SEV4";

export type TicketStatus = "OPEN" | "RESOLVED";

export interface Ticket {
    id: string;
    title: string;
    description: string;
    severity: SeverityLevel;
    timestamp: string;
    status: TicketStatus;
    expiresAt: string | null;
}

export interface CreateTicketRequest {
    title: string;
    description: string;
    severity: SeverityLevel;
    status?: TicketStatus;
}