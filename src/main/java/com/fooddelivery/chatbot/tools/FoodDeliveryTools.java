package com.fooddelivery.chatbot.tools;

import com.fooddelivery.chatbot.model.DeliveryPolicy;
import com.fooddelivery.chatbot.model.MenuItem;
import com.fooddelivery.chatbot.model.OrderStatus;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class FoodDeliveryTools {

    private static final List<MenuItem> MENU = List.of(
            new MenuItem("Classic Burger", "Beef patty, lettuce, tomato, and house sauce",
                    new BigDecimal("12.50"), "Burgers"),
            new MenuItem("Veggie Wrap", "Grilled vegetables, hummus, and greens",
                    new BigDecimal("9.00"), "Wraps"),
            new MenuItem("Margherita Pizza", "Tomato, mozzarella, and fresh basil",
                    new BigDecimal("14.00"), "Pizza"),
            new MenuItem("Garden Salad", "Mixed greens, cucumber, tomato, and vinaigrette",
                    new BigDecimal("8.50"), "Salads")
    );

    private static final DeliveryPolicy DELIVERY_POLICY =
            new DeliveryPolicy("Daily, 11:00 AM–10:00 PM", "Downtown and Riverside",
                    "$3.00", "$15.00");

    private static final Map<String, OrderStatus> DEMO_ORDERS = Map.of(
            "DEMO-1001", new OrderStatus("DEMO-1001", "Preparing", "About 25 minutes"),
            "DEMO-1002", new OrderStatus("DEMO-1002", "Out for delivery", "About 10 minutes")
    );

    @Tool(description = "Search the demo restaurant menu by item name or category. "
            + "Use only for menu, ingredients, category, or price questions.")
    public List<MenuItem> searchMenu(
            @ToolParam(description = "A menu item name or category to search for") String query) {
        String normalizedQuery = query.toLowerCase(Locale.ROOT).trim();
        return MENU.stream()
                .filter(item -> item.name().toLowerCase(Locale.ROOT).contains(normalizedQuery)
                        || item.category().toLowerCase(Locale.ROOT).contains(normalizedQuery))
                .toList();
    }

    @Tool(description = "Get the restaurant's demo delivery hours, service areas, fee, and minimum order. "
            + "Use for delivery availability and fee questions.")
    public DeliveryPolicy getDeliveryPolicy() {
        return DELIVERY_POLICY;
    }

    @Tool(description = "Look up a sample order in the local demo data. This does not access a live order system.")
    public String lookupDemoOrder(
            @ToolParam(description = "A sample order ID, for example DEMO-1001") String orderId) {
        OrderStatus order = DEMO_ORDERS.get(orderId.toUpperCase(Locale.ROOT).trim());
        if (order == null) {
            return "No matching demo order. This application is not connected to a live order system.";
        }
        return "DEMO DATA ONLY — Order %s is %s. Estimated delivery: %s."
                .formatted(order.orderId(), order.status(), order.estimatedDelivery());
    }
}
