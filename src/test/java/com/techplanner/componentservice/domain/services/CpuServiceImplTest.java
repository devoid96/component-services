package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Cpu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.ICpuDao;
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
class CpuServiceImplTest {

    @Mock
    private ICpuDao cpuDao;

    @InjectMocks
    private CpuServiceImpl cpuService;

    private Cpu cpu;

    @BeforeEach
    void setUp() {
        cpu = createCpu(1L, "AMD", "Ryzen 7 7800X3D", "AM5", 8, 16, 120, new BigDecimal("2399000"));
    }

    @Test
    @DisplayName("findAll retorna lista vacía")
    void shouldFindAll_whenNoRecords_returnEmptyList() {
        when(cpuDao.findAll()).thenReturn(Collections.emptyList());

        List<Cpu> result = cpuService.findAll();

        assertThat(result).isEmpty();
        verify(cpuDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll retorna lista con datos")
    void shouldFindAll_whenRecordsExist_returnListWithData() {
        Cpu other = createCpu(2L, "Intel", "Core i7-14700K", "LGA1700", 20, 28, 125, new BigDecimal("2299000"));
        when(cpuDao.findAll()).thenReturn(List.of(cpu, other));

        List<Cpu> result = cpuService.findAll();

        assertThat(result).hasSize(2);
        verify(cpuDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna CPU cuando existe")
    void shouldFindById_whenExists_returnEntity() {
        when(cpuDao.findById(1L)).thenReturn(Optional.of(cpu));

        Cpu result = cpuService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getBrand()).isEqualTo("AMD");
        verify(cpuDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById lanza ComponentNotFoundException cuando no existe")
    void shouldFindById_whenMissing_throwNotFound() {
        when(cpuDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cpuService.findById(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(cpuDao, times(1)).findById(99L);
    }

    @Test
    @DisplayName("findById convierte DataAccessException en ComponentServiceException")
    void shouldFindById_whenDataAccessFails_throwServiceException() {
        when(cpuDao.findById(1L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> cpuService.findById(1L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al consultar el CPU en la base de datos");

        verify(cpuDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("save guarda el CPU correctamente")
    void shouldSave_whenValidEntity_saveAndReturnEntity() {
        when(cpuDao.save(any(Cpu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cpu result = cpuService.save(cpu);

        assertThat(result.getPrice()).isEqualByComparingTo("2399000");
        verify(cpuDao, times(1)).save(cpu);
    }

    @Test
    @DisplayName("save convierte DataIntegrityViolationException en ComponentServiceException")
    void shouldSave_whenIntegrityViolation_throwServiceException() {
        when(cpuDao.save(any(Cpu.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> cpuService.save(cpu))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el componente: datos duplicados o restricción violada");

        verify(cpuDao, times(1)).save(cpu);
    }

    @Test
    @DisplayName("save convierte DataAccessException general en ComponentServiceException")
    void shouldSave_whenDataAccessFails_throwServiceException() {
        when(cpuDao.save(any(Cpu.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> cpuService.save(cpu))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el CPU en la base de datos");

        verify(cpuDao, times(1)).save(cpu);
    }

    @Test
    @DisplayName("update actualiza campos cuando el CPU existe")
    void shouldUpdate_whenExists_updateAndReturnEntity() {
        Cpu input = createCpu(1L, "AMD", "Ryzen 9 9900X", "AM5", 12, 24, 170, new BigDecimal("2399000"));
        when(cpuDao.findById(1L)).thenReturn(Optional.of(cpu));
        when(cpuDao.save(any(Cpu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cpu result = cpuService.update(1L, input);

        assertThat(result.getModel()).isEqualTo("Ryzen 9 9900X");
        assertThat(result.getCores()).isEqualTo(12);
        assertThat(result.getThreads()).isEqualTo(24);
        assertThat(result.getTdp()).isEqualTo(170);
        verify(cpuDao, times(1)).findById(1L);
        verify(cpuDao, times(1)).save(cpu);
    }

    @Test
    @DisplayName("update lanza ComponentNotFoundException cuando el CPU no existe")
    void shouldUpdate_whenMissing_throwNotFound() {
        when(cpuDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cpuService.update(99L, cpu))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(cpuDao, times(1)).findById(99L);
        verify(cpuDao, never()).save(any());
    }

    @Test
    @DisplayName("update convierte DataAccessException en ComponentServiceException")
    void shouldUpdate_whenDataAccessFails_throwServiceException() {
        when(cpuDao.findById(1L)).thenReturn(Optional.of(cpu));
        when(cpuDao.save(any(Cpu.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> cpuService.update(1L, cpu))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al actualizar el CPU en la base de datos");

        verify(cpuDao, times(1)).findById(1L);
        verify(cpuDao, times(1)).save(cpu);
    }

    @Test
    @DisplayName("delete elimina el CPU cuando existe")
    void shouldDelete_whenExists_deleteById() {
        when(cpuDao.existsById(1L)).thenReturn(true);

        cpuService.delete(1L);

        verify(cpuDao, times(1)).existsById(1L);
        verify(cpuDao, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete lanza ComponentNotFoundException cuando el CPU no existe")
    void shouldDelete_whenMissing_throwNotFound() {
        when(cpuDao.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> cpuService.delete(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(cpuDao, times(1)).existsById(99L);
        verify(cpuDao, never()).deleteById(99L);
    }

    @Test
    @DisplayName("delete convierte DataAccessException en ComponentServiceException")
    void shouldDelete_whenDataAccessFails_throwServiceException() {
        when(cpuDao.existsById(1L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> cpuService.delete(1L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al eliminar el CPU en la base de datos");

        verify(cpuDao, times(1)).existsById(1L);
        verify(cpuDao, never()).deleteById(any());
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