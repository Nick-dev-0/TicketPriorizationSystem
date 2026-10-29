let editingTicketId = null;

async function buildTable() {
    const ticketAPI = "/api/tickets";
    const response = await fetch(ticketAPI);
    const data = await response.json();

    const openTickets = data.filter(ticket => ticket.status === "OPEN");

    const totalOpenTickets = openTickets.length;

    const openTicketCount = document.getElementById("openTicketCount");
    openTicketCount.textContent = totalOpenTickets;

    const tBody = document.getElementById("ticket-table-body");

    tBody.innerHTML = "";

    const severityClasses = {
        "SEV1": "severity-sev1",
        "SEV2": "severity-sev2",
        "SEV3": "severity-sev3",
        "SEV4": "severity-sev4"
    }

    const statusClasses = {
        "OPEN": "status-open",
        "RESOLVED": "status-resolved"
    }

    for (const ticket of data) {
        const tRow = document.createElement("tr");

        const tHeader = document.createElement("th");
        tHeader.scope = "row";
        tHeader.textContent = ticket.title;
        tRow.appendChild(tHeader);

        const tDescription = document.createElement("td");
        tDescription.textContent = ticket.description;
        tRow.appendChild(tDescription);

        const tSeverity = document.createElement("td");
        const span = document.createElement("span");
        span.textContent = ticket.severity;

        span.classList.add("badge");
        span.classList.add("badge-pill");

        const severityStyle = severityClasses[ticket.severity];
        span.classList.add(severityStyle);

        tSeverity.appendChild(span);
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
        const tcreatedDate = document.createElement("td");
        tcreatedDate.textContent = formattedDate;
        tRow.appendChild(tcreatedDate);

        const tStatus = document.createElement("td");
        const span1 = document.createElement("span");
        span1.textContent = ticket.status

        span1.classList.add("badge");
        span1.classList.add("badge-pill");

        const statusStyle = statusClasses[ticket.status];
        span1.classList.add(statusStyle);

        tStatus.appendChild(span1);
        tRow.appendChild(tStatus);

        const edit = document.createElement("button");
        edit.type = "button";
        edit.className = ("btn btn-primary");
        edit.id = `edit-btn-${ticket.id}`;
        edit.textContent = "Edit";
        tRow.appendChild(edit);

        edit.addEventListener("click", () => {
            $("#myModal").modal("show");

            const titleModal = document.getElementById("title-modal")
            const descriptionModal = document.getElementById("description-modal")
            const severityModal = document.getElementById("severity-modal")

            titleModal.value = ticket["title"];
            descriptionModal.value = ticket["description"];
            severityModal.value = ticket["severity"];

            editingTicketId = ticket.id;
        });

        tBody.appendChild(tRow);
    }
}

buildTable();

const form = document.getElementById("create-ticket-form");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const severity = document.getElementById("severity").value;

    const ticketData = {
        "title": title,
        "description": description,
        "severity": severity
    }

    const response = await fetch("/api/tickets", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(ticketData)
    })
    console.log(response.status);

    if (response.status === 201) {
        await buildTable();
    }
});

const editForm = document.getElementById("edit-ticket-form");
editForm.addEventListener("submit", async (event) => {
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
    }

    const response = await fetch(`/api/tickets/${editingTicketId}`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(ticketData)
    })
    console.log(response.status);

    if (response.status === 200) {
        $("#myModal").modal("hide");
        await buildTable();
    }
});
