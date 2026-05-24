package com.techplanner.componentservice.domain.repositories;

import com.techplanner.componentservice.domain.entities.Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IComponentDao extends JpaRepository<Component, Long> {
}