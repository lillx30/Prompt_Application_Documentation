package com.fooddelivery.chatbot.service;

import com.fooddelivery.chatbot.model.DeliveryPolicy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DeliveryService {

    private static final BigDecimal DELIVERY_FEE = new BigDecimal("3.00");
    private static final BigDecimal MINIMUM_SUBTOTAL = new BigDecimal("15.00");
    private static final String ESTIMATED_DELIVERY = "30–45 minutes";

    public DeliveryPolicy getPolicy() {
        return new DeliveryPolicy(
                "Daily, 11:00 AM–10:00 PM",
                "Downtown and Riverside",
                "$3.00",
                "$15.00");
    }

    public BigDecimal getDeliveryFee() {
        return DELIVERY_FEE;
    }

    public BigDecimal getMinimumSubtotal() {
        return MINIMUM_SUBTOTAL;
    }

    public String getEstimatedDelivery() {
        return ESTIMATED_DELIVERY;
    }
}
