package com.fooddelivery.chatbot.model;

public record OrderStatus(String orderId, String status, String estimatedDelivery) {
}
