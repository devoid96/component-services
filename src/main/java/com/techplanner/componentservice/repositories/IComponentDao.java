package com.techplanner.componentservice.repositories;

import com.techplanner.componentservice.entities.Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IComponentDao extends JpaRepository<Component, Long> {
}