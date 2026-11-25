package com.fooddelivery.chatbot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull Long menuItemId,
        @Min(1) @Max(10) int quantity) {
}
