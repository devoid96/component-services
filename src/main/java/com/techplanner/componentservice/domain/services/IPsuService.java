package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Psu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import java.util.List;

public interface IPsuService {

    List<Psu> findAll();

    Psu findById(Long id) throws ComponentNotFoundException;

    Psu save(Psu psu);

    Psu update(Long id, Psu psu);

    void delete(Long id);
}