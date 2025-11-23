RAG Chat Storage - Spring Boot

This is a Spring Boot microservice responsible for storing chat sessions and messages for a RAG-based chatbot system.
It includes API-key security, rate limiting, CORS, PostgreSQL, Redis caching, and Swagger UI.

Quick Start (Docker)

Set API_KEY, DB credentials, Redis host, etc.

Build the JAR

mvn clean package -DskipTests


Start all services

docker compose up --build


Services will be available at:

API Base: http://localhost:8080/api/v1

Swagger UI: http://localhost:8080/swagger-ui/index.html

Adminer (DB UI): http://localhost:8081

Health Check: http://localhost:8080/api/v1/health

Endpoints
Sessions

POST /api/v1/sessions
Create a new chat session

PATCH /api/v1/sessions/{id}/rename
Rename a session

PATCH /api/v1/sessions/{id}/favorite
Mark/unmark as favorite

DELETE /api/v1/sessions/{id}
Delete a session

GET /api/v1/users/{userId}/sessions
List sessions for a specific user

Messages

POST /api/v1/sessions/{sessionId}/messages
Add a message to a session

GET /api/v1/sessions/{sessionId}/messages?page=0&size=20
Get paginated messages

Health

GET /api/v1/health

Security

All protected endpoints require an API key header:

X-API-KEY: your_api_key


Only the following endpoints are public:

/api/v1/health

/v3/api-docs/**

/swagger-ui/**

CORS origins can be configured via application.yml

app:
  cors:
    allowed-origins:
      - "http://localhost:3000"
      - "http://localhost:5173"

Environment Variables

All environment variables are defined in .env.example

Includes:

DATABASE_URL

DATABASE_USER

DATABASE_PASSWORD

REDIS_HOST

REDIS_PORT

API_KEY

DEV_MODE

CORS_ALLOWED_ORIGINS

Note: .env.example must be included in your submission.
Do not commit .env.

Notes

Use X-API-KEY for all requests except:

/health

Swagger UI and API documentation

The rate limiting filter currently uses a simple in-memory implementation.
For production, replace with a Redis + Bucket4j or Redis Cell implementation.

Project includes:

JUnit 5 + Mockito tests

Swagger-based API documentation

Docker setup (App + Postgres + Redis + Adminer)
