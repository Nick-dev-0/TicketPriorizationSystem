# Ticket Prioritization System

A support ticket tracker built with Java and Spring Boot. It has a REST API, a browser dashboard for creating and editing tickets, and an AI suggestion that recommends a severity level for a new ticket. I built it to get more practice with Spring Boot, working with a database, and connecting a plain JavaScript frontend to backend endpoints.

**Live demo:** [ticketpriorizationsystem.onrender.com](https://ticketpriorizationsystem.onrender.com/ticket-dashboard.html)

The demo can take about a minute to start if nobody has used it recently. It is also public, so everyone sees the same tickets, and new tickets are deleted after 24 hours. Please don't enter anything private.

## What it does

- Create tickets with a title, description, and severity from `SEV1` (most severe) to `SEV4`.
- List tickets sorted by severity, then by creation time with the oldest first.
- Edit a ticket's title, description, severity, and status (`OPEN` or `RESOLVED`) from the dashboard.
- Filter the dashboard by open and resolved tickets, with a count for each.
- Reject invalid requests with a `400` response, for example a blank title or a missing severity.
- Store tickets in MongoDB.
- Suggest a severity from a ticket's title and description using the OpenAI API.

## AI severity suggestion

The **AI Suggestion** button on the create form sends the title and description to `POST /api/severity-suggestions`. The dashboard shows the recommended severity with a short explanation, and **Use Recommendation** copies it into the severity dropdown. Nothing is saved until the ticket is submitted, so the final choice is the user's. The model can get a severity wrong, which is why it only suggests.

How it works:

- `SeveritySuggestionService` calls the OpenAI Responses API through the OpenAI Java SDK.
- The prompt defines each severity level and includes five example tickets.
- The reply is requested as structured output and parsed straight into a Java object with a `suggestedSeverity` and a `reasoning` field.
- The ticket text is passed to the model as data, and the prompt tells it not to follow instructions that appear inside a ticket.

## Built with

- Java 25
- Spring Boot 4.1.2 (Spring MVC, Spring Data MongoDB, Jakarta Bean Validation)
- MongoDB, using MongoDB Atlas for the live demo
- OpenAI Java SDK 4.94.0
- HTML, CSS, JavaScript, and Bootstrap 4
- Maven
- JUnit 5, Mockito, and MockMvc
- Docker and Render

## Run it locally

You will need JDK 25, Maven, a MongoDB database (a free MongoDB Atlas cluster works), and an OpenAI API key.

Clone the repository:

```bash
git clone https://github.com/Nick-dev-0/TicketPriorizationSystem.git
cd TicketPriorizationSystem
```

Create a `.env` file in the project root:

```properties
MONGODB_URI=mongodb+srv://<username>:<password>@<cluster-url>/?retryWrites=true&w=majority
OPENAI_API_KEY=your_openai_api_key
```

`MONGODB_URI` and `OPENAI_API_KEY` have no defaults, so both need to be set for the app to start. You can also set them as normal environment variables instead of using a `.env` file. Keep your `.env` private. It is already listed in `.gitignore`.

These settings are optional:

| Variable | Default | Purpose |
| --- | --- | --- |
| `MONGODB_DATABASE` | `ticket_system` | Name of the MongoDB database. |
| `OPENAI_MODEL` | Set in `application.properties` | Model used for severity suggestions. |
| `APP_DEMO_MODE` | `false` | Gives new tickets an expiry time. See [Demo mode](#demo-mode). |
| `PORT` | `8080` | Port the app listens on. |

Start the app:

```bash
mvn spring-boot:run
```

Then open [http://localhost:8080](http://localhost:8080), which redirects to the dashboard.

## API

| Method | Endpoint | Purpose | Main responses |
| --- | --- | --- | --- |
| `GET` | `/api/tickets` | List tickets by severity, then creation time. | `200` |
| `GET` | `/api/tickets/{id}` | Get one ticket. | `200`, `404` |
| `POST` | `/api/tickets` | Create a ticket. | `201`, `400` |
| `PATCH` | `/api/tickets/{id}` | Update a ticket. | `200`, `400`, `404` |
| `POST` | `/api/severity-suggestions` | Get a suggested severity without creating a ticket. | `200`, `400` |

### Create a ticket

```json
{
  "title": "Login page is unavailable",
  "description": "Users are receiving an error when trying to sign in.",
  "severity": "SEV2"
}
```

New tickets start as `OPEN`. Valid severities are `SEV1`, `SEV2`, `SEV3`, and `SEV4`.

### Update a ticket

`PATCH /api/tickets/{id}` replaces the title, description, severity, and status, so send all four. If `status` is left out, it is treated as `OPEN`.

```json
{
  "title": "Login page is unavailable",
  "description": "Users are receiving an error when trying to sign in.",
  "severity": "SEV2",
  "status": "RESOLVED"
}
```

### Get a severity suggestion

```json
{
  "title": "Customers unable to complete purchases",
  "description": "Customers are receiving errors when attempting to check out."
}
```

The response has the suggested severity and the model's reasoning:

```json
{
  "suggestedSeverity": "SEV1",
  "reasoning": "Checkout errors prevent customers from completing purchases, affecting a core business function."
}
```

## Demo mode

The live demo runs with `APP_DEMO_MODE=true`, which is set in Render's environment variables. In this mode, each ticket a visitor creates gets an `expiresAt` timestamp 24 hours after it was created, and a MongoDB TTL index on that field deletes it. Locally, `APP_DEMO_MODE` defaults to `false`, so tickets have no expiry and are never removed.

The app only sets the timestamp and does not create the index. If you run demo mode yourself, create the TTL index in the `app_tickets` collection:

```js
use ticket_system

db.app_tickets.createIndex(
    { expiresAt: 1 },
    { expireAfterSeconds: 0 }
)
```

## Tests

```bash
mvn clean test
```

There are 15 tests, five each for the service, repository, and controller layers:

- Service tests use a mocked repository (Mockito).
- Repository tests use `@DataMongoTest` and run against `ticket_system_test`, which they clear before each test.
- Controller tests use `@SpringBootTest` with MockMvc and cover validation, `201` and `200` responses, and missing tickets.

The tests connect to MongoDB using the same `MONGODB_URI`. The controller tests create tickets in whatever database `MONGODB_DATABASE` points to, so use a test database and not the one behind a live deployment. There are no tests yet for the AI suggestion endpoint or for demo mode expiry.

## Docker and deployment

The `Dockerfile` has two stages. The first builds the jar with Maven on a JDK 25 image, and the second runs it on a smaller JRE 25 image.

```bash
docker build -t ticket-prioritization-system .
docker run -p 8080:8080 --env-file .env ticket-prioritization-system
```

The `.env` file is not copied into the image, so the settings have to be passed in when the container starts. The live demo runs on Render, which provides them as environment variables instead.

## Known limitations

- The update endpoint needs the full ticket in the request body.
- There is no login, so anyone can edit tickets in the public demo.
- The AI suggestion and demo mode expiry need tests.
