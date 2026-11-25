package com.fooddelivery.chatbot.service;

import com.fooddelivery.chatbot.model.PaymentMethod;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String processDemoPayment(PaymentMethod method) {
        return switch (method) {
            case CASH_ON_DELIVERY -> "PAY_ON_DELIVERY";
            case SIMULATED_CARD -> "SIMULATED - no real payment processed";
        };
    }
}
