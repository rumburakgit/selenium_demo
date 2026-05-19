# Selenium Demo — Docker

## Uruchomienie

```bash
# Zbuduj i uruchom tylko backend + frontend (bez testów)
docker compose up --build -d

# Uruchom testy (usuwa kontener po zakończeniu)
docker compose run --rm order-tests
```

`order-tests` ma przypisany profil `test`, więc `docker compose up` go pomija.
Testy uruchamia się osobno, dopiero gdy aplikacja działa.

## Zatrzymanie i sprzątanie

```bash
# Zatrzymaj i usuń kontenery + sieć
docker compose down

# Zatrzymaj + usuń obrazy
docker compose down --rmi all
```
