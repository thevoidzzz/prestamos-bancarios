# Sistema de prestamos bancarios

API REST con Spring Boot y un front en Angular para solicitar, aprobar o rechazar prestamos.

## Como correrlo

Java 21, Maven y Node 18+.

```bash
cd backend
mvn spring-boot:run
```

```bash
cd frontend
npm install
npm start
```

- Front: http://127.0.0.1:14209
- API: http://127.0.0.1:18473
- Consola H2: http://127.0.0.1:18473/h2-console  
  JDBC URL: `jdbc:h2:mem:prestamos`

| usuario            | clave | rol     |
|--------------------|-------|---------|
| usuario@test.com   | 123   | CLIENTE |
| admin@test.com     | 123   | ADMIN   |

## Funcionalidad

- El cliente inicia sesion, pide un prestamo (monto) y consulta el estado.
- El admin ve todas las solicitudes y puede aprobar o rechazar.
- Un cliente no ve prestamos de otra persona.
- Una solicitud ya decidida no se vuelve a decidir.
- El monto debe ser mayor a 0.

## Estructura del backend

```
domain/           entidades y reglas de negocio
domain/port       contratos de persistencia
application/      casos de uso y DTOs
infrastructure/   JPA, seguridad JWT, cache, controllers
```

Las reglas (aprobar/rechazar, permisos) estan en el dominio (`Prestamo.decidir`), no en el controller.

## Tecnologias

- Spring Boot, Spring Data JPA, Hibernate Validator, Spring Security (JWT)
- Cache Caffeine en `GET /api/prestamos/{id}/estado` (60s; se limpia al decidir)
- `@Transactional` en la aprobacion/rechazo
- H2 en memoria (se puede cambiar a Postgres cambiando driver y URL)
- Angular: Reactive Forms, interceptor JWT, `authGuard` y `adminGuard`

## Tests

```bash
cd backend
mvn -B test
```

## Endpoints

| Metodo | Ruta | Notas |
|--------|------|--------|
| POST | `/api/auth/login` | publico |
| GET | `/api/auth/me` | autenticado |
| POST | `/api/prestamos` | cliente |
| GET | `/api/prestamos` | cliente: los suyos; admin: todos |
| GET | `/api/prestamos/{id}` | autenticado |
| GET | `/api/prestamos/{id}/estado` | autenticado, con cache |
| PUT | `/api/prestamos/{id}/decision` | solo ADMIN |
| GET/POST | `/api/usuarios` | solo ADMIN |
