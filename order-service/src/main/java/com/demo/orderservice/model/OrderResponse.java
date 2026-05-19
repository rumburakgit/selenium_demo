package com.demo.orderservice.model;

public class OrderResponse {
    private String orderId;
    private String status;
    private String message;
    private double totalPrice;
    private String estimatedDelivery;

    public OrderResponse() {}

    public OrderResponse(String orderId, String status, String message, double totalPrice, String estimatedDelivery) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.totalPrice = totalPrice;
        this.estimatedDelivery = estimatedDelivery;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(String estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }
}
