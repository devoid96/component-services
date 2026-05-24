package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Cpu;
import com.techplanner.componentservice.repositories.ICpuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<Cpu> findById(Long id) {
        return cpuDao.findById(id);
    }

    @Override
    @Transactional
    public Cpu save(Cpu cpu) {
        return cpuDao.save(cpu);
    }

    @Override
    @Transactional
    public Cpu update(Long id, Cpu cpu) {

        Cpu cpuActual = cpuDao.findById(id).orElseThrow();

        cpuActual.setBrand(cpu.getBrand());
        cpuActual.setModel(cpu.getModel());
        cpuActual.setPrice(cpu.getPrice());
        cpuActual.setSocket(cpu.getSocket());
        cpuActual.setCores(cpu.getCores());
        cpuActual.setThreads(cpu.getThreads());
        cpuActual.setTdp(cpu.getTdp());

        return cpuDao.save(cpuActual);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cpuDao.deleteById(id);
    }
}