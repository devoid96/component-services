package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Psu;
import com.techplanner.componentservice.repositories.IPsuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PsuServiceImpl implements IPsuService {

    @Autowired
    private IPsuDao psuDao;

    @Override
    public List<Psu> findAll() {
        return psuDao.findAll();
    }

    @Override
    public Optional<Psu> findById(Long id) {
        return psuDao.findById(id);
    }

    @Override
    public Psu save(Psu psu) {
        return psuDao.save(psu);
    }

    @Override
    public Psu update(Long id, Psu psu) {

        Psu psuActual = psuDao.findById(id).orElseThrow();

        psuActual.setBrand(psu.getBrand());
        psuActual.setModel(psu.getModel());
        psuActual.setPrice(psu.getPrice());
        psuActual.setWattage(psu.getWattage());
        psuActual.setEfficiency(psu.getEfficiency());

        return psuDao.save(psuActual);
    }

    @Override
    public void delete(Psu psu) {
        psuDao.delete(psu);
    }
}