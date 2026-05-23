package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Ram;
import com.techplanner.componentservice.repositories.IRamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RamServiceImpl implements IRamService {

    @Autowired
    private IRamDao ramDao;

    @Override
    public List<Ram> findAll() {
        return ramDao.findAll();
    }

    @Override
    public Optional<Ram> findById(Long id) {
        return ramDao.findById(id);
    }

    @Override
    public Ram save(Ram ram) {
        return ramDao.save(ram);
    }

    @Override
    public Ram update(Long id, Ram ram) {

        Ram ramActual = ramDao.findById(id).orElseThrow();

        ramActual.setBrand(ram.getBrand());
        ramActual.setModel(ram.getModel());
        ramActual.setPrice(ram.getPrice());
        ramActual.setType(ram.getType());
        ramActual.setCapacity(ram.getCapacity());
        ramActual.setSpeed(ram.getSpeed());

        return ramDao.save(ramActual);
    }

    @Override
    public void delete(Ram ram) {
        ramDao.delete(ram);
    }
}