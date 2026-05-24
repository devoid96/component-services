package com.techplanner.componentservice.domain.repositories;

import com.techplanner.componentservice.domain.entities.Ram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRamDao extends JpaRepository<Ram, Long> {

}