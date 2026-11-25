package com.fooddelivery.chatbot.repository;

import com.fooddelivery.chatbot.model.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    List<MenuProduct> findAllByAvailableTrueOrderByCategoryAscNameAsc();
}
