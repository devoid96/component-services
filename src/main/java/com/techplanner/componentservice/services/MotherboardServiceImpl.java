package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Motherboard;
import com.techplanner.componentservice.repositories.IMotherboardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<Motherboard> findById(Long id) {
        return motherboardDao.findById(id);
    }

    @Override
    @Transactional
    public Motherboard save(Motherboard motherboard) {
        return motherboardDao.save(motherboard);
    }

    @Override
    @Transactional
    public Motherboard update(Long id, Motherboard motherboard) {

        Motherboard motherboardActual =
                motherboardDao.findById(id).orElseThrow();

        motherboardActual.setBrand(motherboard.getBrand());
        motherboardActual.setModel(motherboard.getModel());
        motherboardActual.setPrice(motherboard.getPrice());
        motherboardActual.setSocket(motherboard.getSocket());
        motherboardActual.setRamType(motherboard.getRamType());
        motherboardActual.setMaxRam(motherboard.getMaxRam());
        motherboardActual.setPcieVersion(motherboard.getPcieVersion());

        return motherboardDao.save(motherboardActual);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        motherboardDao.deleteById(id);
    }
}