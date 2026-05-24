package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Gpu;
import com.techplanner.componentservice.repositories.IGpuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<Gpu> findById(Long id) {
        return gpuDao.findById(id);
    }

    @Override
    @Transactional
    public Gpu save(Gpu gpu) {
        return gpuDao.save(gpu);
    }

    @Override
    @Transactional
    public Gpu update(Long id, Gpu gpu) {

        Gpu gpuActual = gpuDao.findById(id).orElseThrow();

        gpuActual.setBrand(gpu.getBrand());
        gpuActual.setModel(gpu.getModel());
        gpuActual.setPrice(gpu.getPrice());
        gpuActual.setVram(gpu.getVram());
        gpuActual.setTdp(gpu.getTdp());
        gpuActual.setPcieVersion(gpu.getPcieVersion());

        return gpuDao.save(gpuActual);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        gpuDao.deleteById(id);
    }
}