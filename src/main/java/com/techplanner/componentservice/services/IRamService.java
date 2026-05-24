package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Ram;
import java.util.List;
import java.util.Optional;

public interface IRamService {

    List<Ram> findAll();

    Optional<Ram> findById(Long id);

    Ram save(Ram ram);

    Ram update(Long id, Ram ram);

    void delete(Long id);
}