import type { SeverityLevel } from "./ticket";

export interface SeveritySuggestionRequest {
    title: string;
    description: string;
}

export interface SeveritySuggestionResponse {
    suggestedSeverity: SeverityLevel;
    reasoning: string;
}