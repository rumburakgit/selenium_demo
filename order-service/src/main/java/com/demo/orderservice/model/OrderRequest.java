package com.demo.orderservice.model;

public class OrderRequest {
    private String productId;
    private Integer quantity;
    private Address address;
    private String paymentMethod;

    public OrderRequest() {}

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
