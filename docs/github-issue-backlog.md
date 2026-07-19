# GitHub Issue Backlog

This backlog turns the repository improvement roadmap into ready-to-copy GitHub issues. Each issue includes a suggested title, labels, background, scope, and acceptance criteria.

## 1. Add Docker Compose for local MongoDB development

**Labels:** `enhancement`, `documentation`, `good first issue`

### Problem

New contributors currently need to install and configure MongoDB manually before running the API locally. A Docker Compose setup would make local development faster and more consistent.

### Proposed solution

Add a `docker-compose.yml` file that starts MongoDB for local development, update `.env.example` if needed, and document the startup flow in `README.md`.

### Acceptance criteria

- [ ] Add `docker-compose.yml` with a MongoDB service.
- [ ] Persist MongoDB data with a named Docker volume.
- [ ] Expose MongoDB on the expected local port.
- [ ] Update `README.md` with local setup commands.
- [ ] Verify the app can connect using `MONGODB_URI=mongodb://localhost:27017/rest_tutorial`.

---

## 2. Add OpenAPI/Swagger API documentation

**Labels:** `enhancement`, `documentation`, `api`

### Problem

The README documents API testing mostly through screenshots. Interactive OpenAPI documentation would make the endpoints easier to discover and test.

### Proposed solution

Add Springdoc OpenAPI support and document the auth and pet endpoints with summaries, request examples, and response examples.

### Acceptance criteria

- [ ] Add the Springdoc OpenAPI dependency.
- [ ] Enable Swagger UI for local development.
- [ ] Document `/api/auth/signup`, `/api/auth/login`, and `/api/auth/logout`.
- [ ] Document all pet CRUD endpoints.
- [ ] Document Bearer token authentication.
- [ ] Add a README section that links to Swagger UI.

---

## 3. Add API versioning and normalize route paths

**Labels:** `enhancement`, `api`, `breaking-change`

### Problem

Auth endpoints are under `/api/auth`, while pet endpoints are under `/pets`. A consistent versioned API path will make the project easier to evolve.

### Proposed solution

Move public API routes under `/api/v1`, while considering backward-compatible redirects or clear migration notes.

### Acceptance criteria

- [ ] Move auth endpoints to `/api/v1/auth`.
- [ ] Move pet endpoints to `/api/v1/pets`.
- [ ] Update tests to use the new paths.
- [ ] Update README examples and screenshots/links as needed.
- [ ] Document whether old paths remain supported or are intentionally removed.

---

## 4. Add pagination, sorting, and filtering to pet listing

**Labels:** `enhancement`, `api`, `database`

### Problem

`GET /pets/` returns every pet at once, which will not scale as the collection grows.

### Proposed solution

Update the pet listing endpoint to support pagination, sorting, and optional filters such as species and breed.

### Acceptance criteria

- [ ] Support `page`, `size`, and `sort` query parameters.
- [ ] Support filtering by `species`.
- [ ] Support filtering by `breed`.
- [ ] Return pagination metadata in the response.
- [ ] Add service and controller tests for pagination and filters.
- [ ] Update API documentation and README examples.

---

## 5. Improve test coverage toward 80%+

**Labels:** `testing`, `quality`, `enhancement`

### Problem

The project roadmap includes a goal for 80%+ code coverage. Existing tests cover core pet service behavior, but auth, JWT, validation, and error handling need more coverage.

### Proposed solution

Add focused unit and MVC tests for security, authentication, validation, and global error responses. Consider enforcing a coverage threshold once coverage is stable.

### Acceptance criteria

- [ ] Add tests for successful signup and login.
- [ ] Add tests for duplicate username/email signup.
- [ ] Add tests for logout with missing, malformed, invalid, and valid tokens.
- [ ] Add tests for JWT generation, validation, expiration handling, and blacklisting.
- [ ] Add tests for validation error responses.
- [ ] Generate a Jacoco report and document current coverage.
- [ ] Optionally enforce a minimum coverage threshold in Gradle.

---

## 6. Add MongoDB integration tests with Testcontainers

**Labels:** `testing`, `database`, `enhancement`

### Problem

Mock-based tests are useful, but they do not verify repository behavior or MongoDB integration.

### Proposed solution

Use Testcontainers to run MongoDB during integration tests and validate the application against a real database.

### Acceptance criteria

