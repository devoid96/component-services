package com.techplanner.componentservice.services;

import com.techplanner.componentservice.entities.Component;
import com.techplanner.componentservice.repositories.IComponentDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentServiceImpl implements IComponentService {

    @org.springframework.beans.factory.annotation.Autowired
    private IComponentDao componentDao;

    @Override
    @Transactional(readOnly = true)
    public List<Component> findAll() {
        return componentDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Component> findById(Long id) {
        return componentDao.findById(id);
    }

    @Override
    @Transactional
    public Component save(Component component) {
        return componentDao.save(component);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        componentDao.deleteById(id);
    }
}