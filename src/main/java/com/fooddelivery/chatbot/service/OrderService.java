package com.fooddelivery.chatbot.service;

import com.fooddelivery.chatbot.common.ResourceNotFoundException;
import com.fooddelivery.chatbot.model.CreateOrderItemRequest;
import com.fooddelivery.chatbot.model.CreateOrderRequest;
import com.fooddelivery.chatbot.model.FoodOrder;
import com.fooddelivery.chatbot.model.MenuProduct;
import com.fooddelivery.chatbot.model.OrderLine;
import com.fooddelivery.chatbot.model.OrderLineResponse;
import com.fooddelivery.chatbot.model.OrderResponse;
import com.fooddelivery.chatbot.repository.FoodOrderRepository;
import com.fooddelivery.chatbot.repository.MenuProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class OrderService {

    private final MenuProductRepository menuProductRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;

    public OrderService(
            MenuProductRepository menuProductRepository,
            FoodOrderRepository foodOrderRepository,
            DeliveryService deliveryService,
            PaymentService paymentService) {
        this.menuProductRepository = menuProductRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.deliveryService = deliveryService;
        this.paymentService = paymentService;
    }

    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest request) {
        List<ResolvedItem> resolvedItems = request.items().stream()
                .map(this::resolveItem)
                .toList();
        BigDecimal subtotal = resolvedItems.stream()
                .map(item -> item.product().getPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (subtotal.compareTo(deliveryService.getMinimumSubtotal()) < 0) {
            throw new IllegalArgumentException("The minimum food subtotal is $15.00.");
        }

        BigDecimal deliveryFee = deliveryService.getDeliveryFee();
        FoodOrder order = new FoodOrder(
                newOrderCode(),
                request.customerName().trim(),
                request.deliveryAddress().trim(),
                "PLACED",
                paymentService.processDemoPayment(request.paymentMethod()),
                subtotal,
                deliveryFee,
                subtotal.add(deliveryFee));
        resolvedItems.forEach(item -> order.addItem(item.product(), item.quantity()));

        return toResponse(foodOrderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public OrderResponse trackOrder(String orderCode) {
        FoodOrder order = foodOrderRepository.findByOrderCodeIgnoreCase(orderCode.trim())
                .orElseThrow(() -> new ResourceNotFoundException("No order found for that tracking code."));
        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public String lookupStatusForAssistant(String orderCode) {
        return foodOrderRepository.findByOrderCodeIgnoreCase(orderCode.trim())
                .map(order -> "DEMO DATA ONLY — Order %s is %s. Estimated delivery: %s."
                        .formatted(order.getOrderCode(), order.getStatus(), deliveryService.getEstimatedDelivery()))
                .orElse("No matching order. This demo has no live delivery integration.");
    }

    private ResolvedItem resolveItem(CreateOrderItemRequest request) {
        MenuProduct product = menuProductRepository.findById(request.menuItemId())
                .filter(MenuProduct::isAvailable)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item " + request.menuItemId() + " was not found or is unavailable."));
        return new ResolvedItem(product, request.quantity());
    }

    private OrderResponse toResponse(FoodOrder order) {
        List<OrderLineResponse> items = order.getItems().stream()
                .map(item -> new OrderLineResponse(item.getProductName(), item.getQuantity(), item.getUnitPrice()))
                .toList();
        return new OrderResponse(
                order.getOrderCode(),
                order.getStatus(),
                order.getPaymentStatus(),
                order.getSubtotal(),
                order.getDeliveryFee(),
                order.getTotal(),
                deliveryService.getEstimatedDelivery(),
                order.getCreatedAt(),
                items);
    }

    private String newOrderCode() {
        return "FD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12)
                .toUpperCase(Locale.ROOT);
    }

    private record ResolvedItem(MenuProduct product, int quantity) {
    }
}
