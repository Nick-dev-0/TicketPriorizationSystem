import { API_BASE_URL } from "./apiConfig";
import type { SystemHealthResponse } from "../models/system-health";

export async function getSystemHealth(): Promise<SystemHealthResponse> {

    const response = await fetch(`${API_BASE_URL}/actuator/health`);

    if (!response.ok) {
        throw new Error("Unable to retrieve system health.");
    }

    return (await response.json()) as SystemHealthResponse;
}