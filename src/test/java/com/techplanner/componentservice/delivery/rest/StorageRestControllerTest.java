package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.delivery.exception.GlobalExceptionHandler;
import com.techplanner.componentservice.domain.entities.Storage;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.services.IStorageService;
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

@WebMvcTest(StorageRestController.class)
@Import(GlobalExceptionHandler.class)
class StorageRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IStorageService storageService;

    @Test
    @DisplayName("GET /storages retorna lista de Storage")
    void shouldGetStorages_whenRequested_returnOk() throws Exception {
        Storage storage = createStorage(20L, "Samsung", "990 PRO", 2000, "NVMe", "M.2", new BigDecimal("1799000"));
        when(storageService.findAll()).thenReturn(List.of(storage));

        mockMvc.perform(get("/api/v1/component-service/storages").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(20L));

        verify(storageService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /storages/{id} retorna Storage cuando existe")
    void shouldGetStorageById_whenExists_returnOk() throws Exception {
        Storage storage = createStorage(20L, "Samsung", "990 PRO", 2000, "NVMe", "M.2", new BigDecimal("1799000"));
        when(storageService.findById(20L)).thenReturn(storage);

        mockMvc.perform(get("/api/v1/component-service/storages/20").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("NVMe"));

        verify(storageService, times(1)).findById(20L);
    }

    @Test
    @DisplayName("GET /storages/{id} retorna 404 cuando no existe")
    void shouldGetStorageById_whenMissing_returnNotFound() throws Exception {
        when(storageService.findById(99L)).thenThrow(new ComponentNotFoundException(99L));

        mockMvc.perform(get("/api/v1/component-service/storages/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(storageService, times(1)).findById(99L);
    }

    @Test
    @DisplayName("POST /storages con body inválido retorna 400")
    void shouldCreateStorage_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/component-service/storages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"\",\"model\":\"990 PRO\",\"capacityGb\":2000,\"type\":\"NVMe\",\"interfaceType\":\"M.2\",\"price\":1799000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(storageService, never()).save(any());
    }

    @Test
    @DisplayName("POST /storages crea un Storage")
    void shouldCreateStorage_whenValid_returnCreated() throws Exception {
        Storage storage = createStorage(20L, "Samsung", "990 PRO", 2000, "NVMe", "M.2", new BigDecimal("1799000"));
        when(storageService.save(any(Storage.class))).thenReturn(storage);

        mockMvc.perform(post("/api/v1/component-service/storages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"Samsung\",\"model\":\"990 PRO\",\"capacityGb\":2000,\"type\":\"NVMe\",\"interfaceType\":\"M.2\",\"price\":1799000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20L));

        verify(storageService, times(1)).save(any(Storage.class));
    }

    @Test
    @DisplayName("PUT /storages/{id} actualiza un Storage")
    void shouldUpdateStorage_whenValid_returnOk() throws Exception {
        Storage storage = createStorage(20L, "WD", "Black SN850X", 1000, "NVMe", "M.2", new BigDecimal("999900"));
        when(storageService.update(eq(20L), any(Storage.class))).thenReturn(storage);

        mockMvc.perform(put("/api/v1/component-service/storages/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"WD\",\"model\":\"Black SN850X\",\"capacityGb\":1000,\"type\":\"NVMe\",\"interfaceType\":\"M.2\",\"price\":999900}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Black SN850X"));

        verify(storageService, times(1)).update(eq(20L), any(Storage.class));
    }

    @Test
    @DisplayName("PUT /storages/{id} con body inválido retorna 400")
    void shouldUpdateStorage_whenBodyInvalid_returnBadRequest() throws Exception {
        mockMvc.perform(put("/api/v1/component-service/storages/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"\",\"model\":\"Black SN850X\",\"capacityGb\":1000,\"type\":\"NVMe\",\"interfaceType\":\"M.2\",\"price\":999900}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("El campo 'brand' La marca no puede estar vacía"));

        verify(storageService, never()).update(any(), any());
    }

    @Test
    @DisplayName("DELETE /storages/{id} elimina un Storage")
    void shouldDeleteStorage_whenRequested_returnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/component-service/storages/20").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(storageService, times(1)).delete(20L);
    }

    private Storage createStorage(Long id, String brand, String model, Integer capacityGb, String type, String interfaceType, BigDecimal price) {
        Storage storage = new Storage();
        storage.setId(id);
        storage.setBrand(brand);
        storage.setModel(model);
        storage.setCapacityGb(capacityGb);
        storage.setType(type);
        storage.setInterfaceType(interfaceType);
        storage.setPrice(price);
        return storage;
    }
}