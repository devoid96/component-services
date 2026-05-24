package com.techplanner.componentservice.domain.exception;

public class ComponentServiceException extends RuntimeException {

    public ComponentServiceException(String mensaje) {
        super(mensaje);
    }

    public ComponentServiceException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
