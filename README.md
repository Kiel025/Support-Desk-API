# 🎫 Support Desk API

A RESTful API for ticket and support management, built with **Java 21**, **Spring Boot 3**, **Spring Security + JWT**, **PostgreSQL**, **Flyway** and **Docker Compose**.

---

## 📋 Table of Contents

- [About the Project](#about-the-project)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Database Schema](#database-schema)
- [Endpoints](#endpoints)
- [Domain Model](#domain-model)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [Running Tests](#running-tests)
- [Project Structure](#project-structure)

---

## About the Project

The **Support Desk API** is a backend system for managing support tickets between customers and support agents. It supports ticket lifecycle management (creation, status transitions, comments and history), role-based access control, and JWT-based authentication.

**Key features:**
- User management with three distinct roles: `CUSTOMER`, `SUPPORT` and `ADMIN`
- Full ticket lifecycle: `OPEN` → `IN_PROGRESS` → `RESOLVED` → `CLOSED`
- Ticket priority levels: `LOW`, `MEDIUM`, `HIGH`, `URGENT`
- Comment system linked to tickets
- Automatic ticket status history tracking
- JWT authentication via Spring Security
- Database versioning with Flyway migrations
- Containerized PostgreSQL via Docker Compose

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.9 |
| Security | Spring Security + JWT |
| Persistence | Spring Data JPA (Hibernate) |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Containerization | Docker Compose |
| Build Tool | Maven |
| Utilities | Lombok |

---

## Architecture

The project follows a layered architecture organized by domain:

```
Controller  →  Service  →  Repository  →  Database
    ↑               ↑
   DTOs          Entities / Enums
```

Each domain (e.g., `ticket`, `user`) is self-contained with its own controller, service, repository, DTOs and enums — promoting cohesion and clear separation of concerns.

---

## Database Schema

The migration `V1__init.sql` creates the following tables:

### `users`
| Column | Type | Description |
|---|---|---|
| `id` | UUID (PK) | Auto-generated identifier |
| `name` | VARCHAR(100) | Full name |
| `email` | VARCHAR(150) UNIQUE | Login credential |
| `password_hash` | VARCHAR(255) | BCrypt-hashed password |
| `role` | VARCHAR(30) | `CUSTOMER`, `SUPPORT` or `ADMIN` |
| `is_active` | BOOLEAN | Soft-delete / account status |
| `created_at` | TIMESTAMP | Record creation time |
| `updated_at` | TIMESTAMP | Last update time |

### `ticket`
| Column | Type | Description |
|---|---|---|
| `id` | UUID (PK) | Auto-generated identifier |
| `title` | VARCHAR(255) | Short description |
| `description` | TEXT | Full ticket description |
| `status` | VARCHAR(30) | `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED` |
| `priority` | VARCHAR(30) | `LOW`, `MEDIUM`, `HIGH`, `URGENT` |
| `customer_id` | UUID (FK → users) | Ticket requester |
| `responsible_id` | UUID (FK → users) | Assigned support agent |
| `created_at` | TIMESTAMP | — |
| `updated_at` | TIMESTAMP | — |

### `ticket_comment`
| Column | Type | Description |
|---|---|---|
| `id` | UUID (PK) | — |
| `ticket_id` | UUID (FK → ticket) | Parent ticket |
| `author_id` | UUID (FK → users) | Comment author |
| `content` | TEXT | Comment body |
| `created_at` | TIMESTAMP | — |

### `ticket_history`
| Column | Type | Description |
|---|---|---|
| `id` | UUID (PK) | — |
| `ticket_id` | UUID (FK → ticket) | Related ticket |
| `changed_by` | UUID (FK → users) | Who changed the status |
| `old_status` | VARCHAR(30) | Previous status |
| `new_status` | VARCHAR(30) | New status |
| `created_at` | TIMESTAMP | When the change occurred |

> Indexes are created on `status`, `priority`, `customer_id`, `responsible_id` (on `ticket`) and `ticket_id` (on `ticket_comment` and `ticket_history`) for query performance.

---

## Endpoints

> **Base URL:** `http://localhost:8080`

### Users

| Method | Path | Description |
|---|---|---|
| `GET` | `/users` | List all users |
| `GET` | `/users/{id}` | Get user by ID |
| `POST` | `/users` | Create a new user |

#### POST `/users` — Request body example
```json
{
  "name": "Alice Silva",
  "email": "alice@example.com",
  "password": "secure_password",
  "role": "CUSTOMER"
}
```

#### Response body example
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Alice Silva",
  "email": "alice@example.com",
  "role": "CUSTOMER",
  "isActive": true,
  "createdAt": "2025-01-01T10:00:00",
  "updatedAt": null
}
```

### Tickets

| Method | Path | Description |
|---|---|---|
| `GET` | `/tickets/{id}` | Get ticket by ID |

> Additional ticket endpoints (create, update, list, comment, etc.) are part of the ongoing development roadmap.

---

## Domain Model

### User Roles

| Role | Description |
|---|---|
| `CUSTOMER` | Opens and tracks support tickets |
| `SUPPORT` | Manages and resolves tickets |
| `ADMIN` | Full access to users and tickets |

### Ticket Status Flow

```
OPEN  →  IN_PROGRESS  →  RESOLVED  →  CLOSED
```

Every status transition is persisted to the `ticket_history` table for full auditability.

### Ticket Priority

`LOW` < `MEDIUM` < `HIGH` < `URGENT`

---

## Getting Started

### Prerequisites

- [Java 21](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker + Docker Compose](https://docs.docker.com/compose/)

### 1. Clone the repository

```bash
git clone https://github.com/Kiel025/Support-Desk-API.git
cd Support-Desk-API
```

### 2. Start PostgreSQL with Docker Compose

```bash
docker compose up -d
```

This starts a PostgreSQL 16 container with the following defaults:

| Setting | Value |
|---|---|
| Database | `supportdesk` |
| User | `supportdesk` |
| Password | `supportdesk` |
| Port | `5433` (host) → `5432` (container) |

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Flyway will automatically apply the migration `V1__init.sql` on startup, creating all tables.

The API will be available at: **`http://localhost:8080`**

---

## Environment Variables

The default configuration is in `src/main/resources/application.yaml`. Override for different environments:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/supportdesk
    username: supportdesk
    password: supportdesk
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
```

For production, it is recommended to inject credentials via environment variables:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://prod-host:5432/supportdesk
SPRING_DATASOURCE_USERNAME=your_user
SPRING_DATASOURCE_PASSWORD=your_password
```

---

## Running Tests

```bash
./mvnw test
```

The project includes Spring Security test support (`spring-security-test`) for integration testing of secured endpoints.

---

## Project Structure

```
src/
└── main/
    ├── java/com/ezequiel/supportdesk_api/
    │   ├── SupportDeskApiApplication.java      # Entry point
    │   ├── exceptions/
    │   │   ├── TicketNotFoundException.java
    │   │   └── UserNotFoundException.java
    │   ├── ticket/
    │   │   ├── Ticket.java                     # JPA entity
    │   │   ├── TicketRepository.java
    │   │   ├── TicketService.java
    │   │   ├── comment/
    │   │   │   ├── TicketComment.java
    │   │   │   └── TicketCommentRepository.java
    │   │   ├── dto/
    │   │   │   ├── TicketRequestDTO.java
    │   │   │   └── TicketResponseDTO.java
    │   │   ├── enums/
    │   │   │   ├── TicketPriority.java
    │   │   │   └── TicketStatus.java
    │   │   └── history/
    │   │       ├── TicketHistory.java
    │   │       └── TicketHistoryRepository.java
    │   └── user/
    │       ├── User.java                       # JPA entity + UserDetails
    │       ├── UserController.java
    │       ├── UserRepository.java
    │       ├── UserRole.java
    │       ├── UserService.java
    │       └── dto/
    │           ├── UserRequestDTO.java
    │           └── UserResponseDTO.java
    └── resources/
        ├── application.yaml
        └── db/migration/
            └── V1__init.sql                    # Flyway initial migration
```

---

## 🔐 Security

Authentication is handled by **Spring Security** with **JWT (JSON Web Tokens)**. The `User` entity implements `UserDetails`, enabling native integration with Spring Security's authentication pipeline.

- Passwords are stored as hashes (BCrypt recommended)
- Role-based authorization via `ROLE_CUSTOMER`, `ROLE_SUPPORT` and `ROLE_ADMIN`
- Stateless session management (JWT — no server-side session)

---

## 📄 License

This project is open-source. See [LICENSE](LICENSE) for details.
