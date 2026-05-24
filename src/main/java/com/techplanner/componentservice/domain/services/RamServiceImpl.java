package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Ram;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IRamDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RamServiceImpl implements IRamService {

    @Autowired
    private IRamDao ramDao;

    @Override
    @Transactional(readOnly = true)
    public List<Ram> findAll() {
        return ramDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Ram findById(Long id) {
        try {
            return ramDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al consultar la RAM en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Ram save(Ram ram) {
        try {
            return ramDao.save(ram);
        } catch (DataIntegrityViolationException ex) {
            throw new ComponentServiceException(
                    "Error al guardar el componente: datos duplicados o restricción violada",
                    ex);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al guardar la RAM en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Ram update(Long id, Ram ram) {
        try {
            Ram ramActual = ramDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));

            ramActual.setBrand(ram.getBrand());
            ramActual.setModel(ram.getModel());
            ramActual.setPrice(ram.getPrice());
            ramActual.setType(ram.getType());
            ramActual.setCapacity(ram.getCapacity());
            ramActual.setSpeed(ram.getSpeed());

            return ramDao.save(ramActual);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al actualizar la RAM en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            if (!ramDao.existsById(id)) {
                throw new ComponentNotFoundException(id);
            }
            ramDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al eliminar la RAM en la base de datos", e);
        }
    }
}