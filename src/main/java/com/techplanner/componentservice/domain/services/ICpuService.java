package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Cpu;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import java.util.List;

public interface ICpuService {

    List<Cpu> findAll();

    Cpu findById(Long id) throws ComponentNotFoundException;

    Cpu save(Cpu cpu);

    Cpu update(Long id, Cpu cpu);

    void delete(Long id);

}