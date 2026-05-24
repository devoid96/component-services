package com.techplanner.componentservice.domain.exception;

import com.techplanner.componentservice.delivery.exception.ApiErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionClassesTest {

    @Test
    @DisplayName("ComponentNotFoundException conserva id y mensaje")
    void shouldCreateComponentNotFoundException_whenConstructed_keepIdAndMessage() {
        ComponentNotFoundException exception = new ComponentNotFoundException(1L);

        assertThat(exception.getId()).isEqualTo(1L);
        assertThat(exception.getMessage()).isEqualTo("Componente con id 1 no encontrado");
    }

    @Test
    @DisplayName("ComponentServiceException conserva mensaje y causa")
    void shouldCreateComponentServiceException_whenConstructed_keepMessageAndCause() {
        RuntimeException cause = new RuntimeException("causa");
        ComponentServiceException exception = new ComponentServiceException("fallo", cause);

        assertThat(exception.getMessage()).isEqualTo("fallo");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    @DisplayName("ApiErrorResponse conserva campos y timestamp")
    void shouldCreateApiErrorResponse_whenConstructed_keepFieldsAndTimestamp() {
        ApiErrorResponse response = new ApiErrorResponse(404, "Not Found", "No encontrado");

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getError()).isEqualTo("Not Found");
        assertThat(response.getMensaje()).isEqualTo("No encontrado");
        assertThat(response.getTimestamp()).isNotNull().isInstanceOf(LocalDateTime.class);
    }
}