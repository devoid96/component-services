package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Component;
import com.techplanner.componentservice.repositories.IComponentDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements IComponentService {

    @org.springframework.beans.factory.annotation.Autowired
    private IComponentDao componentDao;

    @Override
    public List<Component> findAll() {
        return componentDao.findAll();
    }

    @Override
    public Optional<Component> findById(Long id) {
        return componentDao.findById(id);
    }

    @Override
    public Component save(Component component) {
        return componentDao.save(component);
    }

    @Override
    public void delete(Component component) {
        componentDao.delete(component);
    }
}