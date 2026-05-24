package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.delivery.exception.GlobalExceptionHandler;
import com.techplanner.componentservice.domain.entities.Motherboard;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.services.IMotherboardService;
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

@WebMvcTest(MotherboardRestController.class)
@Import(GlobalExceptionHandler.class)
class MotherboardRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMotherboardService motherboardService;

    @Test
    @DisplayName("GET /motherboards retorna lista de Motherboards")
    void shouldGetMotherboards_whenRequested_returnOk() throws Exception {
        Motherboard motherboard = createMotherboard(10L, "ASUS", "ROG Strix B650", "AM5", "DDR5", 128, "PCIe 5.0", new BigDecimal("1299000"));
        when(motherboardService.findAll()).thenReturn(List.of(motherboard));

        mockMvc.perform(get("/api/v1/component-service/motherboards").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L));

        verify(motherboardService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /motherboards/{id} retorna Motherboard cuando existe")
    void shouldGetMotherboardById_whenExists_returnOk() throws Exception {
        Motherboard motherboard = createMotherboard(10L, "ASUS", "ROG Strix B650", "AM5", "DDR5", 128, "PCIe 5.0", new BigDecimal("1299000"));
        when(motherboardService.findById(10L)).thenReturn(motherboard);

        mockMvc.perform(get("/api/v1/component-service/motherboards/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.socket").value("AM5"));

        verify(motherboardService, times(1)).findById(10L);
    }

    @Test
    @DisplayName("GET /motherboards/{id} retorna 404 cuando no existe")
    void shouldGetMotherboardById_whenMissing_returnNotFound() throws Exception {
        when(motherboardService.findById(99L)).thenThrow(new ComponentNotFoundException(99L));

        mockMvc.perform(get("/api/v1/component-service/motherboards/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(motherboardService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("POST /motherboards con body inválido retorna 400")
    void shouldCreateMotherboard_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/component-service/motherboards")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brand\":\"\",\"model\":\"ROG Strix B650\",\"socket\":\"AM5\",\"ramType\":\"DDR5\",\"maxRam\":128,\"pcieVersion\":\"PCIe 5.0\",\"price\":1299000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(motherboardService, never()).save(any());
    }

    @Test
    @DisplayName("POST /motherboards crea una Motherboard")
    void shouldCreateMotherboard_whenValid_returnCreated() throws Exception {
        Motherboard motherboard = createMotherboard(10L, "ASUS", "ROG Strix B650", "AM5", "DDR5", 128, "PCIe 5.0", new BigDecimal("1299000"));
        when(motherboardService.save(any(Motherboard.class))).thenReturn(motherboard);

        mockMvc.perform(post("/api/v1/component-service/motherboards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"ASUS\",\"model\":\"ROG Strix B650\",\"socket\":\"AM5\",\"ramType\":\"DDR5\",\"maxRam\":128,\"pcieVersion\":\"PCIe 5.0\",\"price\":1299000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L));

        verify(motherboardService, times(1)).save(any(Motherboard.class));
    }

    @Test
    @DisplayName("PUT /motherboards/{id} actualiza una Motherboard")
    void shouldUpdateMotherboard_whenValid_returnOk() throws Exception {
        Motherboard motherboard = createMotherboard(10L, "ASUS", "ROG Strix B650-E", "AM5", "DDR5", 192, "PCIe 5.0", new BigDecimal("1399000"));
        when(motherboardService.update(eq(10L), any(Motherboard.class))).thenReturn(motherboard);

        mockMvc.perform(put("/api/v1/component-service/motherboards/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"ASUS\",\"model\":\"ROG Strix B650-E\",\"socket\":\"AM5\",\"ramType\":\"DDR5\",\"maxRam\":192,\"pcieVersion\":\"PCIe 5.0\",\"price\":1399000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("ROG Strix B650-E"));

        verify(motherboardService, times(1)).update(eq(10L), any(Motherboard.class));
    }

    @Test
    @DisplayName("PUT /motherboards/{id} con body inválido retorna 400")
    void shouldUpdateMotherboard_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/component-service/motherboards/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"\",\"model\":\"ROG Strix B650-E\",\"socket\":\"AM5\",\"ramType\":\"DDR5\",\"maxRam\":192,\"pcieVersion\":\"PCIe 5.0\",\"price\":1399000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(motherboardService, never()).update(any(), any());
    }

    @Test
    @DisplayName("DELETE /motherboards/{id} elimina una Motherboard")
    void shouldDeleteMotherboard_whenRequested_returnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/component-service/motherboards/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(motherboardService, times(1)).delete(10L);
    }

    private Motherboard createMotherboard(Long id, String brand, String model, String socket, String ramType, Integer maxRam, String pcieVersion, BigDecimal price) {
        Motherboard motherboard = new Motherboard();
        motherboard.setId(id);
        motherboard.setBrand(brand);
        motherboard.setModel(model);
        motherboard.setSocket(socket);
        motherboard.setRamType(ramType);
        motherboard.setMaxRam(maxRam);
        motherboard.setPcieVersion(pcieVersion);
        motherboard.setPrice(price);
        return motherboard;
    }
}