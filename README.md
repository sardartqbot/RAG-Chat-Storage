# RAG Chat Storage - Spring Boot

This is a sample Spring Boot microservice that stores chat sessions and messages for a RAG-based chatbot.

## Quick start (Docker)
1. Copy `.env.example` to `.env` and set `API_KEY`.
2. Build jar: `mvn clean package -DskipTests`
3. `docker compose up --build`
4. API: `http://localhost:8080/api/v1`
   - Swagger: `http://localhost:8080/swagger-ui/index.html`
   - Adminer: `http://localhost:8081`

## Endpoints
- `POST /api/v1/sessions` – create session
- `PATCH /api/v1/sessions/{id}/rename` – rename
- `PATCH /api/v1/sessions/{id}/favorite` – set favorite
- `DELETE /api/v1/sessions/{id}` – delete session
- `POST /api/v1/sessions/{sessionId}/messages` – add message
- `GET /api/v1/sessions/{sessionId}/messages` – list messages (pagination)
- `GET /api/v1/health` – health

## Notes
- Use `x-api-key` header for all requests (except health & swagger).
- This project uses a simple in-memory rate limiter for demo. Replace with Redis-backed limiter in prod.
