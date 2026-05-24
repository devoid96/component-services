package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.delivery.exception.GlobalExceptionHandler;
import com.techplanner.componentservice.domain.entities.Cpu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.services.ICpuService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CpuRestController.class)
@Import(GlobalExceptionHandler.class)
class CpuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICpuService cpuService;

    @Test
    @DisplayName("GET /cpus retorna lista de CPUs")
    void shouldGetCpus_whenRequested_returnOk() throws Exception {
        Cpu cpu = createCpu(1L, "AMD", "Ryzen 7 7800X3D", "AM5", 8, 16, 120, new BigDecimal("2399000"));
        when(cpuService.findAll()).thenReturn(List.of(cpu));

        mockMvc.perform(get("/api/v1/component-service/cpus").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].brand").value("AMD"));

        verify(cpuService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /cpus/{id} retorna CPU cuando existe")
    void shouldGetCpuById_whenExists_returnOk() throws Exception {
        Cpu cpu = createCpu(1L, "AMD", "Ryzen 7 7800X3D", "AM5", 8, 16, 120, new BigDecimal("2399000"));
        when(cpuService.findById(1L)).thenReturn(cpu);

        mockMvc.perform(get("/api/v1/component-service/cpus/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.model").value("Ryzen 7 7800X3D"));

        verify(cpuService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /cpus/{id} retorna 404 cuando no existe")
    void shouldGetCpuById_whenMissing_returnNotFound() throws Exception {
        when(cpuService.findById(99L)).thenThrow(new ComponentNotFoundException(99L));

        mockMvc.perform(get("/api/v1/component-service/cpus/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        verify(cpuService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("POST /cpus con body inválido retorna 400")
    void shouldCreateCpu_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/component-service/cpus")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brand\":\"\",\"model\":\"Ryzen 7 7800X3D\",\"socket\":\"AM5\",\"cores\":8,\"threads\":16,\"tdp\":120,\"price\":2399000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(cpuService, never()).save(any());
    }

    @Test
    @DisplayName("POST /cpus crea una CPU")
    void shouldCreateCpu_whenValid_returnCreated() throws Exception {
        Cpu cpu = createCpu(1L, "AMD", "Ryzen 7 7800X3D", "AM5", 8, 16, 120, new BigDecimal("2399000"));
        when(cpuService.save(any(Cpu.class))).thenReturn(cpu);

        mockMvc.perform(post("/api/v1/component-service/cpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"AMD\",\"model\":\"Ryzen 7 7800X3D\",\"socket\":\"AM5\",\"cores\":8,\"threads\":16,\"tdp\":120,\"price\":2399000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.price").value(2399000));

        verify(cpuService, times(1)).save(any(Cpu.class));
    }

    @Test
    @DisplayName("PUT /cpus/{id} actualiza una CPU")
    void shouldUpdateCpu_whenValid_returnOk() throws Exception {
        Cpu cpu = createCpu(1L, "AMD", "Ryzen 9 9900X", "AM5", 12, 24, 170, new BigDecimal("2399000"));
        when(cpuService.update(eq(1L), any(Cpu.class))).thenReturn(cpu);

        mockMvc.perform(put("/api/v1/component-service/cpus/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"AMD\",\"model\":\"Ryzen 9 9900X\",\"socket\":\"AM5\",\"cores\":12,\"threads\":24,\"tdp\":170,\"price\":2399000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Ryzen 9 9900X"));

        verify(cpuService, times(1)).update(eq(1L), any(Cpu.class));
    }

    @Test
    @DisplayName("PUT /cpus/{id} con body inválido retorna 400")
    void shouldUpdateCpu_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/component-service/cpus/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"\",\"model\":\"Ryzen 9 9900X\",\"socket\":\"AM5\",\"cores\":12,\"threads\":24,\"tdp\":170,\"price\":2399000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(cpuService, never()).update(any(), any());
    }

    @Test
    @DisplayName("DELETE /cpus/{id} elimina una CPU")
    void shouldDeleteCpu_whenRequested_returnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/component-service/cpus/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cpuService, times(1)).delete(1L);
    }

    private Cpu createCpu(Long id, String brand, String model, String socket, Integer cores, Integer threads, Integer tdp, BigDecimal price) {
        Cpu cpu = new Cpu();
        cpu.setId(id);
        cpu.setBrand(brand);
        cpu.setModel(model);
        cpu.setSocket(socket);
        cpu.setCores(cores);
        cpu.setThreads(threads);
        cpu.setTdp(tdp);
        cpu.setPrice(price);
        return cpu;
    }
}