package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Gpu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IGpuDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GpuServiceImplTest {

    @Mock
    private IGpuDao gpuDao;

    @InjectMocks
    private GpuServiceImpl gpuService;

    private Gpu gpu;

    @BeforeEach
    void setUp() {
        gpu = createGpu(4L, "NVIDIA", "RTX 4070 Ti", 12, 285, "PCIe 4.0", new BigDecimal("4299000"));
    }

    @Test
    @DisplayName("findAll retorna lista vacía")
    void shouldFindAll_whenNoRecords_returnEmptyList() {
        when(gpuDao.findAll()).thenReturn(Collections.emptyList());

        List<Gpu> result = gpuService.findAll();

        assertThat(result).isEmpty();
        verify(gpuDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll retorna lista con datos")
    void shouldFindAll_whenRecordsExist_returnListWithData() {
        Gpu other = createGpu(5L, "AMD", "RX 7800 XT", 16, 263, "PCIe 4.0", new BigDecimal("3199000"));
        when(gpuDao.findAll()).thenReturn(List.of(gpu, other));

        List<Gpu> result = gpuService.findAll();

        assertThat(result).hasSize(2);
        verify(gpuDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna GPU cuando existe")
    void shouldFindById_whenExists_returnEntity() {
        when(gpuDao.findById(4L)).thenReturn(Optional.of(gpu));

        Gpu result = gpuService.findById(4L);

        assertThat(result.getBrand()).isEqualTo("NVIDIA");
        verify(gpuDao, times(1)).findById(4L);
    }

    @Test
    @DisplayName("findById lanza ComponentNotFoundException cuando no existe")
    void shouldFindById_whenMissing_throwNotFound() {
        when(gpuDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gpuService.findById(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");
    }

    @Test
    @DisplayName("findById convierte DataAccessException en ComponentServiceException")
    void shouldFindById_whenDataAccessFails_throwServiceException() {
        when(gpuDao.findById(4L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> gpuService.findById(4L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al consultar la GPU en la base de datos");
    }

    @Test
    @DisplayName("save guarda la GPU correctamente")
    void shouldSave_whenValidEntity_saveAndReturnEntity() {
        when(gpuDao.save(any(Gpu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Gpu result = gpuService.save(gpu);

        assertThat(result.getPcieVersion()).isEqualTo("PCIe 4.0");
        verify(gpuDao, times(1)).save(gpu);
    }

    @Test
    @DisplayName("save convierte DataIntegrityViolationException en ComponentServiceException")
    void shouldSave_whenIntegrityViolation_throwServiceException() {
        when(gpuDao.save(any(Gpu.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> gpuService.save(gpu))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el componente: datos duplicados o restricción violada");
    }

    @Test
    @DisplayName("save convierte DataAccessException general en ComponentServiceException")
    void shouldSave_whenDataAccessFails_throwServiceException() {
        when(gpuDao.save(any(Gpu.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> gpuService.save(gpu))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar la GPU en la base de datos");
    }

    @Test
    @DisplayName("update actualiza campos cuando la GPU existe")
    void shouldUpdate_whenExists_updateAndReturnEntity() {
        Gpu input = createGpu(4L, "NVIDIA", "RTX 4070 Ti SUPER", 16, 285, "PCIe 4.0", new BigDecimal("4499000"));
        when(gpuDao.findById(4L)).thenReturn(Optional.of(gpu));
        when(gpuDao.save(any(Gpu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Gpu result = gpuService.update(4L, input);

        assertThat(result.getModel()).isEqualTo("RTX 4070 Ti SUPER");
        assertThat(result.getVram()).isEqualTo(16);
        verify(gpuDao, times(1)).findById(4L);
        verify(gpuDao, times(1)).save(gpu);
    }

    @Test
    @DisplayName("update lanza ComponentNotFoundException cuando la GPU no existe")
    void shouldUpdate_whenMissing_throwNotFound() {
        when(gpuDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gpuService.update(99L, gpu))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(gpuDao, times(1)).findById(99L);
        verify(gpuDao, never()).save(any());
    }

    @Test
    @DisplayName("update convierte DataAccessException en ComponentServiceException")
    void shouldUpdate_whenDataAccessFails_throwServiceException() {
        when(gpuDao.findById(4L)).thenReturn(Optional.of(gpu));
        when(gpuDao.save(any(Gpu.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> gpuService.update(4L, gpu))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al actualizar la GPU en la base de datos");
    }

    @Test
    @DisplayName("delete elimina la GPU cuando existe")
    void shouldDelete_whenExists_deleteById() {
        when(gpuDao.existsById(4L)).thenReturn(true);

        gpuService.delete(4L);

        verify(gpuDao, times(1)).existsById(4L);
        verify(gpuDao, times(1)).deleteById(4L);
    }

    @Test
    @DisplayName("delete lanza ComponentNotFoundException cuando la GPU no existe")
    void shouldDelete_whenMissing_throwNotFound() {
        when(gpuDao.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> gpuService.delete(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(gpuDao, times(1)).existsById(99L);
        verify(gpuDao, never()).deleteById(99L);
    }

    @Test
    @DisplayName("delete convierte DataAccessException en ComponentServiceException")
    void shouldDelete_whenDataAccessFails_throwServiceException() {
        when(gpuDao.existsById(4L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> gpuService.delete(4L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al eliminar la GPU en la base de datos");

        verify(gpuDao, times(1)).existsById(4L);
        verify(gpuDao, never()).deleteById(any());
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