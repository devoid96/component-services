package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Motherboard;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IMotherboardDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MotherboardServiceImpl implements IMotherboardService {

    @Autowired
    private IMotherboardDao motherboardDao;

    @Override
    @Transactional(readOnly = true)
    public List<Motherboard> findAll() {
        return motherboardDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Motherboard findById(Long id) {
        try {
            return motherboardDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al consultar la Motherboard en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Motherboard save(Motherboard motherboard) {
        try {
            return motherboardDao.save(motherboard);
        } catch (DataIntegrityViolationException ex) {
            throw new ComponentServiceException(
                    "Error al guardar el componente: datos duplicados o restricción violada",
                    ex);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al guardar la Motherboard en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Motherboard update(Long id, Motherboard motherboard) {
        try {
            Motherboard motherboardActual = motherboardDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));

            motherboardActual.setBrand(motherboard.getBrand());
            motherboardActual.setModel(motherboard.getModel());
            motherboardActual.setPrice(motherboard.getPrice());
            motherboardActual.setSocket(motherboard.getSocket());
            motherboardActual.setRamType(motherboard.getRamType());
            motherboardActual.setMaxRam(motherboard.getMaxRam());
            motherboardActual.setPcieVersion(motherboard.getPcieVersion());

            return motherboardDao.save(motherboardActual);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al actualizar la Motherboard en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            if (!motherboardDao.existsById(id)) {
                throw new ComponentNotFoundException(id);
            }
            motherboardDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al eliminar la Motherboard en la base de datos", e);
        }
    }
}