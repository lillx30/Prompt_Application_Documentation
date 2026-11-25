package com.fooddelivery.chatbot.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
        @NotBlank(message = "Message must not be blank")
        @Size(max = 1000, message = "Message must be 1000 characters or fewer")
        String message) {
}
