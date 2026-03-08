# Task Management REST API

A RESTful backend built with **Spring Boot 4** and **Java 25**, developed as a portfolio project to demonstrate core backend engineering skills including JWT authentication, role-based authorization, clean architecture, and unit testing.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.0.3 |
| Security | Spring Security 7 + JWT (JJWT 0.11.5) |
| Database | MySQL 8 (JPA / Hibernate) |
| Docs | Swagger / OpenAPI 3 |
| Testing | JUnit 5 + Mockito |
| Build | Maven |
| Container | Docker + Docker Compose |

---

## Architecture

Clean layered architecture following SOLID principles:

```
src/main/java/com/example/taskmanagement/
├── auth/           # Registration & login (JWT)
├── task/           # Task CRUD with ownership
├── user/           # User management (ADMIN only)
├── security/       # JWT filter & UserDetailsService
├── config/         # SecurityConfig, OpenApiConfig
└── exception/      # GlobalExceptionHandler
```

Controllers depend on **interfaces**, not implementations (Dependency Inversion Principle).

---

## Features

- **JWT Authentication** — stateless auth via Bearer token
- **Role-based Authorization** — `ROLE_ADMIN` / `ROLE_USER` with `@PreAuthorize`
- **Task CRUD** — full CRUD with ownership enforcement (users can only access their own tasks)
- **Pagination & Filtering** — filter tasks by `status` and `priority`
- **Swagger UI** — interactive API docs with JWT support at `/swagger-ui.html`
- **Global Exception Handling** — consistent error responses (400, 401, 403, 404, 409)
- **SLF4J Logging** — structured logs across service layer and JWT filter
- **Unit Tests** — 11 test cases covering success paths, ownership checks, and error cases

---

## API Endpoints

### Auth (Public)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register new account, returns JWT |
| POST | `/auth/login` | Login, returns JWT |

### Tasks (Authenticated)
| Method | Endpoint | Access |
|---|---|---|
| GET | `/tasks` | USER sees own tasks, ADMIN sees all |
| POST | `/tasks` | Any authenticated user |
| GET | `/tasks/{id}` | Owner or ADMIN |
| PUT | `/tasks/{id}` | Owner or ADMIN |
| DELETE | `/tasks/{id}` | Owner or ADMIN |

### Users (ADMIN only)
| Method | Endpoint |
|---|---|
| GET | `/users` |
| GET | `/users/{id}` |
| POST | `/users` |
| PUT | `/users/{id}` |
| DELETE | `/users/{id}` |

---

## Running Locally

**Prerequisites:** Java 25, Maven, MySQL 8

```bash
# Clone
git clone https://github.com/tannhat0602/JavaMiniProject
cd JavaMiniProject

# Run (uses root/no-password MySQL by default)
mvn spring-boot:run
```

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

**Environment variables (optional):**

| Variable | Default |
|---|---|
| `DB_USERNAME` | `root` |
| `DB_PASSWORD` | _(empty)_ |
| `JWT_SECRET` | `mySuperSecretKey...` |
| `JWT_EXPIRATION` | `3600000` (1 hour) |

---

## Running with Docker

No Java or MySQL installation needed — just Docker Desktop.

```bash
docker-compose up --build
```

Starts MySQL 8 + Spring Boot app together. App available at `http://localhost:8080`.

---

## Running Tests

```bash
mvn test
```

Unit tests for `TaskServiceImpl` using JUnit 5 + Mockito (no database required):
- Create task — success and user not found
- Get task — as owner, as admin, unauthorized, not found
- Update task — as owner, unauthorized
- Delete task — as owner, as admin, unauthorized

---

## Key Concepts Practiced

- Designing stateless JWT authentication from scratch
- Implementing role-based access control with Spring Security 7
- Applying SOLID principles and Clean Architecture separation
- Writing unit tests with mocking (no Spring context needed)
- Containerizing a multi-service app with Docker Compose

---

## Author

**Phung Nhat Tan** — Aspiring Backend Developer, Java & Spring Boot
