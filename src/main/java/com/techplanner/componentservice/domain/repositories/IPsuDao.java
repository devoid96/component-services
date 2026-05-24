package com.techplanner.componentservice.domain.repositories;

import com.techplanner.componentservice.domain.entities.Psu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPsuDao extends JpaRepository<Psu, Long> {

}