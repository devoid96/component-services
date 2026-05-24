package com.techplanner.componentservice.domain.repositories;

import com.techplanner.componentservice.domain.entities.Motherboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMotherboardDao extends JpaRepository<Motherboard, Long> {

}