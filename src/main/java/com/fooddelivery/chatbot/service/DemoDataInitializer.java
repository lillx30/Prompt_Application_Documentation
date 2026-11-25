package com.fooddelivery.chatbot.service;

import com.fooddelivery.chatbot.model.MenuProduct;
import com.fooddelivery.chatbot.repository.MenuProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DemoDataInitializer implements CommandLineRunner {

    private final MenuProductRepository menuProductRepository;

    public DemoDataInitializer(MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    @Override
    public void run(String... args) {
        if (menuProductRepository.count() > 0) {
            return;
        }

        menuProductRepository.saveAll(List.of(
                product("Classic Burger", "Beef patty, lettuce, tomato, and house sauce",
                        "12.50", "Burgers", "photo-1568901346375-23c9450c58cd"),
                product("Veggie Wrap", "Grilled vegetables, hummus, and greens",
                        "9.00", "Wraps", "photo-1626700051175-6818013e1d4f"),
                product("Margherita Pizza", "Tomato, mozzarella, and fresh basil",
                        "14.00", "Pizza", "photo-1571407970349-bc81e7e96d47"),
                product("Garden Salad", "Mixed greens, cucumber, tomato, and vinaigrette",
                        "8.50", "Salads", "photo-1512621776951-a57141f2eefd"),
                product("Crispy Chicken Sandwich", "Crispy chicken, slaw, and house pickles",
                        "13.50", "Sandwiches", "photo-1606755962773-d324e0a13086"),
                product("Golden Fries", "Crispy seasoned potato fries",
                        "4.50", "Sides", "photo-1630384060421-cb20d0e0649d"),
                product("Tomato Basil Pasta", "Pasta tossed with tomato, basil, and parmesan",
                        "13.00", "Pasta", "photo-1473093295043-cdd812d0e601"),
                product("Fresh Lemonade", "House lemonade served chilled",
                        "3.00", "Drinks", "photo-1513558161293-cdaf765edfd4")
        ));
    }

    private MenuProduct product(String name, String description, String price, String category, String imageId) {
        String imageUrl = "https://images.unsplash.com/%s?auto=format&fit=crop&w=900&q=85".formatted(imageId);
        return new MenuProduct(name, description, new BigDecimal(price), category, imageUrl);
    }
}
