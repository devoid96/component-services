package com.techplanner.componentservice.domain.exception;

public class ComponentNotFoundException extends RuntimeException {

    private final Long id;

    public ComponentNotFoundException(Long id) {
        super("Componente con id " + id + " no encontrado");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
