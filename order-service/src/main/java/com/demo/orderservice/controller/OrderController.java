package com.demo.orderservice.controller;

import com.demo.orderservice.model.OrderRequest;
import com.demo.orderservice.model.OrderResponse;
import com.demo.orderservice.model.Product;
import com.demo.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest req) {
        Map<String, String> errors = orderService.validate(req);
        if (!errors.isEmpty()) {
            return ResponseEntity.status(400).body(Map.of(
                    "status", 400,
                    "errors", errors
            ));
        }

        Product product = orderService.getProductById(req.getProductId()).orElseThrow();
        if (!product.isAvailable()) {
            return ResponseEntity.status(422).body(Map.of(
                    "status", 422,
                    "message", "Produkt " + req.getProductId() + " jest obecnie niedostępny"
            ));
        }

        OrderResponse response = orderService.createOrder(req);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        if ("ORD-20240001".equals(orderId)) {
            return ResponseEntity.ok(new OrderResponse(
                    "ORD-20240001",
                    "ACCEPTED",
                    "Zamówienie zostało przyjęte",
                    3499.00,
                    "2-3 dni robocze"
            ));
        }
        return ResponseEntity.status(404).body(Map.of(
                "status", 404,
                "message", "Order not found: " + orderId
        ));
    }
}
