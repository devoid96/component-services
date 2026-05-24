package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Motherboard;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import java.util.List;

public interface IMotherboardService {

    List<Motherboard> findAll();

    Motherboard findById(Long id) throws ComponentNotFoundException;

    Motherboard save(Motherboard motherboard);

    Motherboard update(Long id, Motherboard motherboard);

    void delete(Long id);
}