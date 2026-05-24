package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.delivery.exception.GlobalExceptionHandler;
import com.techplanner.componentservice.domain.entities.Gpu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.services.IGpuService;
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

@WebMvcTest(GpuRestController.class)
@Import(GlobalExceptionHandler.class)
class GpuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IGpuService gpuService;

    @Test
    @DisplayName("GET /gpus retorna lista de GPUs")
    void shouldGetGpus_whenRequested_returnOk() throws Exception {
        Gpu gpu = createGpu(4L, "NVIDIA", "RTX 4070 Ti", 12, 285, "PCIe 4.0", new BigDecimal("4299000"));
        when(gpuService.findAll()).thenReturn(List.of(gpu));

        mockMvc.perform(get("/api/v1/component-service/gpus").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4L));

        verify(gpuService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /gpus/{id} retorna GPU cuando existe")
    void shouldGetGpuById_whenExists_returnOk() throws Exception {
        Gpu gpu = createGpu(4L, "NVIDIA", "RTX 4070 Ti", 12, 285, "PCIe 4.0", new BigDecimal("4299000"));
        when(gpuService.findById(4L)).thenReturn(gpu);

        mockMvc.perform(get("/api/v1/component-service/gpus/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("RTX 4070 Ti"));

        verify(gpuService, times(1)).findById(4L);
    }

    @Test
    @DisplayName("GET /gpus/{id} retorna 404 cuando no existe")
    void shouldGetGpuById_whenMissing_returnNotFound() throws Exception {
        when(gpuService.findById(99L)).thenThrow(new ComponentNotFoundException(99L));

        mockMvc.perform(get("/api/v1/component-service/gpus/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(gpuService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("POST /gpus con body inválido retorna 400")
    void shouldCreateGpu_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/component-service/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brand\":\"\",\"model\":\"RTX 4070 Ti\",\"vram\":12,\"tdp\":285,\"pcieVersion\":\"PCIe 4.0\",\"price\":4299000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(gpuService, never()).save(any());
    }

    @Test
    @DisplayName("POST /gpus crea una GPU")
    void shouldCreateGpu_whenValid_returnCreated() throws Exception {
        Gpu gpu = createGpu(4L, "NVIDIA", "RTX 4070 Ti", 12, 285, "PCIe 4.0", new BigDecimal("4299000"));
        when(gpuService.save(any(Gpu.class))).thenReturn(gpu);

        mockMvc.perform(post("/api/v1/component-service/gpus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"NVIDIA\",\"model\":\"RTX 4070 Ti\",\"vram\":12,\"tdp\":285,\"pcieVersion\":\"PCIe 4.0\",\"price\":4299000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4L));

        verify(gpuService, times(1)).save(any(Gpu.class));
    }

    @Test
    @DisplayName("PUT /gpus/{id} actualiza una GPU")
    void shouldUpdateGpu_whenValid_returnOk() throws Exception {
        Gpu gpu = createGpu(4L, "NVIDIA", "RTX 4070 Ti SUPER", 16, 285, "PCIe 4.0", new BigDecimal("4499000"));
        when(gpuService.update(eq(4L), any(Gpu.class))).thenReturn(gpu);

        mockMvc.perform(put("/api/v1/component-service/gpus/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"NVIDIA\",\"model\":\"RTX 4070 Ti SUPER\",\"vram\":16,\"tdp\":285,\"pcieVersion\":\"PCIe 4.0\",\"price\":4499000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("RTX 4070 Ti SUPER"));

        verify(gpuService, times(1)).update(eq(4L), any(Gpu.class));
    }

    @Test
    @DisplayName("PUT /gpus/{id} con body inválido retorna 400")
    void shouldUpdateGpu_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/component-service/gpus/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"\",\"model\":\"RTX 4070 Ti SUPER\",\"vram\":16,\"tdp\":285,\"pcieVersion\":\"PCIe 4.0\",\"price\":4499000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(gpuService, never()).update(any(), any());
    }

    @Test
    @DisplayName("DELETE /gpus/{id} elimina una GPU")
    void shouldDeleteGpu_whenRequested_returnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/component-service/gpus/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(gpuService, times(1)).delete(4L);
    }

    private Gpu createGpu(Long id, String brand, String model, Integer vram, Integer tdp, String pcieVersion, BigDecimal price) {
        Gpu gpu = new Gpu();
        gpu.setId(id);
        gpu.setBrand(brand);
        gpu.setModel(model);
        gpu.setVram(vram);
        gpu.setTdp(tdp);
        gpu.setPcieVersion(pcieVersion);
        gpu.setPrice(price);
        return gpu;
    }
}