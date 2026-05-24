package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.domain.entities.Component;
import com.techplanner.componentservice.domain.exception.ComponentNotFoundException;
import java.util.List;

public interface IComponentService {

    List<Component> findAll();

    Component findById(Long id) throws ComponentNotFoundException;

    Component save(Component component);

    void delete(Long id);
}