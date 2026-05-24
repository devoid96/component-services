package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Storage;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IStorageDao;
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
class StorageServiceImplTest {

    @Mock
    private IStorageDao storageDao;

    @InjectMocks
    private StorageServiceImpl storageService;

    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = createStorage(20L, "Samsung", "990 PRO", 2000, "NVMe", "M.2", new BigDecimal("1799000"));
    }

    @Test
    @DisplayName("findAll retorna lista vacía")
    void shouldFindAll_whenNoRecords_returnEmptyList() {
        when(storageDao.findAll()).thenReturn(Collections.emptyList());

        List<Storage> result = storageService.findAll();

        assertThat(result).isEmpty();
        verify(storageDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll retorna lista con datos")
    void shouldFindAll_whenRecordsExist_returnListWithData() {
        Storage other = createStorage(21L, "WD", "Black SN850X", 1000, "NVMe", "M.2", new BigDecimal("999900"));
        when(storageDao.findAll()).thenReturn(List.of(storage, other));

        List<Storage> result = storageService.findAll();

        assertThat(result).hasSize(2);
        verify(storageDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna Storage cuando existe")
    void shouldFindById_whenExists_returnEntity() {
        when(storageDao.findById(20L)).thenReturn(Optional.of(storage));

        Storage result = storageService.findById(20L);

        assertThat(result.getBrand()).isEqualTo("Samsung");
        verify(storageDao, times(1)).findById(20L);
    }

    @Test
    @DisplayName("findById lanza ComponentNotFoundException cuando no existe")
    void shouldFindById_whenMissing_throwNotFound() {
        when(storageDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storageService.findById(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");
    }

    @Test
    @DisplayName("findById convierte DataAccessException en ComponentServiceException")
    void shouldFindById_whenDataAccessFails_throwServiceException() {
        when(storageDao.findById(20L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> storageService.findById(20L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al consultar el almacenamiento en la base de datos");
    }

    @Test
    @DisplayName("save guarda el Storage correctamente")
    void shouldSave_whenValidEntity_saveAndReturnEntity() {
        when(storageDao.save(any(Storage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Storage result = storageService.save(storage);

        assertThat(result.getCapacityGb()).isEqualTo(2000);
        verify(storageDao, times(1)).save(storage);
    }

    @Test
    @DisplayName("save convierte DataIntegrityViolationException en ComponentServiceException")
    void shouldSave_whenIntegrityViolation_throwServiceException() {
        when(storageDao.save(any(Storage.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> storageService.save(storage))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el componente: datos duplicados o restricción violada");
    }

    @Test
    @DisplayName("save convierte DataAccessException general en ComponentServiceException")
    void shouldSave_whenDataAccessFails_throwServiceException() {
        when(storageDao.save(any(Storage.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> storageService.save(storage))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el almacenamiento en la base de datos");
    }

    @Test
    @DisplayName("update actualiza campos cuando el Storage existe")
    void shouldUpdate_whenExists_updateAndReturnEntity() {
        Storage input = createStorage(20L, "WD", "Black SN850X", 1000, "NVMe", "M.2", new BigDecimal("999900"));
        when(storageDao.findById(20L)).thenReturn(Optional.of(storage));
        when(storageDao.save(any(Storage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Storage result = storageService.update(20L, input);

        assertThat(result.getModel()).isEqualTo("Black SN850X");
        assertThat(result.getCapacityGb()).isEqualTo(1000);
        verify(storageDao, times(1)).findById(20L);
        verify(storageDao, times(1)).save(storage);
    }

    @Test
    @DisplayName("update lanza ComponentNotFoundException cuando el Storage no existe")
    void shouldUpdate_whenMissing_throwNotFound() {
        when(storageDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storageService.update(99L, storage))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(storageDao, times(1)).findById(99L);
        verify(storageDao, never()).save(any());
    }

    @Test
    @DisplayName("update convierte DataAccessException en ComponentServiceException")
    void shouldUpdate_whenDataAccessFails_throwServiceException() {
        when(storageDao.findById(20L)).thenReturn(Optional.of(storage));
        when(storageDao.save(any(Storage.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> storageService.update(20L, storage))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al actualizar el almacenamiento en la base de datos");
    }

    @Test
    @DisplayName("delete elimina el Storage cuando existe")
    void shouldDelete_whenExists_deleteById() {
        when(storageDao.existsById(20L)).thenReturn(true);

        storageService.delete(20L);

        verify(storageDao, times(1)).existsById(20L);
        verify(storageDao, times(1)).deleteById(20L);
    }

    @Test
    @DisplayName("delete lanza ComponentNotFoundException cuando el Storage no existe")
    void shouldDelete_whenMissing_throwNotFound() {
        when(storageDao.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> storageService.delete(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(storageDao, times(1)).existsById(99L);
        verify(storageDao, never()).deleteById(99L);
    }

    @Test
    @DisplayName("delete convierte DataAccessException en ComponentServiceException")
    void shouldDelete_whenDataAccessFails_throwServiceException() {
        when(storageDao.existsById(20L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> storageService.delete(20L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al eliminar el almacenamiento en la base de datos");

        verify(storageDao, times(1)).existsById(20L);
        verify(storageDao, never()).deleteById(any());
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