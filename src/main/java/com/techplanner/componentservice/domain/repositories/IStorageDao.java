package com.techplanner.componentservice.domain.repositories;

import com.techplanner.componentservice.domain.entities.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStorageDao extends JpaRepository<Storage, Long> {

}