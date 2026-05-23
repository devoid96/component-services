package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Motherboard;
import java.util.List;
import java.util.Optional;

public interface IMotherboardService {

    List<Motherboard> findAll();

    Optional<Motherboard> findById(Long id);

    Motherboard save(Motherboard motherboard);

    Motherboard update(Long id, Motherboard motherboard);

    void delete(Motherboard motherboard);
}