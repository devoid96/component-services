package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Component;
import java.util.List;
import java.util.Optional;

public interface IComponentService {

    List<Component> findAll();

    Optional<Component> findById(Long id);

    Component save(Component component);

    void delete(Component component);
}