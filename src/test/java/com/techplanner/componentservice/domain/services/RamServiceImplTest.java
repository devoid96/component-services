package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Ram;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IRamDao;
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
class RamServiceImplTest {

    @Mock
    private IRamDao ramDao;

    @InjectMocks
    private RamServiceImpl ramService;

    private Ram ram;

    @BeforeEach
    void setUp() {
        ram = createRam(7L, "Corsair", "Vengeance", "DDR5", 32, 6000, new BigDecimal("399900"));
    }

    @Test
    @DisplayName("findAll retorna lista vacía")
    void shouldFindAll_whenNoRecords_returnEmptyList() {
        when(ramDao.findAll()).thenReturn(Collections.emptyList());

        List<Ram> result = ramService.findAll();

        assertThat(result).isEmpty();
        verify(ramDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll retorna lista con datos")
    void shouldFindAll_whenRecordsExist_returnListWithData() {
        Ram other = createRam(8L, "Kingston", "Fury Beast", "DDR4", 16, 3200, new BigDecimal("199900"));
        when(ramDao.findAll()).thenReturn(List.of(ram, other));

        List<Ram> result = ramService.findAll();

        assertThat(result).hasSize(2);
        verify(ramDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna RAM cuando existe")
    void shouldFindById_whenExists_returnEntity() {
        when(ramDao.findById(7L)).thenReturn(Optional.of(ram));

        Ram result = ramService.findById(7L);

        assertThat(result.getBrand()).isEqualTo("Corsair");
        verify(ramDao, times(1)).findById(7L);
    }

    @Test
    @DisplayName("findById lanza ComponentNotFoundException cuando no existe")
    void shouldFindById_whenMissing_throwNotFound() {
        when(ramDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ramService.findById(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");
    }

    @Test
    @DisplayName("findById convierte DataAccessException en ComponentServiceException")
    void shouldFindById_whenDataAccessFails_throwServiceException() {
        when(ramDao.findById(7L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> ramService.findById(7L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al consultar la RAM en la base de datos");
    }

    @Test
    @DisplayName("save guarda la RAM correctamente")
    void shouldSave_whenValidEntity_saveAndReturnEntity() {
        when(ramDao.save(any(Ram.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ram result = ramService.save(ram);

        assertThat(result.getCapacity()).isEqualTo(32);
        verify(ramDao, times(1)).save(ram);
    }

    @Test
    @DisplayName("save convierte DataIntegrityViolationException en ComponentServiceException")
    void shouldSave_whenIntegrityViolation_throwServiceException() {
        when(ramDao.save(any(Ram.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> ramService.save(ram))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el componente: datos duplicados o restricción violada");
    }

    @Test
    @DisplayName("save convierte DataAccessException general en ComponentServiceException")
    void shouldSave_whenDataAccessFails_throwServiceException() {
        when(ramDao.save(any(Ram.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> ramService.save(ram))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar la RAM en la base de datos");
    }

    @Test
    @DisplayName("update actualiza campos cuando la RAM existe")
    void shouldUpdate_whenExists_updateAndReturnEntity() {
        Ram input = createRam(7L, "Corsair", "Vengeance RGB", "DDR5", 32, 6400, new BigDecimal("449900"));
        when(ramDao.findById(7L)).thenReturn(Optional.of(ram));
        when(ramDao.save(any(Ram.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ram result = ramService.update(7L, input);

        assertThat(result.getModel()).isEqualTo("Vengeance RGB");
        assertThat(result.getSpeed()).isEqualTo(6400);
        verify(ramDao, times(1)).findById(7L);
        verify(ramDao, times(1)).save(ram);
    }

    @Test
    @DisplayName("update lanza ComponentNotFoundException cuando la RAM no existe")
    void shouldUpdate_whenMissing_throwNotFound() {
        when(ramDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ramService.update(99L, ram))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(ramDao, times(1)).findById(99L);
        verify(ramDao, never()).save(any());
    }

    @Test
    @DisplayName("update convierte DataAccessException en ComponentServiceException")
    void shouldUpdate_whenDataAccessFails_throwServiceException() {
        when(ramDao.findById(7L)).thenReturn(Optional.of(ram));
        when(ramDao.save(any(Ram.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> ramService.update(7L, ram))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al actualizar la RAM en la base de datos");
    }

    @Test
    @DisplayName("delete elimina la RAM cuando existe")
    void shouldDelete_whenExists_deleteById() {
        when(ramDao.existsById(7L)).thenReturn(true);

        ramService.delete(7L);

        verify(ramDao, times(1)).existsById(7L);
        verify(ramDao, times(1)).deleteById(7L);
    }

    @Test
    @DisplayName("delete lanza ComponentNotFoundException cuando la RAM no existe")
    void shouldDelete_whenMissing_throwNotFound() {
        when(ramDao.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> ramService.delete(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(ramDao, times(1)).existsById(99L);
        verify(ramDao, never()).deleteById(99L);
    }

    @Test
    @DisplayName("delete convierte DataAccessException en ComponentServiceException")
    void shouldDelete_whenDataAccessFails_throwServiceException() {
        when(ramDao.existsById(7L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> ramService.delete(7L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al eliminar la RAM en la base de datos");

        verify(ramDao, times(1)).existsById(7L);
        verify(ramDao, never()).deleteById(any());
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