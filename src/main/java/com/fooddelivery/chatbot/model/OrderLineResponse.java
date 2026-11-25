package com.fooddelivery.chatbot.model;

import java.math.BigDecimal;

public record OrderLineResponse(String productName, int quantity, BigDecimal unitPrice) {
}
