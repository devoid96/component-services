package com.techplanner.componentservice.repositories;

import com.techplanner.componentservice.entities.Psu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPsuDao extends JpaRepository<Psu, Long> {

}