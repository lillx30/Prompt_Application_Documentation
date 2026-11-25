package com.fooddelivery.chatbot.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank @Size(max = 100) String customerName,
        @NotBlank @Size(max = 300) String deliveryAddress,
        @NotNull PaymentMethod paymentMethod,
        @NotEmpty @Size(max = 20) List<@Valid CreateOrderItemRequest> items) {
}
