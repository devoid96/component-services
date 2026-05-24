package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Storage;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IStorageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StorageServiceImpl implements IStorageService {

    @Autowired
    private IStorageDao storageDao;

    @Override
    @Transactional(readOnly = true)
    public List<Storage> findAll() {
        return storageDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Storage findById(Long id) {
        try {
            return storageDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al consultar el almacenamiento en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Storage save(Storage storage) {
        try {
            return storageDao.save(storage);
        } catch (DataIntegrityViolationException ex) {
            throw new ComponentServiceException(
                    "Error al guardar el componente: datos duplicados o restricción violada",
                    ex);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al guardar el almacenamiento en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Storage update(Long id, Storage storage) {
        try {
            Storage storageActual = storageDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));

            storageActual.setBrand(storage.getBrand());
            storageActual.setModel(storage.getModel());
            storageActual.setPrice(storage.getPrice());
            storageActual.setCapacityGb(storage.getCapacityGb());
            storageActual.setType(storage.getType());
            storageActual.setInterfaceType(storage.getInterfaceType());

            return storageDao.save(storageActual);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al actualizar el almacenamiento en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            if (!storageDao.existsById(id)) {
                throw new ComponentNotFoundException(id);
            }
            storageDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al eliminar el almacenamiento en la base de datos", e);
        }
    }
}