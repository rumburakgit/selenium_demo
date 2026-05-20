# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

Three co-dependent modules: a Spring Boot backend and React frontend that serve as the test target, plus a Selenium test suite. All three must be considered together.

- **`order-service`** — Spring Boot 3.4 / Java 17 REST backend on port **8080** (no database, all data hardcoded)
- **`order-client`** — React 18 / Vite 5 / TypeScript frontend on port **5173** (proxies `/api` → `http://localhost:8080`)
- **`order-tests`** — Selenium 4 / JUnit 5 / Java 17 end-to-end tests (requires both services running)

## Commands

### Backend (`order-service/`)
```bash
cd order-service
./mvnw spring-boot:run          # start dev server on :8080
./mvnw compile                  # compile only
./mvnw package                  # build JAR
java -jar target/order-service-0.0.1-SNAPSHOT.jar
```

### Frontend (`order-client/`)
```bash
cd order-client
npm install                     # first-time setup
npm run dev                     # start Vite dev server on :5173
npm run build                   # tsc + vite build → dist/
```

### Selenium tests (`order-tests/`)
```bash
# Both order-service (:8080) and order-client (:5173) must be running first
cd order-tests
mvn test                        # run all tests
mvn test -Dtest=HappyPathTest   # run a single test class
mvn test -Dtest=HappyPathTest#happyPathCard  # run a single test method
```

Environment variables read by `BaseTest`:
- `BASE_URL` — defaults to `http://localhost:5173`
- `CHROME_BIN` — defaults to `/usr/bin/google-chrome`
- `CHROME_DRIVER` — defaults to `/usr/bin/chromedriver`

### Docker (full stack)
```bash
# Build and start backend + frontend (tests excluded by profile)
docker compose up --build -d

# Run tests against the running stack, then remove container
docker compose run --rm order-tests

# Tear down
docker compose down           # stop and remove containers + network
docker compose down --rmi all # also remove images
```

In Docker, `order-client` is served by nginx on port **80** (not 5173); `BASE_URL` is set to `http://order-client:80` automatically.

The `order-tests` service is gated behind the `test` Docker Compose profile — it is not started by a plain `docker compose up`. CI runs `docker compose --profile test up --build --exit-code-from order-tests`.

### Kubernetes (minikube)
Images are built and pushed to `ghcr.io/rumburakgit/` by CI on every merge to `main`.

```bash
minikube start
minikube addons enable ingress

# Deploy backend, frontend, and Ingress
kubectl apply -f k8s/order-service-deployment.yaml -f k8s/order-service-service.yaml
kubectl apply -f k8s/order-client-deployment.yaml -f k8s/order-client-service.yaml
kubectl apply -f k8s/ingress.yaml

# Wait for pods, then run the test Job
kubectl wait --for=condition=ready pod -l app=order-client --timeout=60s
kubectl apply -f k8s/order-tests-job.yaml

# View test results and clean up
kubectl logs -l job-name=order-tests
kubectl delete -f k8s/

# Open frontend in browser — Ingress exposes everything on port 80
minikube ip   # e.g. 192.168.49.2 → open http://192.168.49.2
```

The test Job uses `backoffLimit: 0` (no retries) and sets `BASE_URL=http://order-client:80`. It includes init containers that poll until both services are reachable before starting tests.

## Architecture

### Data flow
Frontend (`localhost:5173`) → Vite proxy `/api` → Spring Boot (`localhost:8080/api`)

### Backend structure
All business logic lives in `OrderService` with no persistence layer. Controllers are thin delegators:
- `GET /api/products` and `GET /api/products/{id}` via `ProductController`
- `POST /api/orders` (201 / 400 / 422) and `GET /api/orders/{orderId}` via `OrderController`
- `CorsConfig` allows all origins (`allowedOriginPatterns("*")`)

Validation is manual inside `OrderService` (no Bean Validation). Errors return as `Map<String, String>` with HTTP 400; unavailability returns HTTP 422.

### Frontend structure
Single-page app with two views toggled by `App.tsx` state:
- **`OrderForm`** — fetches products on mount, renders form, calls `submitOrder`, dispatches errors to `ErrorSummary`
- **`ConfirmationView`** — shown after successful order submission
- **`orderApi.ts`** — axios wrapper; re-throws 400 as `ValidationErrors` and 422 as `UnavailableError` (typed, not generic Error)

### Selenium test structure
Tests use the Page Object Model pattern:
- **`BaseTest`** — sets up headless Chrome via WebDriverManager, 5 s explicit wait, `byTestId()` helper
- **`OrderFormPage`** / **`ConfirmationPage`** — page objects; all locators use `data-testid` attributes
- Test classes: `HappyPathTest`, `ValidationTest`, `UnavailableTest`, `ProductLoadTest`, `NavigationTest`

All selectors target `data-testid` attributes (e.g. `[data-testid='product-select']`). When adding UI elements that tests interact with, always add a matching `data-testid`.

### Key test data
| Product | Available | Triggers |
|---------|-----------|----------|
| P001–P003 | yes | 201 on valid order |
| P004 | no | 422 Unavailable |

Valid `paymentMethod`: `CARD`, `BLIK`, `TRANSFER`  
Only pre-existing order id: `ORD-20240001` (all others → 404)

Validation rules for `POST /api/orders`: `quantity` 1–99, `address.street` ≥5 chars, `address.city` ≥2 chars, `address.postalCode` format `XX-XXX`, `paymentMethod` enum.
