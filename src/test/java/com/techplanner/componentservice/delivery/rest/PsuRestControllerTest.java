package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.delivery.exception.GlobalExceptionHandler;
import com.techplanner.componentservice.domain.entities.Psu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.services.IPsuService;
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

@WebMvcTest(PsuRestController.class)
@Import(GlobalExceptionHandler.class)
class PsuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPsuService psuService;

    @Test
    @DisplayName("GET /psus retorna lista de PSUs")
    void shouldGetPsus_whenRequested_returnOk() throws Exception {
        Psu psu = createPsu(13L, "Corsair", "RM850x", 850, "80+ Gold", new BigDecimal("699900"));
        when(psuService.findAll()).thenReturn(List.of(psu));

        mockMvc.perform(get("/api/v1/component-service/psus").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(13L));

        verify(psuService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /psus/{id} retorna PSU cuando existe")
    void shouldGetPsuById_whenExists_returnOk() throws Exception {
        Psu psu = createPsu(13L, "Corsair", "RM850x", 850, "80+ Gold", new BigDecimal("699900"));
        when(psuService.findById(13L)).thenReturn(psu);

        mockMvc.perform(get("/api/v1/component-service/psus/13").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wattage").value(850));

        verify(psuService, times(1)).findById(13L);
    }

    @Test
    @DisplayName("GET /psus/{id} retorna 404 cuando no existe")
    void shouldGetPsuById_whenMissing_returnNotFound() throws Exception {
        when(psuService.findById(99L)).thenThrow(new ComponentNotFoundException(99L));

        mockMvc.perform(get("/api/v1/component-service/psus/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(psuService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("POST /psus con body inválido retorna 400")
    void shouldCreatePsu_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/component-service/psus")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brand\":\"\",\"model\":\"RM850x\",\"wattage\":850,\"efficiency\":\"80+ Gold\",\"price\":699900}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(psuService, never()).save(any());
    }

    @Test
    @DisplayName("POST /psus crea una PSU")
    void shouldCreatePsu_whenValid_returnCreated() throws Exception {
        Psu psu = createPsu(13L, "Corsair", "RM850x", 850, "80+ Gold", new BigDecimal("699900"));
        when(psuService.save(any(Psu.class))).thenReturn(psu);

        mockMvc.perform(post("/api/v1/component-service/psus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"Corsair\",\"model\":\"RM850x\",\"wattage\":850,\"efficiency\":\"80+ Gold\",\"price\":699900}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(13L));

        verify(psuService, times(1)).save(any(Psu.class));
    }

    @Test
    @DisplayName("PUT /psus/{id} actualiza una PSU")
    void shouldUpdatePsu_whenValid_returnOk() throws Exception {
        Psu psu = createPsu(13L, "Corsair", "RM850x SHIFT", 850, "80+ Gold", new BigDecimal("739900"));
        when(psuService.update(eq(13L), any(Psu.class))).thenReturn(psu);

        mockMvc.perform(put("/api/v1/component-service/psus/13")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"Corsair\",\"model\":\"RM850x SHIFT\",\"wattage\":850,\"efficiency\":\"80+ Gold\",\"price\":739900}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("RM850x SHIFT"));

        verify(psuService, times(1)).update(eq(13L), any(Psu.class));
    }

    @Test
    @DisplayName("PUT /psus/{id} con body inválido retorna 400")
    void shouldUpdatePsu_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/component-service/psus/13")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"\",\"model\":\"RM850x SHIFT\",\"wattage\":850,\"efficiency\":\"80+ Gold\",\"price\":739900}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(psuService, never()).update(any(), any());
    }

    @Test
    @DisplayName("DELETE /psus/{id} elimina una PSU")
    void shouldDeletePsu_whenRequested_returnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/component-service/psus/13").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(psuService, times(1)).delete(13L);
    }

    private Psu createPsu(Long id, String brand, String model, Integer wattage, String efficiency, BigDecimal price) {
        Psu psu = new Psu();
        psu.setId(id);
        psu.setBrand(brand);
        psu.setModel(model);
        psu.setWattage(wattage);
        psu.setEfficiency(efficiency);
        psu.setPrice(price);
        return psu;
    }
}