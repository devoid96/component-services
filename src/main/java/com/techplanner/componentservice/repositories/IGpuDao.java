package com.techplanner.componentservice.repositories;

import com.techplanner.componentservice.entities.Gpu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGpuDao extends JpaRepository<Gpu, Long> {

}