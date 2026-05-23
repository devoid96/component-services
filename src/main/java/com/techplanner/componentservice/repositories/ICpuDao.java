package com.techplanner.componentservice.repositories;

import com.techplanner.componentservice.entities.Cpu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICpuDao extends JpaRepository<Cpu, Long> {

}