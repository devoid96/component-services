package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Ram;
import com.techplanner.componentservice.repositories.IRamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RamServiceImpl implements IRamService {

    @Autowired
    private IRamDao ramDao;

    @Override
    @Transactional(readOnly = true)
    public List<Ram> findAll() {
        return ramDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ram> findById(Long id) {
        return ramDao.findById(id);
    }

    @Override
    @Transactional
    public Ram save(Ram ram) {
        return ramDao.save(ram);
    }

    @Override
    @Transactional
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
    @Transactional
    public void delete(Long id) {
        ramDao.deleteById(id);
    }
}