package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.domain.entities.Component;
import com.techplanner.componentservice.domain.services.IComponentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComponentRestController.class)
@Import(com.techplanner.componentservice.delivery.exception.GlobalExceptionHandler.class)
class ComponentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IComponentService componentService;

    @Test
    @DisplayName("GET /components retorna la lista de componentes")
    void shouldGetComponents_whenRequested_returnOkAndJson() throws Exception {
        Component component = new Component();
        component.setId(1L);
        component.setBrand("AMD");
        component.setModel("Ryzen 7 7800X3D");
        component.setPrice(new BigDecimal("2399000"));
        when(componentService.findAll()).thenReturn(List.of(component));

        mockMvc.perform(get("/api/v1/component-service/components").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].brand").value("AMD"));

        verify(componentService, times(1)).findAll();
    }
}