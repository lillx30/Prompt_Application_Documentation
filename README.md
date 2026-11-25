# Food Delivery Support Chatbot

A small Spring Boot chatbot for a fictional food delivery restaurant. It uses Spring AI's OpenAI-compatible client with OpenRouter, a constrained organization-only system prompt, local demo tools, and a single-page HTML/CSS/JavaScript frontend.

> **Demo only:** Menu, delivery policy, and order statuses are sample data. The app does not connect to a real restaurant or live order system.

## Requirements

- Java 17 or newer
- Maven 3.9+
- An OpenRouter API key (a free key works with a model available to your account)

## Configure and run

1. Copy `.env.example` to `.env`.
2. Set `OPENROUTER_API_KEY` in `.env` to your OpenRouter key. Keep this file private; it is excluded from Git.
3. From the project root, run:

   ```sh
   mvn spring-boot:run
   ```

4. Open [http://localhost:8080](http://localhost:8080).

The default model is OpenRouter's `openrouter/free` router. Set `OPENROUTER_MODEL` in `.env` to another model available to your account if needed. The OpenRouter base URL and model are configurable there as well.

## API

`POST /api/chat`

```json
{
  "message": "What vegetarian items are on the menu?"
}
```

Successful response:

```json
{
  "reply": "..."
}
```

Messages must contain non-whitespace text and be no longer than 1000 characters. The API key remains on the backend; the browser only calls this application.

## Demo data and tools

- `tools/FoodDeliveryTools` supplies menu search, delivery policy, and sample order lookup to the model.
- `model/MenuItem`, `model/DeliveryPolicy`, and `model/OrderStatus` describe the tool data.
- Sample order IDs are `DEMO-1001` and `DEMO-1002`. They are not real orders.
- Update the sample records in `FoodDeliveryTools` before adapting this project to an actual organization. For live order status, integrate an authenticated order service and apply its access controls; the current lookup tool is intentionally read-only and local.
- `service/FoodDeliveryChatService` contains the system prompt. Keep it limited to facts and actions the organization can actually support.

## Project structure

```text
src/main/java/com/fooddelivery/chatbot/
├── controller/   # HTTP API
├── model/        # Request, response, and domain data
├── service/      # Chat orchestration and system prompt
└── tools/        # Spring AI tools with demo restaurant data
src/main/resources/
└── static/index.html  # Self-contained UI (CSS and JavaScript are inline)
```

Configuration is in `src/main/resources/application.properties`, which imports the root `.env` file. This project uses no JPA, database, or persistence layer.

## Tests and package

```sh
mvn test
mvn package
```
