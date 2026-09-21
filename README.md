# RestAPI Template — Spring Boot 3 · Java 21 · MongoDB · JWT

> **A production-ready REST API starter** — fork it, swap the domain, and ship. Built on the latest stack so you don't start from scratch.

[![CI](https://github.com/GouravRusiya30/SpringBootRestAPI/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/GouravRusiya30/SpringBootRestAPI/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/GouravRusiya30/SpringBootRestAPI/branch/master/graph/badge.svg)](https://codecov.io/gh/GouravRusiya30/SpringBootRestAPI)
[![sonar](https://sonarcloud.io/api/project_badges/measure?project=GouravRusiya30_SpringBootRestAPI&metric=alert_status)](https://sonarcloud.io/dashboard?id=GouravRusiya30_SpringBootRestAPI)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/LICENSE)

[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/GouravRusiya30/SpringBootRestAPI/issues)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/CONTRIBUTING.md)
[![first-timers-only-friendly](http://img.shields.io/badge/first--timers--only-friendly-blue.svg?style=flat-square)](https://code.publiclab.org#r=all)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)

<img height="260px" src="https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/docs/spring.png">

---

## What's Inside

A fully-wired REST API template demonstrating:

- **Spring Boot 3.3** · **Java 21** · **MongoDB**
- **Stateless JWT authentication** — signup, login, logout with in-memory token blacklist
- **Role-based access control** — `ROLE_USER`, `ROLE_MODERATOR`, `ROLE_ADMIN` via `@PreAuthorize`
- **Pagination & filtering** on list endpoints
- **Structured error responses** — consistent JSON on every error (4xx / 5xx)
- **OpenAPI / Swagger UI** — auto-generated, browsable at `/swagger-ui.html`
- **Spring Boot Actuator** — `/actuator/health` and `/actuator/info`
- **JaCoCo** coverage reports + **SonarCloud** static analysis
- **GitHub Actions** CI — builds, tests, and uploads coverage on every push/PR
- **Docker Compose** for local MongoDB

---

## Task List Progress

- [x] REST controllers and models with Spring Boot
- [x] MongoDB configuration
- [x] JWT authentication (signup / login / logout)
- [x] Role-based access control
- [x] Pagination and filtering on list endpoints
- [x] OpenAPI / Swagger UI
- [x] GitHub Actions CI pipeline
- [x] SonarCloud integration
- [x] JaCoCo test coverage report + Codecov upload
- [ ] 80 %+ code coverage
- [ ] Cloud deployment (Render / Railway)

---

## API Reference

### Auth — `/api/auth/**` (no token required)

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/auth/signup` | Register a new user. Body: `username`, `email`, `password`, optional `roles` array (`"admin"`, `"mod"`). |
| `POST` | `/api/auth/login` | Authenticate. Returns a JWT in `JwtResponse`. |
| `POST` | `/api/auth/logout` | Invalidate the current JWT. Requires `Authorization: Bearer <token>`. |

### Pets — `/pets/**` (JWT required)

| Method | Path | Role | Description |
|--------|------|------|-------------|
| `GET` | `/pets/` | USER / MOD / ADMIN | Paginated list. Query params: `page`, `size`, `sortBy`, `species`, `adoptionStatus`. |
| `GET` | `/pets/{id}` | USER / MOD / ADMIN | Get a single pet by ID. |
| `POST` | `/pets/` | USER / MOD / ADMIN | Create a pet. Body: `name`, `species`, `breed` (required) + `age`, `color`, `adoptionStatus`. |
| `PUT` | `/pets/{id}` | MOD / ADMIN | Full update of a pet. |
| `DELETE` | `/pets/{id}` | ADMIN | Delete a pet. Returns `204 No Content`. |

### Infrastructure (no token required)

| Path | Description |
|------|-------------|
| `/swagger-ui.html` | Interactive API explorer |
| `/v3/api-docs` | Raw OpenAPI JSON spec |
| `/actuator/health` | Application health status |
| `/actuator/info` | App name and version |

---

## Getting Started

### Prerequisites

- **Java 21** (e.g. [Temurin](https://adoptium.net/))
- **Docker & Docker Compose** — for the recommended local MongoDB setup
- **Gradle** (wrapper included — no separate install needed)

### 1 — Clone & configure

```bash
git clone https://github.com/GouravRusiya30/SpringBootRestAPI.git
cd SpringBootRestAPI
cp .env.example .env        # edit the values for your machine
```

Key variables in `.env`:

| Variable | Description |
|----------|-------------|
| `SERVER_PORT` | Port the app listens on (default `8080`) |
| `MONGODB_URI` | MongoDB connection string |
| `JWT_SECRET` | HS512 signing secret — **minimum 64 characters** |
| `JWT_EXPIRATION_MS` | Token TTL in milliseconds (e.g. `3600000` = 1 h) |

### 2 — Start MongoDB

```bash
docker compose up -d mongodb
```

This starts MongoDB 7 on `localhost:27017` and persists data in the `mongodb_data` Docker volume.

### 3 — Seed the database

The `local` Spring profile auto-seeds roles and sample pets on first startup. To activate it:

```bash
# Linux / macOS
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun

# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE="local"; ./gradlew bootRun
```

Or seed manually in the MongoDB shell:

```js
// Roles (required before first signup)
db.roles.insertMany([
  { name: "ROLE_USER" },
  { name: "ROLE_MODERATOR" },
  { name: "ROLE_ADMIN" }
])

// Sample pets (optional)
db.pets.insertMany([
  { name: "Spot",  species: "dog", breed: "pitbull",           adoptionStatus: "AVAILABLE" },
  { name: "Daisy", species: "cat", breed: "calico",            adoptionStatus: "AVAILABLE" },
  { name: "Bella", species: "dog", breed: "australian shepard",adoptionStatus: "PENDING"   }
])
```

### 4 — Run the application

```bash
./gradlew bootRun
```

The API is now available at `http://localhost:8080`.
Open **`http://localhost:8080/swagger-ui.html`** to explore and try every endpoint interactively.

### 5 — Stop

```bash
docker compose down          # stop MongoDB, keep data
docker compose down -v       # stop MongoDB and delete data volume
```

---

## Running the Tests

```bash
./gradlew test
```

To also generate the JaCoCo HTML coverage report:

```bash
./gradlew test jacocoTestReport
# open build/reports/jacoco/test/html/index.html
```

The CI pipeline runs both steps automatically on every push and pull request to `master`.

---

## Try It — Quick Walkthrough

All examples use `curl`. You can do the same through the Swagger UI at `/swagger-ui.html`.

**1. Register a user**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"alice@example.com","password":"secret123","roles":["admin"]}'
```

**2. Login and capture the token**
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"secret123"}' | jq -r '.token')
```

**3. List pets (paginated)**
```bash
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/pets/?page=0&size=5&species=dog"
```

**4. Create a pet**
```bash
curl -X POST http://localhost:8080/pets/ \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Max","species":"dog","breed":"labrador","adoptionStatus":"AVAILABLE"}'
```

**5. Logout**
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer $TOKEN"
```

---

## Planned GitHub Issues

A ready-to-copy backlog of suggested GitHub issues is available in [docs/github-issue-backlog.md](docs/github-issue-backlog.md).

---

## Contributing

Please read [CONTRIBUTING.md](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/CODE_OF_CONDUCT.md) before opening a pull request.

Use the issue and PR templates in `.github/` — they keep reviews fast and focused.

---

## Authors

**Gourav Rusiya** — [@GouravRusiya30](https://github.com/GouravRusiya30)
