package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Motherboard;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IMotherboardDao;
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
class MotherboardServiceImplTest {

    @Mock
    private IMotherboardDao motherboardDao;

    @InjectMocks
    private MotherboardServiceImpl motherboardService;

    private Motherboard motherboard;

    @BeforeEach
    void setUp() {
        motherboard = createMotherboard(10L, "ASUS", "ROG Strix B650", "AM5", "DDR5", 128, "PCIe 5.0", new BigDecimal("1299000"));
    }

    @Test
    @DisplayName("findAll retorna lista vacía")
    void shouldFindAll_whenNoRecords_returnEmptyList() {
        when(motherboardDao.findAll()).thenReturn(Collections.emptyList());

        List<Motherboard> result = motherboardService.findAll();

        assertThat(result).isEmpty();
        verify(motherboardDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll retorna lista con datos")
    void shouldFindAll_whenRecordsExist_returnListWithData() {
        Motherboard other = createMotherboard(11L, "MSI", "Z790 Tomahawk", "LGA1700", "DDR5", 192, "PCIe 5.0", new BigDecimal("1599000"));
        when(motherboardDao.findAll()).thenReturn(List.of(motherboard, other));

        List<Motherboard> result = motherboardService.findAll();

        assertThat(result).hasSize(2);
        verify(motherboardDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna Motherboard cuando existe")
    void shouldFindById_whenExists_returnEntity() {
        when(motherboardDao.findById(10L)).thenReturn(Optional.of(motherboard));

        Motherboard result = motherboardService.findById(10L);

        assertThat(result.getBrand()).isEqualTo("ASUS");
        verify(motherboardDao, times(1)).findById(10L);
    }

    @Test
    @DisplayName("findById lanza ComponentNotFoundException cuando no existe")
    void shouldFindById_whenMissing_throwNotFound() {
        when(motherboardDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> motherboardService.findById(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");
    }

    @Test
    @DisplayName("findById convierte DataAccessException en ComponentServiceException")
    void shouldFindById_whenDataAccessFails_throwServiceException() {
        when(motherboardDao.findById(10L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> motherboardService.findById(10L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al consultar la Motherboard en la base de datos");
    }

    @Test
    @DisplayName("save guarda la Motherboard correctamente")
    void shouldSave_whenValidEntity_saveAndReturnEntity() {
        when(motherboardDao.save(any(Motherboard.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Motherboard result = motherboardService.save(motherboard);

        assertThat(result.getMaxRam()).isEqualTo(128);
        verify(motherboardDao, times(1)).save(motherboard);
    }

    @Test
    @DisplayName("save convierte DataIntegrityViolationException en ComponentServiceException")
    void shouldSave_whenIntegrityViolation_throwServiceException() {
        when(motherboardDao.save(any(Motherboard.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> motherboardService.save(motherboard))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el componente: datos duplicados o restricción violada");
    }

    @Test
    @DisplayName("save convierte DataAccessException general en ComponentServiceException")
    void shouldSave_whenDataAccessFails_throwServiceException() {
        when(motherboardDao.save(any(Motherboard.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> motherboardService.save(motherboard))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar la Motherboard en la base de datos");
    }

    @Test
    @DisplayName("update actualiza campos cuando la Motherboard existe")
    void shouldUpdate_whenExists_updateAndReturnEntity() {
        Motherboard input = createMotherboard(10L, "ASUS", "ROG Strix B650-E", "AM5", "DDR5", 192, "PCIe 5.0", new BigDecimal("1399000"));
        when(motherboardDao.findById(10L)).thenReturn(Optional.of(motherboard));
        when(motherboardDao.save(any(Motherboard.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Motherboard result = motherboardService.update(10L, input);

        assertThat(result.getModel()).isEqualTo("ROG Strix B650-E");
        assertThat(result.getMaxRam()).isEqualTo(192);
        verify(motherboardDao, times(1)).findById(10L);
        verify(motherboardDao, times(1)).save(motherboard);
    }

    @Test
    @DisplayName("update lanza ComponentNotFoundException cuando la Motherboard no existe")
    void shouldUpdate_whenMissing_throwNotFound() {
        when(motherboardDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> motherboardService.update(99L, motherboard))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(motherboardDao, times(1)).findById(99L);
        verify(motherboardDao, never()).save(any());
    }

    @Test
    @DisplayName("update convierte DataAccessException en ComponentServiceException")
    void shouldUpdate_whenDataAccessFails_throwServiceException() {
        when(motherboardDao.findById(10L)).thenReturn(Optional.of(motherboard));
        when(motherboardDao.save(any(Motherboard.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> motherboardService.update(10L, motherboard))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al actualizar la Motherboard en la base de datos");
    }

    @Test
    @DisplayName("delete elimina la Motherboard cuando existe")
    void shouldDelete_whenExists_deleteById() {
        when(motherboardDao.existsById(10L)).thenReturn(true);

        motherboardService.delete(10L);

        verify(motherboardDao, times(1)).existsById(10L);
        verify(motherboardDao, times(1)).deleteById(10L);
    }

    @Test
    @DisplayName("delete lanza ComponentNotFoundException cuando la Motherboard no existe")
    void shouldDelete_whenMissing_throwNotFound() {
        when(motherboardDao.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> motherboardService.delete(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(motherboardDao, times(1)).existsById(99L);
        verify(motherboardDao, never()).deleteById(99L);
    }

    @Test
    @DisplayName("delete convierte DataAccessException en ComponentServiceException")
    void shouldDelete_whenDataAccessFails_throwServiceException() {
        when(motherboardDao.existsById(10L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> motherboardService.delete(10L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al eliminar la Motherboard en la base de datos");

        verify(motherboardDao, times(1)).existsById(10L);
        verify(motherboardDao, never()).deleteById(any());
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