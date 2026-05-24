package com.techplanner.componentservice.delivery.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    @Test
    @DisplayName("ApiExceptionHandler se puede instanciar")
    void shouldInstantiateApiExceptionHandler_whenCreated_returnInstance() {
        ApiExceptionHandler handler = new ApiExceptionHandler();

        assertThat(handler).isNotNull();
    }
}