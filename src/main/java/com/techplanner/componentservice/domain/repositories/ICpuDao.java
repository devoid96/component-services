package com.techplanner.componentservice.domain.repositories;

import com.techplanner.componentservice.domain.entities.Cpu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICpuDao extends JpaRepository<Cpu, Long> {

}