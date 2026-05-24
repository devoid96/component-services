package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Component;
import com.techplanner.componentservice.services.IComponentService;
import org.springframework.validation.BindingResult;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> listarComponentes() {
        try {
            List<Component> items = componentService.findAll();
            return ResponseEntity.ok(items);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "mensaje", "Error al consultar en la base de datos",
                            "error", e.getMostSpecificCause() != null
                                    ? e.getMostSpecificCause().getMessage()
                                    : e.getMessage()
                    ));
        }
    }

    private List<String> construirErrores(BindingResult result) {
        List<String> errores = new ArrayList<>();
        result.getFieldErrors().forEach(error ->
                errores.add("El campo '" + error.getField() + "' " + error.getDefaultMessage()));
        return errores;
    }
}
