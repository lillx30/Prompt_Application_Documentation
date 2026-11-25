package com.fooddelivery.chatbot.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        String orderCode,
        String status,
        String paymentStatus,
        BigDecimal subtotal,
        BigDecimal deliveryFee,
        BigDecimal total,
        String estimatedDelivery,
        Instant createdAt,
        List<OrderLineResponse> items) {
}