- [ ] Add Testcontainers dependencies for JUnit Jupiter and MongoDB.
- [ ] Add integration tests for pet repository/service flows.
- [ ] Add integration tests for user signup persistence and role lookup.
- [ ] Ensure tests are isolated and clean up their own data.
- [ ] Document Docker requirements for running integration tests.

---

## 7. Replace in-memory JWT blacklist with a production-ready store

**Labels:** `security`, `enhancement`, `architecture`

### Problem

Token invalidation currently uses an in-memory blacklist, which does not work reliably across multiple application instances and is lost on restart.

### Proposed solution

Introduce a token blacklist abstraction and provide a Redis-backed implementation for production deployments.

### Acceptance criteria

- [ ] Add a token blacklist service interface.
- [ ] Move in-memory blacklist behavior behind that interface.
- [ ] Add a Redis-backed implementation or document a clear extension point.
- [ ] Configure implementation selection by Spring profile or property.
- [ ] Add tests for token invalidation and expiration cleanup.
- [ ] Update production deployment documentation.

---

## 8. Add refresh token support

**Labels:** `security`, `enhancement`, `api`

### Problem

The current authentication flow returns only a JWT access token. Short-lived access tokens plus refresh tokens would provide a more production-ready authentication flow.

### Proposed solution

Add refresh token generation, storage, rotation, and revocation endpoints.

### Acceptance criteria

- [ ] Return an access token and refresh token from login.
- [ ] Add a refresh endpoint to issue a new access token.
- [ ] Store refresh tokens securely with expiration metadata.
- [ ] Support refresh token revocation during logout.
- [ ] Add tests for successful refresh, expired refresh tokens, and revoked refresh tokens.
- [ ] Update API documentation.

---

## 9. Add Spring Boot Actuator health checks and observability basics

**Labels:** `operations`, `enhancement`, `monitoring`

### Problem

The API does not expose standardized health or readiness endpoints for deployment platforms.

### Proposed solution

Add Spring Boot Actuator and configure health endpoints, basic application info, and safe metrics exposure.

### Acceptance criteria

- [ ] Add the Spring Boot Actuator dependency.
- [ ] Expose health and info endpoints.
- [ ] Configure liveness and readiness probes.
- [ ] Ensure sensitive actuator endpoints are not publicly exposed.
- [ ] Add README documentation for health checks.
- [ ] Add tests or smoke checks where practical.

---

## 10. Modernize CI with GitHub Actions

**Labels:** `ci`, `quality`, `enhancement`

### Problem

The project documentation references older CI services. A GitHub Actions workflow can provide a current, contributor-friendly validation path.

### Proposed solution

Add or improve GitHub Actions workflows for build, tests, coverage reports, and optional static analysis.

### Acceptance criteria

- [ ] Run `./gradlew test` on pull requests.
- [ ] Generate Jacoco reports in CI.
- [ ] Upload coverage reports as workflow artifacts or to a coverage provider.
- [ ] Cache Gradle dependencies.
- [ ] Add status badge documentation to README.
- [ ] Remove or mark stale CI badge references if they no longer apply.

---

## 11. Add user profile and admin user-management endpoints

**Labels:** `enhancement`, `security`, `api`

### Problem

The API supports signup/login and role-based pet access, but there are no endpoints for users to inspect their own account or for admins to manage users.

### Proposed solution

Add user profile and admin user-management APIs with appropriate role protections.

### Acceptance criteria

- [ ] Add `GET /api/v1/users/me` for authenticated users.
- [ ] Add admin-only user listing endpoint.
- [ ] Add admin-only user role update endpoint.
- [ ] Add admin-only user disable/delete endpoint if appropriate.
- [ ] Add authorization tests for user and admin scenarios.
- [ ] Document endpoints in OpenAPI/README.

---

## 12. Add richer pet domain fields and audit timestamps

**Labels:** `enhancement`, `database`, `api`

### Problem

Pets currently only have name, species, and breed. More realistic data would make the API a stronger template and demo application.

### Proposed solution

Extend the pet model with optional metadata and audit timestamps.

### Acceptance criteria

- [ ] Add fields such as age, color, weight, sex, adoption status, and image URL where appropriate.
- [ ] Add `createdAt` and `updatedAt` timestamps.
- [ ] Update create/update request DTOs and response DTOs.
- [ ] Add validation constraints for new fields.
- [ ] Add migration or backward-compatibility notes for existing MongoDB documents.
- [ ] Update tests and documentation.
