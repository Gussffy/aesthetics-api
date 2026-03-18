# Aesthetics API

A RESTful API for managing appointments, services, and billing for aesthetics establishments — built with Java and Spring Boot.

---

## Description

Aesthetics API is a backend system designed to power aesthetics businesses. It allows clients to schedule services, process payments, and gives owners full control over their agenda and revenue through a billing dashboard.

**Core features:**
- Appointment scheduling with service selection, date/time, and booking fee payment
- Payment processing — online (card, Pix) or in-person (cash, etc.)
- Service management — create, edit, and delete services with name, description, category, price, and duration
- Agenda management — view and manage all appointments
- Billing dashboard — daily revenue and projected revenue based on the schedule

---

## Technologies

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 4.0.3 | Application framework |
| Spring Web MVC | REST API layer |
| Spring Data JPA | Database access and ORM |
| Spring Security | Authentication and authorization |
| Spring Validation | Request validation |
| Spring DevTools | Hot reload during development |
| Lombok | Boilerplate code reduction |
| PostgreSQL | Production database |
| H2 | In-memory database for testing |
| Docker | Production containerization |
| Maven | Dependency management and build tool |

---

## Project Structure

```
com.gustavo.aestheticsapi
│
├── config          // Spring Security and app configuration
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

## How to Run

### Prerequisites

- Java 17+
- Maven
- Docker (for production with PostgreSQL)

### Development (H2 in-memory database)

Clone the repository:

```bash
git clone https://github.com/Gussffy/aesthetics-api.git
cd aesthetics-api
```

Run the application with the `dev` profile:

```bash
./mvnw spring-boot:run
```

The application will start at `http://localhost:8080`.

You can access the H2 console at `http://localhost:8080/h2-console` with the following settings:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:aestheticsdb` |
| Username | `gus` |
| Password | *1234* |

### Production (PostgreSQL + Docker)

> Docker configuration coming soon.

Set the active profile to `prod` in `application.properties` and configure your PostgreSQL credentials in `application-prod.properties`:

```properties
spring.profiles.active=prod
```

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/aestheticsdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Then run:

```bash
./mvnw spring-boot:run
```

---

## Running Tests

```bash
./mvnw test
```

Tests use the H2 in-memory database and do not require any external services.

---

## Author

Gustavo — [Gussffy]([https://github.com/Gussffy](https://github.com/Gussffy))
