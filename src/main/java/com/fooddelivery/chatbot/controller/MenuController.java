package com.fooddelivery.chatbot.controller;

import com.fooddelivery.chatbot.model.MenuItem;
import com.fooddelivery.chatbot.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final CatalogService catalogService;

    public MenuController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<MenuItem> getMenu(@RequestParam(required = false) String category) {
        return catalogService.getMenu(category);
    }
}
