# Aesthetics API

A RESTful API for managing appointments, services, and billing for aesthetics establishments — built with Java and Spring Boot.

---

## Description

Aesthetics API is a backend system designed to power aesthetics businesses. It allows clients to schedule services, process payments, and gives owners full control over their agenda and revenue.

**Core features:**
- Appointment scheduling with service selection, date/time and status management
- Payment processing — online (card, Pix) or in-person (cash, etc.)
- Service management — create, edit, and soft-delete services
- Agenda management — view and manage all appointments
- Role-based access control — OWNER and CLIENT profiles with different permissions
- JWT authentication — stateless, token-based security
- API documentation — interactive Swagger UI

---

## Technologies

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Programming language |
| Spring Boot | 4.0.3 | Application framework |
| Spring Web MVC | — | REST API layer |
| Spring Data JPA | — | Database access and ORM |
| Spring Security | — | Authentication and authorization |
| Spring Validation | — | Request validation |
| Spring DevTools | — | Hot reload during development |
| Lombok | 1.18.36 | Boilerplate code reduction |
| jjwt | 0.12.6 | JWT token generation and validation |
| PostgreSQL | 16 | Production database |
| H2 | — | In-memory database for testing |
| Docker | — | Production containerization |
| Maven | — | Dependency management and build tool |
| SpringDoc OpenAPI | 2.8.6 | Swagger UI and API documentation |

---

## Project Structure

```
com.gustavo.aestheticsapi
│
├── config          // Spring Security and OpenAPI configuration
├── controller      // HTTP request handlers (REST endpoints)
├── service         // Business logic layer
├── repository      // Database access interfaces (JPA)
├── domain
│   ├── entity      // JPA entities (database tables)
│   └── enums       // Enumerations (roles, statuses, etc.)
├── dto             // Data Transfer Objects (request/response)
└── exception       // Global exception handling
```

---

## API Endpoints

### Auth
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/auth/login` | Public | Authenticate and receive JWT token |

### Users
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/users` | Public | Create new user |
| GET | `/users` | OWNER | List all users |
| GET | `/users/{id}` | OWNER | Get user by ID |
| PUT | `/users/{id}` | OWNER | Update user |
| DELETE | `/users/{id}` | OWNER | Delete user |

### Services
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/services` | OWNER | Create new service |
| GET | `/services` | OWNER | List all services |
| GET | `/services/active` | OWNER, CLIENT | List active services |
| GET | `/services/{id}` | OWNER, CLIENT | Get service by ID |
| PUT | `/services/{id}` | OWNER | Update service |
| DELETE | `/services/{id}` | OWNER | Soft-delete service |

### Appointments
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/appointments` | CLIENT | Create new appointment |
| GET | `/appointments` | OWNER | List all appointments |
| GET | `/appointments/{id}` | OWNER, CLIENT | Get appointment by ID |
| GET | `/appointments/client/{clientId}` | OWNER, CLIENT | Get appointments by client |
| PATCH | `/appointments/{id}/cancel` | OWNER, CLIENT | Cancel appointment |

### Payments
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/payments` | OWNER, CLIENT | Create payment for appointment |
| GET | `/payments/appointment/{appointmentId}` | OWNER, CLIENT | Get payment by appointment |

---

## Authentication

This API uses **JWT (JSON Web Token)** for authentication.

1. Create a user via `POST /users`
2. Login via `POST /auth/login` to receive your token
3. Include the token in all protected requests:

```
Authorization: Bearer <your-token>
```

Tokens are valid for **24 hours**.

---

## API Documentation

Interactive documentation is available via Swagger UI after starting the application:

```
http://localhost:8080/swagger-ui/index.html
```

You can authenticate directly in the Swagger UI using the **Authorize** button and your JWT token.

---

## How to Run

### Prerequisites

- Java 17+
- Maven
- Docker

### Production (PostgreSQL + Docker)

Clone the repository:

```bash
git clone https://github.com/Gussffy/aesthetics-api.git
cd aesthetics-api
```

Create a `.env` file in the project root:

```env
POSTGRES_DB=aestheticsdb
POSTGRES_USER=your_username
POSTGRES_PASSWORD=your_password
```

Start the database:

```bash
docker-compose up -d
```

Configure `application-prod.properties` with your credentials and run:

```bash
./mvnw spring-boot:run
```

The application will start at `http://localhost:8080`.

### Development (H2 in-memory database)

Set the active profile to `dev` in `application.properties`:

```properties
spring.profiles.active=dev
```

Then run:

```bash
./mvnw spring-boot:run
```

The H2 console is available at `http://localhost:8080/h2-console`:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:aestheticsdb` |
| Username | `gus` |
| Password | `1234` |

---

## Running Tests

```bash
./mvnw test
```

Tests use mocks and H2 in-memory database and do not require any external services.

---

## Author

Gustavo — [Gussffy](https://github.com/Gussffy)
