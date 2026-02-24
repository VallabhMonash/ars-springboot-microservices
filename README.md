# Airline Reservation Microservices (ARS)

A backend microservices project that upgrades a classic domain + tests codebase into an industry‑style system with **Spring Boot REST APIs**, **async processing**, **RabbitMQ messaging**, **Docker**, and **design patterns** — while preserving the original domain logic and tests.

---

## What This Project Demonstrates

- **Spring Boot REST APIs** for flights, passengers, and tickets  
- **Microservices architecture** (multi‑module Maven)  
- **Asynchronous processing** (ticket booking with polling)  
- **RabbitMQ messaging** (event-driven communication)  
- **Design patterns** (Factory + Singleton via Spring DI)  
- **Dockerized services** with docker‑compose  
- **Extensive unit + integration tests** retained from original domain

---

## Architecture Overview

### Modules

- ars-core/ → pure domain logic + tests
- flight-service/ → flight REST API + RabbitMQ consumer
- ticket-service/ → ticket & passenger REST API + RabbitMQ producer


### Messaging
- **ticket-service** publishes `TicketCreatedEvent`  
- **flight-service** consumes the event via `@RabbitListener`

---

## Tech Stack

- Java 21  
- Spring Boot 3.2.5  
- Maven (multi‑module)  
- RabbitMQ (AMQP)  
- Docker + docker‑compose  
- JUnit 5 + Mockito  
- JaCoCo + PIT

---

## Local Run (no Docker)

> RabbitMQ won’t be running locally unless you install it, so you may see connection warnings. The APIs still work.

### 1) Build core
```bash
mvn -N install
mvn -pl ars-core install
```

### Start services (two terminals)
```bash
mvn -f flight-service/pom.xml spring-boot:run
```
```bash
mvn -f ticket-service/pom.xml spring-boot:run
```

### 3) Test endpoints
```bash
curl http://localhost:8081/flights
```

```bash
curl -X POST http://localhost:8082/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "ticketId": 101,
    "passenger": {
      "firstName": "Alice",
      "secondName": "Wonder",
      "age": 30,
      "gender": "Man",
      "email": "alice@mail.com",
      "phoneNumber": "0412345678",
      "passport": "G12345678",
      "cardNumber": "1234567890123456",
      "securityCode": 123
    }
  }'
```

Poll status:
```bash
curl http://localhost:8082/tickets/requests/<requestId>
```

## Docker Run 

> This runs RabbitMQ + both services in containers.

### 1) Start everything
```bash
docker compose up --build
```

### Endpoints

- Flight API: 
```bash
http://localhost:8081/flights
```

- Ticket API:
```bash
http://localhost:8082/tickets
```
- RabbitMQ UI: 
```bash
http://localhost:15672 (guest/guest)
```

### 2) Stop Containers 
```bash
http://localhost:15672 (guest/guest)
```

## Endpoints

### Flight Service (8081)
- GET /flights
### Ticket Service (8082)
- POST /tickets → async booking
- GET /tickets/requests/{id} → polling status
- GET /tickets
- POST /passengers
- GET /passengers

## Async Ticket Flow
- 1. POST /tickets returns requestId with status PENDING
- 2. Poll GET /tickets/requests/{id}
- 3. Status becomes COMPLETED or FAILED

## Event‑Driven Messaging

> After successful booking, ticket-service publishes:

```bash
TicketCreatedEvent { ticketId, flightId, passengerEmail }
```

> flight-service consumes it and logs:
```bash
Ticket created event received: ...
```

## Tests
```bash
mvn test
```

> Coverage + mutation testing are enabled in ars-core:
- JaCoCo XML report
- PIT mutation testing

## Profiles
- Local: application.properties uses localhost
- Docker: application-docker.properties uses rabbitmq

