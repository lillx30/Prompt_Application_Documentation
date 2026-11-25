package com.fooddelivery.chatbot.model;

import java.math.BigDecimal;

public record MenuItem(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String category,
        String imageUrl) {
}
