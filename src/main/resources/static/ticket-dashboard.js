
let editingTicketId = null;

const severityClasses = {
    "SEV1": "severity-sev1",
    "SEV2": "severity-sev2",
    "SEV3": "severity-sev3",
    "SEV4": "severity-sev4"
};

const statusClasses = {
    "OPEN": "status-open",
    "RESOLVED": "status-resolved"
};

let allTickets = [];

async function buildTable() {
    const ticketAPI = "/api/tickets";
    const response = await fetch(ticketAPI);
    allTickets  = await response.json();

    updateTicketView();

}

function createTicketRow(ticket) {
    const tRow = document.createElement("tr");

    const tHeader = document.createElement("th");
    tHeader.scope = "row";
    tHeader.textContent = ticket.title;
    tRow.appendChild(tHeader);

    const tDescription = document.createElement("td");
    tDescription.textContent = ticket.description;
    tRow.appendChild(tDescription);

    const tSeverity = document.createElement("td");
    const severityBadge = document.createElement("span");
    severityBadge.textContent = ticket.severity;

    severityBadge.classList.add("badge");
    severityBadge.classList.add("badge-pill");
    severityBadge.classList.add(severityClasses[ticket.severity]);

    tSeverity.appendChild(severityBadge);
    tRow.appendChild(tSeverity);

    const createdDate = new Date(ticket["timestamp"]);
    const formattedDate = createdDate.toLocaleString("en-US", {
        month: "numeric",
        day: "numeric",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
        hourCycle: "h23",
        timeZoneName: "short"
    });

    const tCreatedDate = document.createElement("td");
    tCreatedDate.textContent = formattedDate;
    tRow.appendChild(tCreatedDate);

    const tStatus = document.createElement("td");
    const statusBadge = document.createElement("span");
    statusBadge.textContent = ticket.status;

    statusBadge.classList.add("badge");
    statusBadge.classList.add("badge-pill");
    statusBadge.classList.add(statusClasses[ticket.status]);

    tStatus.appendChild(statusBadge);
    tRow.appendChild(tStatus);

    const editButton = document.createElement("button");
    editButton.type = "button";
    editButton.className = "btn btn-primary";
    editButton.id = `edit-btn-${ticket.id}`;
    editButton.textContent = "Edit";
    tRow.appendChild(editButton);

    editButton.addEventListener("click", () => {
        openEditModal(ticket);
    });

    return tRow;
}

function updateTicketView() {

    const ticketHeadingText = document.getElementById("ticketHeadingText");

    const openIsChecked = document.getElementById("openTicketFilter").checked;
    const resolvedIsChecked = document.getElementById("resolvedTicketFilter").checked;

    const openTicketCount = document.getElementById("openTicketCount");
    const resolvedTicketCount = document.getElementById("resolvedTicketCount");

    const openTickets = allTickets.filter(
        ticket => ticket.status === "OPEN"
    );

    const resolvedTickets = allTickets.filter(
        ticket => ticket.status === "RESOLVED"
    );

    openTicketCount.textContent = openTickets.length;
    resolvedTicketCount.textContent = resolvedTickets.length;

    const displayTickets = allTickets.filter(ticket =>
        (openIsChecked && ticket.status === "OPEN") ||
        (resolvedIsChecked && ticket.status === "RESOLVED")
    );

    if (openIsChecked && resolvedIsChecked) {
        ticketHeadingText.textContent = "Open & Resolved Tickets"

    } else if (openIsChecked) {
        ticketHeadingText.textContent = "Open Tickets";

    } else if (resolvedIsChecked) {
        ticketHeadingText.textContent = "Resolved Tickets";
    } else {
        ticketHeadingText.textContent = "No Ticket Statuses Selected"
    }

    const tBody = document.getElementById("ticket-table-body");
    tBody.innerHTML = "";

    for (const ticket of displayTickets) {
        const tRow = createTicketRow(ticket);
        tBody.appendChild(tRow);
    }
}

    function openEditModal(ticket) {
        $("#myModal").modal("show");

        const titleModal = document.getElementById("title-modal");
        const descriptionModal = document.getElementById("description-modal");
        const severityModal = document.getElementById("severity-modal");

        titleModal.value = ticket["title"];
        descriptionModal.value = ticket["description"];
        severityModal.value = ticket["severity"];

        editingTicketId = ticket.id;
    }

    async function handleCreateTicketSubmit(event) {
        event.preventDefault();

        const title = document.getElementById("title").value;
        const description = document.getElementById("description").value;
        const severity = document.getElementById("severity").value;

        const ticketData = {
            "title": title,
            "description": description,
            "severity": severity
        };

        const response = await fetch("/api/tickets", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(ticketData)
        });

        console.log(response.status);

        if (response.status === 201) {
            await buildTable();
        }
    }

    async function handleEditTicketSubmit(event) {
        event.preventDefault();

        const titleModal = document.getElementById("title-modal").value;
        const descriptionModal = document.getElementById("description-modal").value;
        const severityModal = document.getElementById("severity-modal").value;
        const statusModal = document.getElementById("status-modal").value;

        const ticketData = {
            "title": titleModal,
            "description": descriptionModal,
            "severity": severityModal,
            "status": statusModal
        };

        const response = await fetch(`/api/tickets/${editingTicketId}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(ticketData)
        });

        console.log(response.status);

        if (response.status === 200) {
            $("#myModal").modal("hide");
            await buildTable();
        }
    }

    buildTable();

    const form = document.getElementById("create-ticket-form");
    form.addEventListener("submit", handleCreateTicketSubmit);

    const editForm = document.getElementById("edit-ticket-form");
    editForm.addEventListener("submit", handleEditTicketSubmit);


    document.getElementById("openTicketFilter").addEventListener("change", updateTicketView);
    document.getElementById("resolvedTicketFilter").addEventListener("change", updateTicketView);

