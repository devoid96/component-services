package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Storage;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;

import java.util.List;

public interface IStorageService {

    List<Storage> findAll();

    Storage findById(Long id) throws ComponentNotFoundException;

    Storage save(Storage storage);

    Storage update(Long id, Storage storage);

    void delete(Long id);
}