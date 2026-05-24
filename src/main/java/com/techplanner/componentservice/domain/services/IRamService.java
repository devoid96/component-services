package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Ram;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import java.util.List;

public interface IRamService {

    List<Ram> findAll();

    Ram findById(Long id) throws ComponentNotFoundException;

    Ram save(Ram ram);

    Ram update(Long id, Ram ram);

    void delete(Long id);
}