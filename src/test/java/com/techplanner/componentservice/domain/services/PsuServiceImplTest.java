package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Psu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IPsuDao;
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
class PsuServiceImplTest {

    @Mock
    private IPsuDao psuDao;

    @InjectMocks
    private PsuServiceImpl psuService;

    private Psu psu;

    @BeforeEach
    void setUp() {
        psu = createPsu(13L, "Corsair", "RM850x", 850, "80+ Gold", new BigDecimal("699900"));
    }

    @Test
    @DisplayName("findAll retorna lista vacía")
    void shouldFindAll_whenNoRecords_returnEmptyList() {
        when(psuDao.findAll()).thenReturn(Collections.emptyList());

        List<Psu> result = psuService.findAll();

        assertThat(result).isEmpty();
        verify(psuDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll retorna lista con datos")
    void shouldFindAll_whenRecordsExist_returnListWithData() {
        Psu other = createPsu(14L, "EVGA", "SuperNOVA 750", 750, "80+ Gold", new BigDecimal("549900"));
        when(psuDao.findAll()).thenReturn(List.of(psu, other));

        List<Psu> result = psuService.findAll();

        assertThat(result).hasSize(2);
        verify(psuDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna PSU cuando existe")
    void shouldFindById_whenExists_returnEntity() {
        when(psuDao.findById(13L)).thenReturn(Optional.of(psu));

        Psu result = psuService.findById(13L);

        assertThat(result.getBrand()).isEqualTo("Corsair");
        verify(psuDao, times(1)).findById(13L);
    }

    @Test
    @DisplayName("findById lanza ComponentNotFoundException cuando no existe")
    void shouldFindById_whenMissing_throwNotFound() {
        when(psuDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> psuService.findById(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");
    }

    @Test
    @DisplayName("findById convierte DataAccessException en ComponentServiceException")
    void shouldFindById_whenDataAccessFails_throwServiceException() {
        when(psuDao.findById(13L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> psuService.findById(13L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al consultar la PSU en la base de datos");
    }

    @Test
    @DisplayName("save guarda la PSU correctamente")
    void shouldSave_whenValidEntity_saveAndReturnEntity() {
        when(psuDao.save(any(Psu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Psu result = psuService.save(psu);

        assertThat(result.getWattage()).isEqualTo(850);
        verify(psuDao, times(1)).save(psu);
    }

    @Test
    @DisplayName("save convierte DataIntegrityViolationException en ComponentServiceException")
    void shouldSave_whenIntegrityViolation_throwServiceException() {
        when(psuDao.save(any(Psu.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> psuService.save(psu))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el componente: datos duplicados o restricción violada");
    }

    @Test
    @DisplayName("save convierte DataAccessException general en ComponentServiceException")
    void shouldSave_whenDataAccessFails_throwServiceException() {
        when(psuDao.save(any(Psu.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> psuService.save(psu))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar la PSU en la base de datos");
    }

    @Test
    @DisplayName("update actualiza campos cuando la PSU existe")
    void shouldUpdate_whenExists_updateAndReturnEntity() {
        Psu input = createPsu(13L, "Corsair", "RM850x SHIFT", 850, "80+ Gold", new BigDecimal("739900"));
        when(psuDao.findById(13L)).thenReturn(Optional.of(psu));
        when(psuDao.save(any(Psu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Psu result = psuService.update(13L, input);

        assertThat(result.getModel()).isEqualTo("RM850x SHIFT");
        assertThat(result.getEfficiency()).isEqualTo("80+ Gold");
        verify(psuDao, times(1)).findById(13L);
        verify(psuDao, times(1)).save(psu);
    }

    @Test
    @DisplayName("update lanza ComponentNotFoundException cuando la PSU no existe")
    void shouldUpdate_whenMissing_throwNotFound() {
        when(psuDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> psuService.update(99L, psu))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(psuDao, times(1)).findById(99L);
        verify(psuDao, never()).save(any());
    }

    @Test
    @DisplayName("update convierte DataAccessException en ComponentServiceException")
    void shouldUpdate_whenDataAccessFails_throwServiceException() {
        when(psuDao.findById(13L)).thenReturn(Optional.of(psu));
        when(psuDao.save(any(Psu.class))).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> psuService.update(13L, psu))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al actualizar la PSU en la base de datos");
    }

    @Test
    @DisplayName("delete elimina la PSU cuando existe")
    void shouldDelete_whenExists_deleteById() {
        when(psuDao.existsById(13L)).thenReturn(true);

        psuService.delete(13L);

        verify(psuDao, times(1)).existsById(13L);
        verify(psuDao, times(1)).deleteById(13L);
    }

    @Test
    @DisplayName("delete lanza ComponentNotFoundException cuando la PSU no existe")
    void shouldDelete_whenMissing_throwNotFound() {
        when(psuDao.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> psuService.delete(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(psuDao, times(1)).existsById(99L);
        verify(psuDao, never()).deleteById(99L);
    }

    @Test
    @DisplayName("delete convierte DataAccessException en ComponentServiceException")
    void shouldDelete_whenDataAccessFails_throwServiceException() {
        when(psuDao.existsById(13L)).thenThrow(new DataAccessResourceFailureException("db down"));

        assertThatThrownBy(() -> psuService.delete(13L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al eliminar la PSU en la base de datos");

        verify(psuDao, times(1)).existsById(13L);
        verify(psuDao, never()).deleteById(any());
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