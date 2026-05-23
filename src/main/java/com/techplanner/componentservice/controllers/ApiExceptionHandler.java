package com.techplanner.componentservice.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidBody() {
        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", "El cuerpo de la peticion esta vacio o tiene un JSON invalido"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("mensaje", "La peticion no cumple con los datos requeridos por la base de datos"));
    }
}
