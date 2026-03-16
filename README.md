# Yakov Home Assignment - Customer Support Hub

A Spring Boot 3.x home assignment implementing a secure backend for a customer support domain.

## API summary

### Authentication

#### Login
`POST /api/auth/login`

```json
{
  "username": "agent1",
  "password": "Agent123!"
}
```

### Profile

- `GET /api/profile/me`
- `PUT /api/profile/me`

### Customers

- `POST /api/customers` — AGENT or ADMIN
- `GET /api/customers` — AGENT sees own customers, ADMIN sees all customers

### Tickets

- `POST /api/tickets` — CUSTOMER or ADMIN
- `GET /api/tickets/mine` — CUSTOMER or ADMIN
- `GET /api/tickets/agent-view?subject=refund` — AGENT or ADMIN

## Demo users

Loaded automatically on first startup:

- `admin / Admin123!`
- `agent1 / Agent123!`
- `customer1 / Customer123!`

## Docker Build

Docker compose also include mysql 8.4

```bash
docker compose up --build
```



