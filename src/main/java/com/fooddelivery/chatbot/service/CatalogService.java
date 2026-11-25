package com.fooddelivery.chatbot.service;

import com.fooddelivery.chatbot.model.MenuItem;
import com.fooddelivery.chatbot.model.MenuProduct;
import com.fooddelivery.chatbot.repository.MenuProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class CatalogService {

    private final MenuProductRepository menuProductRepository;

    public CatalogService(MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getMenu(String category) {
        return menuProductRepository.findAllByAvailableTrueOrderByCategoryAscNameAsc().stream()
                .filter(item -> category == null || category.isBlank()
                        || item.getCategory().equalsIgnoreCase(category.trim()))
                .map(this::toMenuItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MenuItem> searchMenu(String query) {
        String search = query == null ? "" : query.toLowerCase(Locale.ROOT).trim();
        return menuProductRepository.findAllByAvailableTrueOrderByCategoryAscNameAsc().stream()
                .filter(item -> search.isBlank()
                        || item.getName().toLowerCase(Locale.ROOT).contains(search)
                        || item.getCategory().toLowerCase(Locale.ROOT).contains(search)
                        || item.getDescription().toLowerCase(Locale.ROOT).contains(search))
                .map(this::toMenuItem)
                .toList();
    }

    private MenuItem toMenuItem(MenuProduct item) {
        return new MenuItem(item.getId(), item.getName(), item.getDescription(),
                item.getPrice(), item.getCategory(), item.getImageUrl());
    }
}
