# Preguntas teóricas

## 1. Optimización en sistemas financieros

Imagina que trabajas en una aplicación bancaria que debe procesar transacciones en tiempo real. ¿Qué estrategias aplicarías para mejorar el rendimiento y la escalabilidad? Considera aspectos como concurrencia, caché y patrones de diseño.

**Respuesta:**

En un banco en tiempo real yo iría por tres frentes:

Caché. Lo que se lee mucho y cambia poco (saldo consultado, estado de un préstamo) lo guardo en memoria un rato. En este proyecto el GET /api/prestamos/{id}/estado usa Caffeine 60 segundos. Osea, el estado de un préstamo esta cacheado por 60 segundos, ya que la idea es evitar consultas repetitivas a la base de datos cuando varios usuarios solicitan la misma información. Si el admin aprueba, invalido esa clave: si no, la pantalla seguiría diciendo “pendiente”

Concurrencia. Cada request no debería bloquear a las demás. Acá las escrituras (aprobar) van con JPA y transacción, que es bloqueante pero correcto para no perder plata. No puse toda la API en WebFlux porque mezclar Netty + JPA + Security reactivo en poco tiempo era riesgoso. Si el cuello de botella fuera miles de consultas de estado, ahí sí un stream o más hilos de lectura. El atajo que sí apliqué fue cache, no magia reactiva.

Patrones. Cola (SQS): “anoto la transferencia y la proceso en orden”, no “el usuario espera 8 segundos”. Circuit breaker: si el buró de crédito se cae, no tumbo todo el banco. En este caso práctico el equivalente chico que añadi es: la regla en el dominio (Prestamo.decidir) y un solo lugar que escribe, para no duplicar lógica.

Escalabilidad: primero mido (qué query pesa). Después cache. Después más instancias del API sin sesión en el servidor (por eso JWT: cualquier servidor entiende el token). La base es lo último que se “parte”.

---

## 2. Seguridad en APIs financieras

Explica cómo protegerías una API que maneja información sensible de cuentas bancarias contra ataques como inyección SQL, CSRF, XSS y otros ataques comunes.

**Respuesta:**

El como protegeria la API seria de la siguiente manera punto por punto:

Inyección SQL. Nunca armaría el SQL a mano concatenando lo que escribió el usuario. JPA manda parámetros. Por ejemplo, en este proyecto no hay "SELECT * FROM ... " + email. El login busca por username con el repositorio.

CSRF. Esto pega fuerte cuando la sesión va en cookie: el navegador la manda solo. Aquí la sesión es JWT en el header Authorization. Por eso CSRF está desactivado: no hay cookie de sesión que el atacante aproveche. Si usáramos cookie, pondría token CSRF.

XSS. El atacante intenta colar JavaScript en un comentario y que se ejecute en el banco. Angular no pinta HTML crudo; no uso innerHTML. Un script se ve como texto.

Otros:

- Contraseñas con BCrypt, nunca en texto plano.
- JWT: sin token no pasas; el admin es el único que pega al PUT .../decision (filtro + @PreAuthorize).
- Validación de monto/plazo (@DecimalMin, etc.): basura ni entra.
- El secret del JWT en producción iría en variable de entorno, no hardcodeado como en la demo.
- HTTPS en real. Usuarios con el mínimo permiso (un cliente no ve el préstamo del vecino: eso está en el servicio).

---

## 3. Transacciones en sistemas distribuidos

En un sistema bancario distribuido, ¿cómo implementarías la consistencia y el manejo de errores en una API que procesa transferencias entre cuentas en diferentes servicios?

**Respuesta:**

Por ejemplo, si paso plata de una cuenta en el banco A a otra en el banco B, o se mueve en los dos lados, o en ninguno. No puede quedar “se descontó acá y no llegó allá”.

En este caso, en el actual proyecto no hay dos servicios. Hay una sola base H2. Para aprobar un préstamo basta @Transactional: si decidir() lanza error, no queda a medias.

Si fueran dos microservicios (cuentas vs. préstamos, o dos bancos):

No usaría 2PC clásico (lento y frágil). Usaría una saga:

Servicio origen: reserva/debita y anota “transferencia 99 iniciada”.
Llama al destino: acredita.
Si el destino falla: compensación (devuelvo la plata en origen).
Si se cae a mitad: un job reintenta o revierte según el estado guardado (patrón outbox: “primero guardo el evento en la misma transacción local, después lo publico”).


---

