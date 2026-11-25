package com.fooddelivery.chatbot.controller;

import com.fooddelivery.chatbot.model.ChatRequest;
import com.fooddelivery.chatbot.model.ChatResponse;
import com.fooddelivery.chatbot.service.FoodDeliveryChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final FoodDeliveryChatService chatService;

    public ChatController(FoodDeliveryChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return new ChatResponse(chatService.replyTo(request.message()));
    }
}
