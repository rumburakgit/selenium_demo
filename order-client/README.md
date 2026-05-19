# order-client

Frontend aplikacji zamówień — cel testów Selenium. React 18, TypeScript, Vite 5.

- Port: **5173** (Vite dev server)
- Proxy: `/api` → `http://localhost:8080` (wymaga uruchomionego `order-service`)

---

## Uruchomienie

```bash
npm install
npm run dev
```

Aplikacja dostępna pod `http://localhost:5173`.

### Pozostałe komendy

```bash
npm run build    # tsc + vite build → dist/
npm run preview  # serwuje dist/ na :4173
```

---

## Struktura źródeł

```
src/
├── api/
│   └── orderApi.ts          # axios wrapper: getProducts(), submitOrder()
├── components/
│   ├── OrderForm.tsx        # formularz zamówienia (widok główny)
│   ├── ConfirmationView.tsx # ekran po złożeniu zamówienia
│   └── ErrorSummary.tsx     # lista błędów walidacji / niedostępności
├── types/
│   └── index.ts             # Product, OrderRequest, OrderResponse, błędy
├── styles/
│   └── main.css
├── App.tsx                  # przełączanie widoków stanem confirmation
└── main.tsx
```

---

## Przepływ aplikacji

1. `App.tsx` renderuje `OrderForm` lub `ConfirmationView` w zależności od stanu `confirmation`.
2. `OrderForm` pobiera listę produktów przy montowaniu (`GET /api/products`) i filtruje tylko dostępne.
3. Po wysłaniu formularza (`POST /api/orders`):
   - **201** → `onSuccess(response)` → widok `ConfirmationView`
   - **400** → błędy walidacji jako `Record<string, string>` wyświetlane w `ErrorSummary`
   - **422** → komunikat o niedostępności produktu wyświetlany w `ErrorSummary`
4. Przycisk „Złóż nowe zamówienie" resetuje stan do `null` → powrót do formularza.

---

## Atrybuty `data-testid` (Selenium)

| Element | `data-testid` |
|---------|--------------|
| Select produktu | `product-select` |
| Input ilości | `quantity-input` |
| Input ulicy | `street-input` |
| Input miasta | `city-input` |
| Input kodu pocztowego | `postal-code-input` |
| Radio CARD | `payment-CARD` |
| Radio BLIK | `payment-BLIK` |
| Radio TRANSFER | `payment-TRANSFER` |
| Przycisk submit | `submit-button` |
| Kontener błędów | `error-summary` |
| Błąd konkretnego pola | `error-{fieldName}` |
| Błąd niedostępności | `error-unavailable` |
| Widok potwierdzenia | `confirmation-view` |
| ID zamówienia | `order-id` |
| Status zamówienia | `order-status` |
| Cena | `order-price` |
| Termin dostawy | `order-delivery` |
| Przycisk nowego zamówienia | `new-order-button` |
