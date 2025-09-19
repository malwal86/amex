# User Service (Java · Vert.x · Gradle)

Google Java Style. In-memory, thread-safe CRUD. Vert.x Web API. Dagger DI. Tests. Docker.  
**Notes:** no logging; no external config files (constants only); no Lombok; user IDs are **UUIDs**.

## Overview
A simple REST API that manages `User` resources (create, read, update email, delete).  
Input is validated; errors are mapped to meaningful HTTP responses.

## Architecture
- **ApplicationVerticle** – application entry point; boots DI and deploys HTTP verticle.
- **HttpVerticle** – creates the HTTP server, registers routes, applies validation & error handling.
- **…Handler** – request handlers acting as “controllers” (e.g., `CreateUserHandler`).
- **UserService** – business logic.
- **UserDao** – data access (in-memory).
- **UserErrorHandler** – centralized error → HTTP response mapping. (ControllerAdvice)
- **Validator** – small utility for basic validations.

> For simplicity, there is **no worker verticle** for `UserService`. Blocking code is executed via Vert.x `blockingHandler(...)` on worker threads rather than using the event-bus 
> communicate back and forth with the HttpVerticle.

## Testing
A single **integration test** covers the HTTP surface. There are no external components, but it’s not a strict *unit* test (no mocks), to keep scope focused on the end-to-end behavior.

## Build & Test

### Testing
A single integration test covers the HTTP surface. There are no external components, but it’s not a strict 
unit test (no mocks), to keep scope focused on the end-to-end behavior.

```bash
./gradlew test
```

### Run locally


```bash
./gradlew clean build
./gradlew run
```

Server listens on http://localhost:8888 (override with env PORT).


### cURL Examples


```bash

# Create
curl -i -X POST http://localhost:8888/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"John Doe","email":"john.doe@example.com"}'

# Get by id
curl -i http://localhost:8888/v1/users/{id}

# Update email
curl -i -X PATCH http://localhost:8888/v1/users/{id} \
  -H 'Content-Type: application/json' \
  -d '{"email":"john.doe.updated@example.com"}'

# Delete
curl -i -X DELETE http://localhost:8888/v1/users/{id}

# Not found (example UUID)
curl -i http://localhost:8888/v1/users/08fc9514-f278-4fc6-a6c0-a6c2742a957c

```


## Docker


```bash
# Build image
docker build -t amex-assessment .

# Run (host 8888 → container 8888)
docker run --rm -p 8888:8888 amex-assessment

```