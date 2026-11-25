# Demo Food Delivery Marketplace

A small full-stack food delivery demo with a marketplace, persistent menu and orders, guest checkout, simulated payment, demo delivery tracking, and an organization-scoped AI assistant.

> **Demo only:** Customer details, payment states, delivery estimates, and orders are sample application data. No real payment or delivery provider is connected. Do not use real customer or payment information.

## Features

- Menu catalog persisted in MySQL with public Unsplash food images.
- Basket stored in the browser and guest checkout backed by Spring Data JPA.
- Server-calculated totals, a $15 minimum food subtotal, and a $3 delivery fee.
- Cash-on-delivery or a simulated card result; neither option processes a real payment.
- Generated order tracking codes with a demo `PLACED` status and estimated delivery time.
- Spring AI/OpenRouter assistant with read-only menu, delivery policy, and order-status tools.
- AOP service timing logs that do not log method arguments or customer details.
- One self-contained HTML page with inline CSS and JavaScript.

## Requirements

- Java 17 or newer
- Maven 3.9+
- Docker with the Compose plugin
- An OpenRouter API key for the assistant

## Configure and run

If you do not already have a root `.env`, copy `.env.example` to `.env`. Keep any existing OpenRouter key; do not commit `.env`. The example's database values match the local MySQL defaults.

Start MySQL:

```sh
docker compose up -d mysql
docker compose ps
```

Wait for the `mysql` health check to report healthy, then start the application:

```sh
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080). The app creates its tables and inserts demo menu items on the first startup. Docker data is stored in the `mysql_data` volume.

`application.properties` reads the database and OpenRouter settings from `.env`. The default database connection is `localhost:3306`, database `food_delivery`, user `food_delivery`, password `food_delivery_dev`. For non-demo use, replace all development credentials and configure proper secret management.

The default AI model is OpenRouter's `openrouter/free` router. The base URL must be `https://openrouter.ai/api` (without `/v1`), because the application appends `/v1/chat/completions`.

## API

### Menu

`GET /api/menu` returns available products. Add `?category=Pizza` to filter by category.

### Place an order

`POST /api/orders`

```json
{
  "customerName": "Demo Customer",
  "deliveryAddress": "123 Sample Street, Downtown",
  "paymentMethod": "CASH_ON_DELIVERY",
  "items": [
    { "menuItemId": 1, "quantity": 1 },
    { "menuItemId": 2, "quantity": 1 }
  ]
}
```

Use IDs returned by `GET /api/menu`; IDs in the example are illustrative. The server looks up current prices and calculates the subtotal, delivery fee, and total. Requests below the minimum food subtotal are rejected. Payment methods are `CASH_ON_DELIVERY` and `SIMULATED_CARD`.

### Track an order

`GET /api/orders/{orderCode}` returns the order summary and demo status. The checkout response contains the generated tracking code.

### Chat

`POST /api/chat`

```json
{
  "message": "What vegetarian items are on the menu?"
}
```

The response contains a `reply` field. The model uses database-backed read-only tools for menu, policy, and order status. The assistant does not place or modify orders.

## Project structure

```text
src/main/java/com/fooddelivery/chatbot/
├── common/       # API errors and cross-cutting AOP
├── controller/   # Menu, order, and chat endpoints
├── model/        # JPA entities, request/response models
├── repository/   # Spring Data JPA repositories
├── service/      # Catalog, checkout, payment, delivery, chat
└── tools/        # Database-backed Spring AI tools
src/main/resources/
└── static/index.html  # Marketplace, checkout, tracking, and chat UI
docker-compose.yml     # Local MySQL service
```

`SIMULATED_CARD` never collects card details or calls a payment provider. Delivery status remains demo data; no real delivery service is integrated. The service aspect logs method names and elapsed time only.

## Tests and package

Tests use an in-memory H2 database and do not call OpenRouter or require Docker:

```sh
mvn test
mvn package
```
