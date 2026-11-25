package com.fooddelivery.chatbot.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatRequestValidationTest {

    private static Validator validator;
    private static jakarta.validation.ValidatorFactory validatorFactory;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void acceptsNonBlankMessagesWithinTheLimit() {
        assertThat(validator.validate(new ChatRequest("What is on the menu?"))).isEmpty();
        assertThat(validator.validate(new ChatRequest("a".repeat(1000)))).isEmpty();
    }

    @Test
    void rejectsBlankAndOverlongMessages() {
        assertThat(validator.validate(new ChatRequest("  "))).hasSize(1);
        assertThat(validator.validate(new ChatRequest("a".repeat(1001)))).hasSize(1);
    }
}
