package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.domain.entities.Component;
import com.techplanner.componentservice.domain.services.IComponentService;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador REST para gestionar Componentes
 * Proporciona endpoints CRUD para components específicos
 */
@RestController
@RequestMapping({"/api/v1/component-service", "/api/${api.version}/component-service"})
@CrossOrigin(origins = {"http://localhost:4200"})
public class ComponentRestController {

    private final IComponentService componentService;

    public ComponentRestController(IComponentService componentService) {
        this.componentService = componentService;
    }

    // ── GET /components ──────────────────────────────────────────
    // Devuelve la lista de todos los components registrados
    @GetMapping("/components")
    public ResponseEntity<List<Component>> listarComponentes() {
        return ResponseEntity.ok(componentService.findAll());
    }

    private List<String> construirErrores(BindingResult result) {
        List<String> errores = new ArrayList<>();
        result.getFieldErrors().forEach(error ->
                errores.add("El campo '" + error.getField() + "' " + error.getDefaultMessage()));
        return errores;
    }
}
