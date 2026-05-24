package com.techplanner.componentservice.delivery.exception;

import com.techplanner.componentservice.domain.entities.Component;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handleComponentNotFound retorna 404")
    void shouldHandleComponentNotFound_whenThrown_returnNotFound() {
        var response = handler.handleComponentNotFound(new ComponentNotFoundException(1L));

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).isEqualTo("Componente con id 1 no encontrado");
    }

    @Test
    @DisplayName("handleComponentService retorna 500 genérico")
    void shouldHandleComponentService_whenThrown_returnInternalServerError() {
        var response = handler.handleComponentService(new ComponentServiceException("fallo interno"));

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).isEqualTo("Ocurrio un error interno al procesar la solicitud");
    }

    @Test
    @DisplayName("handleValidation retorna mapa campo-mensaje")
    void shouldHandleValidation_whenInvalidBody_returnFieldErrorMap() throws Exception {
        Method method = DummyController.class.getDeclaredMethod("dummy", Component.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Component(), "component");
        bindingResult.addError(new FieldError("component", "brand", "La marca no puede estar vacía"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        var response = handler.handleValidation(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("brand", "La marca no puede estar vacía");
    }

    @Test
    @DisplayName("handleGeneric retorna 500 genérico")
    void shouldHandleGeneric_whenUnexpectedException_returnInternalServerError() {
        var response = handler.handleGeneric(new RuntimeException("boom"));

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).isEqualTo("Ocurrio un error interno al procesar la solicitud");
    }

    private static class DummyController {
        @SuppressWarnings("unused")
        void dummy(Component component) {
        }
    }
}