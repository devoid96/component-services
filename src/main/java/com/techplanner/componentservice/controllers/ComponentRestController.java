package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Component;
import com.techplanner.componentservice.services.IComponentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        List<Component> items = componentService.findAll();
        return ResponseEntity.ok(items);
    }
}
