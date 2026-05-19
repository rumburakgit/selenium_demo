# order-tests

Testy end-to-end Selenium dla aplikacji zamówień (`order-service` + `order-client`).

- **Java 17**, JUnit 5, Selenium 4.18, WebDriverManager 5.7
- Tryb: **headless Chrome** (działa bez wyświetlacza)
- Wymaga działających obu serwisów przed uruchomieniem testów

---

## Wymagania

- Java 17+
- Maven 3.6+
- Google Chrome zainstalowany w systemie (ChromeDriver pobierany automatycznie przez WebDriverManager)
- Uruchomiony `order-service` na `http://localhost:8080`
- Uruchomiony `order-client` na `http://localhost:5173`

---

## Uruchomienie

```bash
# 1. terminal 1 — backend
cd order-service && ./mvnw spring-boot:run

# 2. terminal 2 — frontend
cd order-client && npm run dev

# 3. terminal 3 — testy
cd order-tests && mvn test
```

Wyniki w konsoli i w `target/surefire-reports/`.

---

## Struktura projektu

```
src/test/java/com/demo/ordertests/
├── base/
│   └── BaseTest.java          # setup/teardown WebDriver, helpery
├── pages/
│   ├── OrderFormPage.java     # Page Object — formularz zamówienia
│   └── ConfirmationPage.java  # Page Object — ekran potwierdzenia
└── tests/
    ├── ProductLoadTest.java   # TC-01
    ├── HappyPathTest.java     # TC-02, 03, 04
    ├── ValidationTest.java    # TC-05–10
    ├── UnavailableTest.java   # TC-11
    └── NavigationTest.java    # TC-12
```

---

## Przypadki testowe

| ID | Klasa | Opis |
|----|-------|------|
| TC-01 | `ProductLoadTest` | Dropdown ładuje 3 produkty; P004 ukryty |
| TC-02 | `HappyPathTest` | Poprawne zamówienie — CARD → status ACCEPTED |
| TC-03 | `HappyPathTest` | Poprawne zamówienie — BLIK → status ACCEPTED |
| TC-04 | `HappyPathTest` | Poprawne zamówienie — TRANSFER → status ACCEPTED |
| TC-05 | `ValidationTest` | Pusty formularz → błędy: productId, street, city, postalCode |
| TC-06 | `ValidationTest` | quantity = 0 → błąd quantity |
| TC-07 | `ValidationTest` | quantity = 100 → błąd quantity |
| TC-08 | `ValidationTest` | Zły kod pocztowy → błąd address.postalCode |
| TC-09 | `ValidationTest` | Ulica < 5 znaków → błąd address.street |
| TC-10 | `ValidationTest` | Miasto < 2 znaki → błąd address.city |
| TC-11 | `UnavailableTest` | P004 (Mysz bezprzewodowa) niewidoczny w dropdown |
| TC-12 | `NavigationTest` | Po zamówieniu → „Złóż nowe zamówienie" → reset formularza |

---

## Architektura testów

**BaseTest** inicjuje ChromeDriver w trybie headless przed każdym testem i zamyka go po teście. Udostępnia helpery:
- `byTestId(String)` — selektor `[data-testid='...']`; zero XPath, zero klas CSS
- `waitFor(By)` — `WebDriverWait` 5 s na `visibilityOfElementLocated`

**Page Objects** enkapsulują całą interakcję z DOM. Testy nie dotykają Selenium API bezpośrednio.

**Zasady wait:**
- Nigdy `Thread.sleep()` — zawsze `WebDriverWait` z `ExpectedConditions`
- Asercja negatywna (element nie istnieje) przez `driver.findElements(...).isEmpty()`

---

## Znane zachowanie backendu istotne dla testów

| Sytuacja | HTTP | Efekt w UI |
|----------|------|------------|
| Poprawne zamówienie | 201 | `confirmation-view` |
| Błędy walidacji | 400 | `error-summary` + `error-{fieldName}` |
| Produkt niedostępny (P004) | 422 | `error-unavailable` |
| `quantity` domyślnie = 1 (valid) | — | TC-05 nie asertuje `error-quantity` |
