# Order Service

REST API do testów Selenium — zahardkodowany backend bez bazy danych.

- **Java 17** (działa też na 21), Spring Boot 3.4, Maven
- Port: **8080**
- Format: `application/json`
- CORS otwarty na `http://localhost:5173` (Vite dev server)

---

## Uruchomienie

```bash
cd order-service
./mvnw spring-boot:run
```

Serwer startuje na `http://localhost:8080`.

### Tylko build (bez uruchamiania)

```bash
./mvnw compile
```

### Pakowanie do JAR

```bash
./mvnw package
java -jar target/order-service-0.0.1-SNAPSHOT.jar
```

---

## Endpointy

### Produkty

| Metoda | Ścieżka | Opis |
|--------|---------|------|
| GET | `/api/products` | Lista wszystkich produktów |
| GET | `/api/products/{id}` | Jeden produkt lub 404 |

### Zamówienia

| Metoda | Ścieżka | Opis |
|--------|---------|------|
| POST | `/api/orders` | Złóż zamówienie |
| GET | `/api/orders/{orderId}` | Pobierz zamówienie (zahardkodowane dla `ORD-20240001`) |

---

## Przykłady curl

### Lista produktów
```bash
curl http://localhost:8080/api/products
```

### Jeden produkt
```bash
curl http://localhost:8080/api/products/P001
# 404:
curl http://localhost:8080/api/products/P999
```

### Poprawne zamówienie → 201
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "P001",
    "quantity": 1,
    "address": {
      "street": "ul. Testowa 1",
      "city": "Warszawa",
      "postalCode": "00-001"
    },
    "paymentMethod": "CARD"
  }'
```

### Błędy walidacji → 400
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "P001",
    "quantity": 0,
    "address": {
      "street": "x",
      "city": "W",
      "postalCode": "abc"
    },
    "paymentMethod": "CASH"
  }'
```

### Produkt niedostępny → 422
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "P004",
    "quantity": 1,
    "address": {
      "street": "ul. Testowa 1",
      "city": "Warszawa",
      "postalCode": "00-001"
    },
    "paymentMethod": "CARD"
  }'
```

### Zahardkodowane zamówienie
```bash
curl http://localhost:8080/api/orders/ORD-20240001
# 404:
curl http://localhost:8080/api/orders/ORD-99999
```

---

## Dane testowe

### Produkty (statyczna mapa, bez bazy)

| ID | Nazwa | Cena | Dostępny |
|----|-------|------|----------|
| P001 | Laptop X1 | 3499.00 | tak |
| P002 | Monitor 27" | 1299.00 | tak |
| P003 | Klawiatura mechaniczna | 449.00 | tak |
| P004 | Mysz bezprzewodowa | 199.00 | **nie** |

### Metody płatności
`CARD`, `BLIK`, `TRANSFER`

---

## Reguły walidacji (POST /api/orders)

| Pole | Reguła |
|------|--------|
| `productId` | wymagane, musi istnieć, produkt musi być dostępny |
| `quantity` | wymagane, 1–99 |
| `address.street` | wymagane, min. 5 znaków |
| `address.city` | wymagane, min. 2 znaki |
| `address.postalCode` | format `XX-XXX` (np. `00-001`) |
| `paymentMethod` | jedna z: `CARD`, `BLIK`, `TRANSFER` |

---

## Struktura projektu

```
order-service/
├── pom.xml
└── src/main/java/com/demo/orderservice/
    ├── OrderServiceApplication.java
    ├── config/
    │   └── CorsConfig.java          # CORS dla localhost:5173
    ├── controller/
    │   ├── ProductController.java
    │   └── OrderController.java
    ├── model/
    │   ├── Product.java
    │   ├── Address.java
    │   ├── OrderRequest.java
    │   └── OrderResponse.java
    └── service/
        └── OrderService.java        # walidacja + dane zahardkodowane
```
