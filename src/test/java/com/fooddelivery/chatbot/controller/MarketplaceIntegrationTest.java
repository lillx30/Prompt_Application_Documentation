package com.fooddelivery.chatbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.chatbot.model.CreateOrderItemRequest;
import com.fooddelivery.chatbot.model.CreateOrderRequest;
import com.fooddelivery.chatbot.model.MenuProduct;
import com.fooddelivery.chatbot.model.OrderResponse;
import com.fooddelivery.chatbot.model.PaymentMethod;
import com.fooddelivery.chatbot.repository.FoodOrderRepository;
import com.fooddelivery.chatbot.repository.MenuProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MarketplaceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearOrders() {
        foodOrderRepository.deleteAll();
    }

    @Test
    void listsPersistedMenuItemsWithImages() throws Exception {
        mockMvc.perform(get("/api/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$[0].imageUrl").value(org.hamcrest.Matchers.startsWith("https://images.unsplash.com/")));
    }

    @Test
    void placesOrderAndTracksItByGeneratedCode() throws Exception {
        MenuProduct burger = findMenuItem("Classic Burger");
        MenuProduct wrap = findMenuItem("Veggie Wrap");
        CreateOrderRequest request = new CreateOrderRequest(
                "Demo Customer",
                "123 Sample Street, Downtown",
                PaymentMethod.SIMULATED_CARD,
                List.of(new CreateOrderItemRequest(burger.getId(), 1),
                        new CreateOrderItemRequest(wrap.getId(), 1)));

        MvcResult result = mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PLACED"))
                .andExpect(jsonPath("$.paymentStatus").value("SIMULATED - no real payment processed"))
                .andExpect(jsonPath("$.total").value(24.50))
                .andReturn();

        OrderResponse placedOrder = objectMapper.readValue(
                result.getResponse().getContentAsString(), OrderResponse.class);
        assertThat(placedOrder.orderCode()).startsWith("FD-");

        mockMvc.perform(get("/api/orders/{orderCode}", placedOrder.orderCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderCode").value(placedOrder.orderCode()))
                .andExpect(jsonPath("$.status").value("PLACED"))
                .andExpect(jsonPath("$.items.length()").value(2));
    }

    @Test
    void rejectsOrderBelowMinimumSubtotal() throws Exception {
        MenuProduct wrap = findMenuItem("Veggie Wrap");
        CreateOrderRequest request = new CreateOrderRequest(
                "Demo Customer",
                "123 Sample Street",
                PaymentMethod.CASH_ON_DELIVERY,
                List.of(new CreateOrderItemRequest(wrap.getId(), 1)));

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The minimum food subtotal is $15.00."));
    }

    @Test
    void rejectsQuantityAboveThePerItemLimit() throws Exception {
        MenuProduct burger = findMenuItem("Classic Burger");
        CreateOrderRequest request = new CreateOrderRequest(
                "Demo Customer",
                "123 Sample Street",
                PaymentMethod.CASH_ON_DELIVERY,
                List.of(new CreateOrderItemRequest(burger.getId(), 11)));

        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reportsUnknownTrackingCodesAsNotFound() throws Exception {
        mockMvc.perform(get("/api/orders/{orderCode}", "FD-NOTREAL"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No order found for that tracking code."));
    }

    private MenuProduct findMenuItem(String name) {
        return menuProductRepository.findAllByAvailableTrueOrderByCategoryAscNameAsc().stream()
                .filter(item -> item.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }
}
