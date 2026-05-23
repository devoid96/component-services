package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Gpu;
import java.util.List;
import java.util.Optional;

public interface IGpuService {

    List<Gpu> findAll();

    Optional<Gpu> findById(Long id);

    Gpu save(Gpu gpu);

    Gpu update(Long id, Gpu gpu);

    void delete(Gpu gpu);
}