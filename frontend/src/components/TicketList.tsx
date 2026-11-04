import { useState } from "react";
import type { Ticket } from "../models/ticket";

function TicketList() {

    const [tickets] = useState<Ticket[]>([]);

    return (
        <div>
            <h2>Tickets</h2>
        <p>Number of tickets: {tickets.length}</p>
    </div>
);
}

export default TicketList;