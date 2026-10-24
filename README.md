# Bank loan system

REST API with Spring Boot and an Angular front end to request, approve, or reject loans.

## How to run

Java 21, Maven, and Node 18+.

```bash
cd backend
mvn spring-boot:run
```

```bash
cd frontend
npm install
npm start
```

- Front end: http://127.0.0.1:14209
- API: http://127.0.0.1:18473
- H2 console: http://127.0.0.1:18473/h2-console  
  JDBC URL: `jdbc:h2:mem:prestamos`

| user               | password | role   |
|--------------------|----------|--------|
| usuario@test.com   | 123      | CLIENTE |
| admin@test.com     | 123      | ADMIN  |

## Behavior

- A client signs in, requests a loan (amount), and checks its status.
- An admin sees every request and can approve or reject it.
- A client cannot see another person's loans.
- A request that already has a decision cannot be decided again.
- The amount must be between 1,000 and 50,000,000.

## Backend layout

```
domain/           entities and business rules
domain/port       persistence contracts
application/      use cases and DTOs
infrastructure/   JPA, JWT security, cache, controllers
```

The rules (approve, reject, permissions) live in the domain (`Prestamo.decidir`), not in the controller.

## Stack

- Spring Boot, Spring Data JPA, Hibernate Validator, Spring Security (JWT)
- Caffeine cache on `GET /api/prestamos/{id}/estado` (60s; cleared when a decision is made)
- `@Transactional` on approve and reject
- In-memory H2 (switch to Postgres by changing the driver and the URL)
- Angular: reactive forms, JWT interceptor, `authGuard`, and `adminGuard`

## Tests

```bash
cd backend
mvn -B test
```

## Endpoints

| Method | Path | Notes |
|--------|------|--------|
| POST | `/api/auth/login` | public |
| GET | `/api/auth/me` | authenticated |
| POST | `/api/prestamos` | client |
| GET | `/api/prestamos` | client: their own; admin: all |
| GET | `/api/prestamos/{id}` | authenticated |
| GET | `/api/prestamos/{id}/estado` | authenticated, cached |
| PUT | `/api/prestamos/{id}/decision` | ADMIN only |
| GET/POST | `/api/usuarios` | ADMIN only |
