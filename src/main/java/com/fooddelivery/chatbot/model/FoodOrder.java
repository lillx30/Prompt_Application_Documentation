package com.fooddelivery.chatbot.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food_orders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String orderCode;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(nullable = false, length = 300)
    private String deliveryAddress;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false, length = 40)
    private String paymentStatus;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "foodOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderLine> items = new ArrayList<>();

    protected FoodOrder() {
    }

    public FoodOrder(
            String orderCode,
            String customerName,
            String deliveryAddress,
            String status,
            String paymentStatus,
            BigDecimal subtotal,
            BigDecimal deliveryFee,
            BigDecimal total) {
        this.orderCode = orderCode;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.subtotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.total = total;
    }

    public void addItem(MenuProduct product, int quantity) {
        items.add(new OrderLine(this, product.getName(), quantity, product.getPrice()));
    }

    @PrePersist
    void setCreatedAt() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<OrderLine> getItems() {
        return items;
    }
}
