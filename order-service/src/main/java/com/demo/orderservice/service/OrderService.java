package com.demo.orderservice.service;

import com.demo.orderservice.model.Address;
import com.demo.orderservice.model.OrderRequest;
import com.demo.orderservice.model.OrderResponse;
import com.demo.orderservice.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    private static final Map<String, Product> PRODUCTS = Map.of(
            "P001", new Product("P001", "Laptop X1", 3499.00, true),
            "P002", new Product("P002", "Monitor 27\"", 1299.00, true),
            "P003", new Product("P003", "Klawiatura mechaniczna", 449.00, true),
            "P004", new Product("P004", "Mysz bezprzewodowa", 199.00, false)
    );

    private static final Set<String> VALID_PAYMENT_METHODS = Set.of("CARD", "BLIK", "TRANSFER");

    public List<Product> getAllProducts() {
        return new ArrayList<>(PRODUCTS.values());
    }

    public Optional<Product> getProductById(String id) {
        return Optional.ofNullable(PRODUCTS.get(id));
    }

    public Map<String, String> validate(OrderRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (req.getProductId() == null || req.getProductId().isBlank()) {
            errors.put("productId", "Identyfikator produktu jest wymagany");
        } else if (!PRODUCTS.containsKey(req.getProductId())) {
            errors.put("productId", "Produkt nie istnieje: " + req.getProductId());
        }

        if (req.getQuantity() == null) {
            errors.put("quantity", "Ilość jest wymagana");
        } else if (req.getQuantity() < 1 || req.getQuantity() > 99) {
            errors.put("quantity", "Ilość musi być między 1 a 99");
        }

        Address address = req.getAddress();
        if (address == null) {
            errors.put("address", "Adres jest wymagany");
        } else {
            if (address.getStreet() == null || address.getStreet().isBlank() || address.getStreet().length() < 5) {
                errors.put("address.street", "Ulica jest wymagana (min. 5 znaków)");
            }
            if (address.getCity() == null || address.getCity().isBlank() || address.getCity().length() < 2) {
                errors.put("address.city", "Miasto jest wymagane (min. 2 znaki)");
            }
            if (address.getPostalCode() == null || !address.getPostalCode().matches("\\d{2}-\\d{3}")) {
                errors.put("address.postalCode", "Nieprawidłowy format kodu pocztowego (XX-XXX)");
            }
        }

        if (req.getPaymentMethod() == null || req.getPaymentMethod().isBlank()) {
            errors.put("paymentMethod", "Metoda płatności jest wymagana");
        } else if (!VALID_PAYMENT_METHODS.contains(req.getPaymentMethod())) {
            errors.put("paymentMethod", "Nieprawidłowa metoda płatności");
        }

        return errors;
    }

    public OrderResponse createOrder(OrderRequest req) {
        Product product = PRODUCTS.get(req.getProductId());
        double totalPrice = product.getPrice() * req.getQuantity();
        String orderId = "ORD-" + System.currentTimeMillis();
        return new OrderResponse(orderId, "ACCEPTED", "Zamówienie zostało przyjęte", totalPrice, "2-3 dni robocze");
    }
}
