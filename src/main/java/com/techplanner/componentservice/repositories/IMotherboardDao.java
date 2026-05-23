package com.techplanner.componentservice.repositories;

import com.techplanner.componentservice.entities.Motherboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMotherboardDao extends JpaRepository<Motherboard, Long> {

}