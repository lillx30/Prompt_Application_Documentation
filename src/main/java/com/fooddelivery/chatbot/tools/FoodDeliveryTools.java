package com.fooddelivery.chatbot.tools;

import com.fooddelivery.chatbot.model.DeliveryPolicy;
import com.fooddelivery.chatbot.model.MenuItem;
import com.fooddelivery.chatbot.service.CatalogService;
import com.fooddelivery.chatbot.service.DeliveryService;
import com.fooddelivery.chatbot.service.OrderService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FoodDeliveryTools {

    private final CatalogService catalogService;
    private final DeliveryService deliveryService;
    private final OrderService orderService;

    public FoodDeliveryTools(
            CatalogService catalogService,
            DeliveryService deliveryService,
            OrderService orderService) {
        this.catalogService = catalogService;
        this.deliveryService = deliveryService;
        this.orderService = orderService;
    }

    @Tool(description = "Search the current demo menu by item name, description, or category. "
            + "Use only for menu, ingredients, category, or price questions.")
    public List<MenuItem> searchMenu(
            @ToolParam(description = "A menu item name, ingredient, or category to search for") String query) {
        return catalogService.searchMenu(query);
    }

    @Tool(description = "Get the restaurant's delivery hours, service areas, fee, and minimum order.")
    public DeliveryPolicy getDeliveryPolicy() {
        return deliveryService.getPolicy();
    }

    @Tool(description = "Look up only the status and estimate for a placed demo order. "
            + "This is not connected to a live delivery provider.")
    public String lookupOrderStatus(
            @ToolParam(description = "The tracking code shown after checkout") String orderCode) {
        return orderService.lookupStatusForAssistant(orderCode);
    }
}
