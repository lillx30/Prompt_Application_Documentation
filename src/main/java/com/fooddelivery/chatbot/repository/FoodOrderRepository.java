package com.fooddelivery.chatbot.repository;

import com.fooddelivery.chatbot.model.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    Optional<FoodOrder> findByOrderCodeIgnoreCase(String orderCode);
}
