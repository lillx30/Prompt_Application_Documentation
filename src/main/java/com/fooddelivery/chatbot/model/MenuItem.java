package com.fooddelivery.chatbot.model;

import java.math.BigDecimal;

public record MenuItem(String name, String description, BigDecimal price, String category) {
}
