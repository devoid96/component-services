package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Cpu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import com.techplanner.componentservice.domain.exception.ComponentServiceException;
import com.techplanner.componentservice.domain.repositories.ICpuDao;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CpuServiceImpl implements ICpuService {

    @Autowired
    private ICpuDao cpuDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cpu> findAll() {
        return cpuDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cpu findById(Long id) {
        try {
            return cpuDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al consultar el CPU en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Cpu save(Cpu cpu) {
        try {
            return cpuDao.save(cpu);
        } catch (DataIntegrityViolationException ex) {
            throw new ComponentServiceException(
                    "Error al guardar el componente: datos duplicados o restricción violada",
                    ex);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al guardar el CPU en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public Cpu update(Long id, Cpu cpu) {
        try {
            Cpu cpuActual = cpuDao.findById(id)
                    .orElseThrow(() -> new ComponentNotFoundException(id));

            cpuActual.setBrand(cpu.getBrand());
            cpuActual.setModel(cpu.getModel());
            cpuActual.setPrice(cpu.getPrice());
            cpuActual.setSocket(cpu.getSocket());
            cpuActual.setCores(cpu.getCores());
            cpuActual.setThreads(cpu.getThreads());
            cpuActual.setTdp(cpu.getTdp());

            return cpuDao.save(cpuActual);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al actualizar el CPU en la base de datos", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            if (!cpuDao.existsById(id)) {
                throw new ComponentNotFoundException(id);
            }
            cpuDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new ComponentServiceException("Error al eliminar el CPU en la base de datos", e);
        }
    }
}