## 4. Pruebas unitarias y de integración

Describe cómo diseñarías una suite de pruebas que asegure la correcta operación de una API bancaria, considerando tanto pruebas unitarias como de integración. ¿Qué herramientas utilizarías?

**Respuesta:**

Las crearia Unitarias (rápidas, sin servidor): la regla de negocio. En el proyecto, PrestamoTest: no se rechaza sin motivo, no se decide dos veces, un cliente no aprueba. Herramientas: JUnit 5. No necesito Spring para eso.

Suite que armaría en un banco de verdad:

Unitarias de dominio (saldos, límites).
Integración de API (como las mías).
Contrato (el front y el back se ponen de acuerdo en el JSON).
Un par de end-to-end (login y ver saldo).
Herramientas: JUnit 5, Spring Boot Test, MockMvc o WebTestClient. En Angular, TestBed. En CI, mvn test en cada push. No prometo Selenium de todo: caro y frágil; lo importante es la regla del dinero.

---

## 5. Front-end

En una aplicación bancaria que muestra el saldo de las cuentas, ¿cómo gestionarías el estado y la autenticación en el front-end, garantizando la seguridad y la coherencia de los datos?

**Respuesta:**

En una aplicación bancaria lo gestiono con Auth, ya que Login pide usuario y clave. El back devuelve JWT. El front lo guarda y lo manda en cada request (interceptor). Guards: si no hay sesión, al login; si no eres admin, no entras a “Gestionar solicitudes”.

En el proyecto: AuthService con BehaviorSubject (el estado de “quién soy”). Token en sessionStorage para no perderlo al recargar; se borra al cerrar la pestaña. No usé NgRx: son 3 pantallas. Si el backoffice creciera, ahí sí.

Para la pantalla de Saldo / datos. No me invento el saldo en el front. Lo pido al API. Después de una operación (aprobar), vuelvo a consultar la lista. Así no queda un número viejo. Equivale a: pediste el préstamo, el admin aprueba, recargas “Mis préstamos” y ya no dice pendiente.

Seguridad en el front: el guard es una reja de cortesía. La puerta de verdad es el API. Aunque alguien fabrique la URL /admin, el PUT de decisión sin rol ADMIN sale 403.

XSS: Angular escapa. No pongo el token en una cookie sin pensar. En producción, HTTPS.

---

## 6. Spring Boot

### a. @SpringBootApplication

¿Qué pasa internamente en una aplicación cuando usas la anotación @SpringBootApplication y cómo afecta el arranque?

**Respuesta:**

Es un atajo de tres cosas:

@Configuration: esta clase arma el contexto.

@ComponentScan: busca tus @Service, @RestController, etc. en el paquete com.makers.prestamos y abajo. Por eso PrestamoService se inyecta solo.

@EnableAutoConfiguration: si ve JPA + H2 en el pom, configura Hibernate y el DataSource. Si ve spring-web, levanta Tomcat. Yo no escribí XML de Tomcat.

Al arrancar: lee application.yml (puerto 18473, H2, JWT) → crea beans → corre DatosIniciales (CommandLineRunner) y deja listos admin y usuario de prueba → el servidor escucha.


### b. Ciclo de vida de un bean

¿Cómo funciona el ciclo de vida de un bean en Spring y cómo podrías intervenir en él?

**Respuesta:**

Spring lo crea, le inyecta lo que necesita (el repo al service), opcionalmente @PostConstruct (un “ya estoy listo”), atiende requests, y al apagar @PreDestroy / DisposableBean.

Cómo intervenir: @PostConstruct para calentar caché; CommandLineRunner como DatosIniciales (corre cuando el contexto ya está arriba, no a mitad); @PreDestroy para cerrar un socket. No meter lógica de negocio pesada en el constructor: las dependencias a veces aún no están.

### c. Auto-configuración

¿Cómo personalizarías el comportamiento de la auto-configuración en Spring Boot sin romper la filosofía de "convención sobre configuración"?

**Respuesta:**

La convención es: Boot asume cosas razonables (H2 si está en el classpath, puerto 8080, etc.). Yo no peleo reescribiendo todo. Sobrescribo con properties:

server.port=18473
spring.jpa.hibernate.ddl-auto=update
spring.cache.type=caffeine
Si un auto-config estorba: spring.autoconfigure.exclude=... o un @Bean propio del mismo tipo (el CacheManager que definí). El truco es cambiar el default con configuración, no copiar 40 clases de Tomcat a mano.
