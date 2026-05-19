# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

A minimal Spring Boot 3.4 / Java 17 REST backend with **no database** — all product and order data is hardcoded in `OrderService`. It exists as a test target for Selenium end-to-end tests paired with a Vite frontend on `localhost:5173`.

## Commands

```bash
# Run dev server (hot reload via Spring DevTools is not configured — restarts fully)
./mvnw spring-boot:run

# Compile only
./mvnw compile

# Build JAR and run
./mvnw package
java -jar target/order-service-0.0.1-SNAPSHOT.jar
```

No test sources exist in this project.

## Architecture

All business logic lives in a single service with no persistence layer:

- **`OrderService`** — holds the static `PRODUCTS` map (P001–P004), validates `OrderRequest` manually (no Bean Validation), and constructs `OrderResponse`. Validation errors are returned as `Map<String, String>`; unavailability is checked after validation passes.
- **`OrderController`** (`/api/orders`) — delegates to `OrderService`; returns 400 on validation errors, 422 when product is unavailable, 201 on success. `GET /api/orders/{orderId}` only recognises the hardcoded id `ORD-20240001`.
- **`ProductController`** (`/api/products`) — thin wrapper over `OrderService.getAllProducts()` / `getProductById()`.
- **`CorsConfig`** — allows all methods from `http://localhost:5173` only.

## Key data

| Product ID | Available |
|-----------|-----------|
| P001–P003 | yes |
| P004      | **no** (triggers 422) |

Valid `paymentMethod` values: `CARD`, `BLIK`, `TRANSFER`.

The only pre-existing order id is `ORD-20240001`; all others return 404.
