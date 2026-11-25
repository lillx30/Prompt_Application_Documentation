package com.fooddelivery.chatbot.tools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FoodDeliveryToolsTest {

    private final FoodDeliveryTools tools = new FoodDeliveryTools();

    @Test
    void searchesMenuByItemAndCategory() {
        assertThat(tools.searchMenu("burger"))
                .extracting(item -> item.name())
                .containsExactly("Classic Burger");
        assertThat(tools.searchMenu("pizza"))
                .extracting(item -> item.name())
                .containsExactly("Margherita Pizza");
    }

    @Test
    void unknownOrderDoesNotImplyLiveOrderAccess() {
        assertThat(tools.lookupDemoOrder("NOT-A-REAL-ORDER"))
                .contains("No matching demo order")
                .contains("not connected to a live order system");
    }

    @Test
    void knownOrderIsClearlyMarkedAsDemoData() {
        assertThat(tools.lookupDemoOrder("demo-1001"))
                .contains("DEMO DATA ONLY")
                .contains("Preparing");
    }
}
