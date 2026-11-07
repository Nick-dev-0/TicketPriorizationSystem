export type SystemHealthStatus = "UP" | "DOWN" | "OUT_OF_SERVICE" | "UNKNOWN";

export interface SystemHealthResponse {
    status: SystemHealthStatus;
}