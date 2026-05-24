package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Gpu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import java.util.List;

public interface IGpuService {

    List<Gpu> findAll();

    Gpu findById(Long id) throws ComponentNotFoundException;

    Gpu save(Gpu gpu);

    Gpu update(Long id, Gpu gpu);

    void delete(Long id);
}