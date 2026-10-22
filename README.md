# Threat Intelligence Platform

A full-stack web app for tracking and managing cybersecurity threats. Add threats, set their severity, category and status, then search and filter them from a dashboard.

**Stack:** React (Vite) · Spring Boot 3 (Java 17) · Spring Data JPA · MySQL (H2 for local dev) · Docker Compose · GitHub Actions

## Features

- Create, view, edit and delete threats (REST API + dashboard)
- Search by text and filter by severity, category and status
- Summary cards (total, per severity, active threats)
- Input validation with clear error messages (400 for bad input, 404 for unknown IDs)
- Interactive API docs (Swagger UI)
- Automated API tests (JUnit + MockMvc) and a CI workflow that runs tests and builds the frontend
- One-command run with Docker Compose

## Architecture

```
React dashboard  ->  nginx (/api proxy)  ->  Spring Boot REST API  ->  MySQL
```

The backend is layered: `controller` (HTTP) -> `service` (business logic, search) -> `repository` (JPA) -> database.

## Project structure

```
backend/    Spring Boot API (controller, service, repository, model, tests)
frontend/   React + Vite dashboard
docker-compose.yml
.github/workflows/ci.yml
```

## Run with Docker (recommended)

Requires Docker Desktop.

```bash
git clone https://github.com/Manu152023/threat-intelligence.git
cd threat-intelligence
docker-compose up --build
```

- App: http://localhost:3000
- API: http://localhost:8080/api/threats
- Swagger UI: http://localhost:8080/swagger-ui.html

Optional: copy `.env.example` to `.env` to set your own MySQL password.

## Run without Docker

Backend (Java 17 and Maven; uses in-memory H2, no database setup):

```bash
cd backend
mvn spring-boot:run
```

Frontend (Node 18+), in a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. Sample data is added automatically on first start.

## API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/threats` | List threats. Optional query params: `q`, `severity`, `category`, `status` |
| GET | `/api/threats/{id}` | Get one threat |
| GET | `/api/threats/stats` | Counts by severity and status |
| POST | `/api/threats` | Create a threat |
| PUT | `/api/threats/{id}` | Update a threat |
| DELETE | `/api/threats/{id}` | Delete a threat |

Example body:

```json
{
  "title": "Phishing wave impersonating payroll portals",
  "description": "Credential-harvesting emails targeting HR departments.",
  "severity": "MEDIUM",
  "category": "PHISHING",
  "status": "INVESTIGATING",
  "source": "CISA"
}
```

Severity: `CRITICAL | HIGH | MEDIUM | LOW`. Status: `ACTIVE | INVESTIGATING | MITIGATED | RESOLVED`. Category: `MALWARE | PHISHING | RANSOMWARE | DDOS | VULNERABILITY | DATA_BREACH | OTHER`.

## Tests

```bash
cd backend
mvn test
```

## Roadmap

Not built yet: authentication (JWT), pagination, role-based access, cloud deployment.
