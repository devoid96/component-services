package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Gpu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.IGpuDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GpuServiceImpl implements IGpuService {

    @Autowired
    private IGpuDao gpuDao;

    @Override
    @Transactional(readOnly = true)
    public List<Gpu> findAll() {
        return gpuDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Gpu findById(Long id) {
        try {
            return gpuDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al consultar la GPU en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Gpu save(Gpu gpu) {
        try {
            return gpuDao.save(gpu);
        } catch (DataIntegrityViolationException ex) {
            throw new ComponentServiceException(
                    "Error al guardar el componente: datos duplicados o restricción violada",
                    ex);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al guardar la GPU en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Gpu update(Long id, Gpu gpu) {
        try {
            Gpu gpuActual = gpuDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));

            gpuActual.setBrand(gpu.getBrand());
            gpuActual.setModel(gpu.getModel());
            gpuActual.setPrice(gpu.getPrice());
            gpuActual.setVram(gpu.getVram());
            gpuActual.setTdp(gpu.getTdp());
            gpuActual.setPcieVersion(gpu.getPcieVersion());

            return gpuDao.save(gpuActual);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al actualizar la GPU en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            if (!gpuDao.existsById(id)) {
                throw new ComponentNotFoundException(id);
            }
            gpuDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al eliminar la GPU en la base de datos", e);
        }
    }
}