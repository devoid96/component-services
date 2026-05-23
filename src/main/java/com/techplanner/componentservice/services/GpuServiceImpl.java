package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Gpu;
import com.techplanner.componentservice.repositories.IGpuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GpuServiceImpl implements IGpuService {

    @Autowired
    private IGpuDao gpuDao;

    @Override
    public List<Gpu> findAll() {
        return gpuDao.findAll();
    }

    @Override
    public Optional<Gpu> findById(Long id) {
        return gpuDao.findById(id);
    }

    @Override
    public Gpu save(Gpu gpu) {
        return gpuDao.save(gpu);
    }

    @Override
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
    public void delete(Gpu gpu) {
        gpuDao.delete(gpu);
    }
}