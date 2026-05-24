package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Psu;
import java.util.List;
import java.util.Optional;

public interface IPsuService {

    List<Psu> findAll();

    Optional<Psu> findById(Long id);

    Psu save(Psu psu);

    Psu update(Long id, Psu psu);

    void delete(Long id);
}