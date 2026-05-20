# k8s — Kubernetes manifesty (minikube)

Obrazy są budowane i pushowane do ghcr.io przez CI po każdym merge na `main`.

## Wymagania

- minikube uruchomiony (`minikube start`)
- obrazy dostępne w ghcr.io (jeśli repozytorium jest prywatne — potrzebny `imagePullSecret`)

## Włącz Ingress addon w minikube

```bash
minikube addons enable ingress
```

## Deploy

```bash
# Backend
kubectl apply -f k8s/order-service-deployment.yaml -f k8s/order-service-service.yaml

# Frontend (zależy od backendu)
kubectl apply -f k8s/order-client-deployment.yaml -f k8s/order-client-service.yaml

# Ingressy (BE z rewrite, FE bez)
kubectl apply -f k8s/ingress.yaml

# Poczekaj aż oba pody będą Ready, następnie uruchom testy
kubectl wait --for=condition=ready pod -l app=order-client --timeout=60s
kubectl apply -f k8s/order-tests-job.yaml
```

## Uruchomienie frontendu w przeglądarce

Ingress LoadBalancer w minikube wymaga tunelu, żeby dostać adres IP dostępny z hosta:

```bash
# W osobnym terminalu (działa w tle, wymaga sudo)
minikube tunnel

# Następnie pobierz przydzielony EXTERNAL-IP
kubectl get ingress

# Otwórz http://<EXTERNAL-IP> w przeglądarce
```

## Logi testów i sprzątanie

```bash
# Logi z Joba
kubectl logs -l job-name=order-tests

# Usunięcie wszystkich zasobów
kubectl delete -f k8s/
```

## Struktura

| Plik | Zasób | Opis |
|------|-------|------|
| `order-service-deployment.yaml` | Deployment | Spring Boot REST API, port 8080, readinessProbe na `/api/products` |
| `order-service-service.yaml` | Service (ClusterIP) | Widoczny wewnątrz klastra jako `order-service:8080` |
| `order-client-deployment.yaml` | Deployment | nginx serwujący React SPA, port 80 |
| `order-client-service.yaml` | Service (ClusterIP) | Ruch zewnętrzny przejmuje Ingress |
| `ingress.yaml` | Ingress x2 (nginx) | `order-ingress-api`: `/api/*` → order-service:8080 (rewrite zachowuje `/api/`); `order-ingress-frontend`: `/*` → order-client:80 |
| `order-tests-job.yaml` | Job | Selenium + JUnit 5, `backoffLimit: 0`, łączy się z `http://order-client:80` |
