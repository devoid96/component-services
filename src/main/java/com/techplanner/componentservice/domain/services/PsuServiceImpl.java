package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Psu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IPsuDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PsuServiceImpl implements IPsuService {

    @Autowired
    private IPsuDao psuDao;

    @Override
    @Transactional(readOnly = true)
    public List<Psu> findAll() {
        return psuDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Psu findById(Long id) {
        try {
            return psuDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al consultar la PSU en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Psu save(Psu psu) {
        try {
            return psuDao.save(psu);
        } catch (DataIntegrityViolationException ex) {
            throw new ComponentServiceException(
                    "Error al guardar el componente: datos duplicados o restricción violada",
                    ex);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al guardar la PSU en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Psu update(Long id, Psu psu) {
        try {
            Psu psuActual = psuDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));

            psuActual.setBrand(psu.getBrand());
            psuActual.setModel(psu.getModel());
            psuActual.setPrice(psu.getPrice());
            psuActual.setWattage(psu.getWattage());
            psuActual.setEfficiency(psu.getEfficiency());

            return psuDao.save(psuActual);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al actualizar la PSU en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            if (!psuDao.existsById(id)) {
                throw new ComponentNotFoundException(id);
            }
            psuDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al eliminar la PSU en la base de datos", e);
        }
    }
}