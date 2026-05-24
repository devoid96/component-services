package com.techplanner.componentservice.domain.repositories;

import com.techplanner.componentservice.domain.entities.Gpu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGpuDao extends JpaRepository<Gpu, Long> {

}