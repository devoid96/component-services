package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Cpu;
import java.util.List;
import java.util.Optional;

public interface ICpuService {

    List<Cpu> findAll();

    Optional<Cpu> findById(Long id);

    Cpu save(Cpu cpu);

    Cpu update(Long id, Cpu cpu);

    void delete(Long id);

}