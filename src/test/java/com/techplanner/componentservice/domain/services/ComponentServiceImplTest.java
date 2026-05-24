package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Component;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IComponentDao;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ComponentServiceImplTest {

    @Mock
    private IComponentDao componentDao;

    @InjectMocks
    private ComponentServiceImpl componentService;

    private Component component;

    @BeforeEach
    void setUp() {
        component = new Component();
        component.setId(1L);
        component.setBrand("AMD");
        component.setModel("Ryzen 7 7800X3D");
        component.setPrice(new BigDecimal("2399000"));
    }

    @Test
    @DisplayName("findAll retorna lista vacía")
    void findAll_vacio_retornaListaVacia() {
        when(componentDao.findAll()).thenReturn(Collections.emptyList());

        List<Component> componentes = componentService.findAll();

        assertThat(componentes).isNotNull().isEmpty();
        verify(componentDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll retorna lista con elementos")
    void findAll_conElementos_retornaListaConElementos() {
        Component otro = crearComponente(2L, "Intel", "Core i7-14700K", new BigDecimal("2299000"));
        when(componentDao.findAll()).thenReturn(List.of(component, otro));

        List<Component> componentes = componentService.findAll();

        assertThat(componentes).hasSize(2);
        assertThat(componentes).extracting(Component::getId).containsExactly(1L, 2L);
        verify(componentDao, times(1)).findAll();
    }

    @Test
    @DisplayName("findById retorna componente existente")
    void findById_existente_retornaComponente() {
        when(componentDao.findById(1L)).thenReturn(Optional.of(component));

        Component encontrado = componentService.findById(1L);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(1L);
        assertThat(encontrado.getBrand()).isEqualTo("AMD");
        assertThat(encontrado.getModel()).isEqualTo("Ryzen 7 7800X3D");
        assertThat(encontrado.getPrice()).isEqualByComparingTo("2399000");
        verify(componentDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById lanza ComponentNotFoundException si no existe")
    void findById_inexistente_lanzaComponentNotFoundException() {
        when(componentDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> componentService.findById(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(componentDao, times(1)).findById(99L);
    }

    @Test
    @DisplayName("findById convierte DataAccessException en ComponentServiceException")
    void findById_errorDataAccessException_convierteAComponentServiceException() {
        when(componentDao.findById(1L)).thenThrow(new DataAccessResourceFailureException("fallo de base de datos"));

        assertThatThrownBy(() -> componentService.findById(1L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al consultar los componentes en la base de datos")
                .hasCauseInstanceOf(DataAccessResourceFailureException.class);

        verify(componentDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("save guarda correctamente el componente")
    void save_exitoso_guardaCorrectamenteElComponente() {
        when(componentDao.save(any(Component.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Component guardado = componentService.save(component);

        assertThat(guardado).isNotNull();
        assertThat(guardado.getId()).isEqualTo(1L);
        assertThat(guardado.getBrand()).isEqualTo("AMD");
        assertThat(guardado.getModel()).isEqualTo("Ryzen 7 7800X3D");
        assertThat(guardado.getPrice()).isEqualByComparingTo("2399000");
        verify(componentDao, times(1)).save(component);
    }

    @Test
    @DisplayName("save convierte DataIntegrityViolationException en ComponentServiceException")
    void save_error_dataIntegrityViolationException_convierteAComponentServiceException() {
        when(componentDao.save(any(Component.class)))
                .thenThrow(new DataIntegrityViolationException("violacion de integridad"));

        assertThatThrownBy(() -> componentService.save(component))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el componente: datos duplicados o restricción violada")
                .hasCauseInstanceOf(DataIntegrityViolationException.class);

        verify(componentDao, times(1)).save(component);
    }

    @Test
    @DisplayName("save convierte DataAccessException general en ComponentServiceException")
    void save_error_dataAccessExceptionGeneral_convierteAComponentServiceException() {
        when(componentDao.save(any(Component.class)))
                .thenThrow(new DataAccessResourceFailureException("fallo de guardado"));

        assertThatThrownBy(() -> componentService.save(component))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al guardar el componente en la base de datos")
                .hasCauseInstanceOf(DataAccessResourceFailureException.class);

        verify(componentDao, times(1)).save(component);
    }

    @Test
    @DisplayName("delete lanza ComponentNotFoundException si el ID no existe")
    void delete_inexistente_lanzaComponentNotFoundException() {
        when(componentDao.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> componentService.delete(99L))
                .isInstanceOf(ComponentNotFoundException.class)
                .hasMessage("Componente con id 99 no encontrado");

        verify(componentDao, times(1)).existsById(99L);
        verify(componentDao, never()).deleteById(99L);
    }

    @Test
    @DisplayName("delete elimina el componente cuando existe")
    void delete_existente_eliminaCorrectamente() {
        when(componentDao.existsById(1L)).thenReturn(true);

        componentService.delete(1L);

        verify(componentDao, times(1)).existsById(1L);
        verify(componentDao, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete convierte DataAccessException en ComponentServiceException")
    void delete_error_dataAccessException_convierteAComponentServiceException() {
        when(componentDao.existsById(1L)).thenReturn(true);
        doThrow(new DataAccessResourceFailureException("fallo al eliminar")).when(componentDao).deleteById(1L);

        assertThatThrownBy(() -> componentService.delete(1L))
                .isInstanceOf(ComponentServiceException.class)
                .hasMessage("Error al eliminar el componente en la base de datos")
                .hasCauseInstanceOf(DataAccessResourceFailureException.class);

        verify(componentDao, times(1)).existsById(1L);
        verify(componentDao, times(1)).deleteById(1L);
    }

    private Component crearComponente(Long id, String brand, String model, BigDecimal price) {
        Component nuevo = new Component();
        nuevo.setId(id);
        nuevo.setBrand(brand);
        nuevo.setModel(model);
        nuevo.setPrice(price);
        return nuevo;
    }
}