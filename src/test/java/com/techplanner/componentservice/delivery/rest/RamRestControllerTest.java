package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.delivery.exception.GlobalExceptionHandler;
import com.techplanner.componentservice.domain.entities.Ram;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.services.IRamService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RamRestController.class)
@Import(GlobalExceptionHandler.class)
class RamRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRamService ramService;

    @Test
    @DisplayName("GET /rams retorna lista de RAMs")
    void shouldGetRams_whenRequested_returnOk() throws Exception {
        Ram ram = createRam(7L, "Corsair", "Vengeance", "DDR5", 32, 6000, new BigDecimal("399900"));
        when(ramService.findAll()).thenReturn(List.of(ram));

        mockMvc.perform(get("/api/v1/component-service/rams").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(7L));

        verify(ramService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /rams/{id} retorna RAM cuando existe")
    void shouldGetRamById_whenExists_returnOk() throws Exception {
        Ram ram = createRam(7L, "Corsair", "Vengeance", "DDR5", 32, 6000, new BigDecimal("399900"));
        when(ramService.findById(7L)).thenReturn(ram);

        mockMvc.perform(get("/api/v1/component-service/rams/7").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("DDR5"));

        verify(ramService, times(1)).findById(7L);
    }

    @Test
    @DisplayName("GET /rams/{id} retorna 404 cuando no existe")
    void shouldGetRamById_whenMissing_returnNotFound() throws Exception {
        when(ramService.findById(99L)).thenThrow(new ComponentNotFoundException(99L));

        mockMvc.perform(get("/api/v1/component-service/rams/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(ramService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("POST /rams con body inválido retorna 400")
    void shouldCreateRam_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/component-service/rams")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brand\":\"\",\"model\":\"Vengeance\",\"type\":\"DDR5\",\"capacity\":32,\"speed\":6000,\"price\":399900}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(ramService, never()).save(any());
    }

    @Test
    @DisplayName("POST /rams crea una RAM")
    void shouldCreateRam_whenValid_returnCreated() throws Exception {
        Ram ram = createRam(7L, "Corsair", "Vengeance", "DDR5", 32, 6000, new BigDecimal("399900"));
        when(ramService.save(any(Ram.class))).thenReturn(ram);

        mockMvc.perform(post("/api/v1/component-service/rams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"Corsair\",\"model\":\"Vengeance\",\"type\":\"DDR5\",\"capacity\":32,\"speed\":6000,\"price\":399900}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7L));

        verify(ramService, times(1)).save(any(Ram.class));
    }

    @Test
    @DisplayName("PUT /rams/{id} actualiza una RAM")
    void shouldUpdateRam_whenValid_returnOk() throws Exception {
        Ram ram = createRam(7L, "Corsair", "Vengeance RGB", "DDR5", 32, 6400, new BigDecimal("449900"));
        when(ramService.update(eq(7L), any(Ram.class))).thenReturn(ram);

        mockMvc.perform(put("/api/v1/component-service/rams/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"Corsair\",\"model\":\"Vengeance RGB\",\"type\":\"DDR5\",\"capacity\":32,\"speed\":6400,\"price\":449900}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Vengeance RGB"));

        verify(ramService, times(1)).update(eq(7L), any(Ram.class));
    }

    @Test
    @DisplayName("PUT /rams/{id} con body inválido retorna 400")
    void shouldUpdateRam_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/component-service/rams/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"\",\"model\":\"Vengeance RGB\",\"type\":\"DDR5\",\"capacity\":32,\"speed\":6400,\"price\":449900}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(ramService, never()).update(any(), any());
    }

    @Test
    @DisplayName("DELETE /rams/{id} elimina una RAM")
    void shouldDeleteRam_whenRequested_returnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/component-service/rams/7").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(ramService, times(1)).delete(7L);
    }

    private Ram createRam(Long id, String brand, String model, String type, Integer capacity, Integer speed, BigDecimal price) {
        Ram ram = new Ram();
        ram.setId(id);
        ram.setBrand(brand);
        ram.setModel(model);
        ram.setType(type);
        ram.setCapacity(capacity);
        ram.setSpeed(speed);
        ram.setPrice(price);
        return ram;
    }
}