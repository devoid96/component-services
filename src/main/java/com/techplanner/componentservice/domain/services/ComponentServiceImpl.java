package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Component;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IComponentDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComponentServiceImpl implements IComponentService {

    @org.springframework.beans.factory.annotation.Autowired
    private IComponentDao componentDao;

    @Override
    @Transactional(readOnly = true)
    public List<Component> findAll() {
        return componentDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Component findById(Long id) {
        try {
            return componentDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al consultar los componentes en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Component save(Component component) {
        try {
            return componentDao.save(component);
        } catch (DataIntegrityViolationException ex) {
            throw new ComponentServiceException(
                    "Error al guardar el componente: datos duplicados o restricción violada",
                    ex);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al guardar el componente en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            if (!componentDao.existsById(id)) {
                throw new ComponentNotFoundException(id);
            }
            componentDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al eliminar el componente en la base de datos", e);
        }
    }
}