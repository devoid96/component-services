package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Psu;
import com.techplanner.componentservice.services.IPsuService;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para gestionar PSUs
 * Proporciona endpoints CRUD para psus específicos
 */
@RestController
@RequestMapping({"/api/v1/component-service", "/api/${api.version}/component-service"})
@CrossOrigin(origins = {"http://localhost:4200"})
public class PsuRestController {

    private final IPsuService psuService;

    public PsuRestController(IPsuService psuService) {
        this.psuService = psuService;
    }

    // ── GET /psus ──────────────────────────────────────────
    // Devuelve la lista de todos los psus registrados
    @GetMapping("/psus")
    public ResponseEntity<?> listarPsus() {
        try {
            List<Psu> items = psuService.findAll();
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

    // ── GET /psus/{id} ─────────────────────────────────────
    // Busca un psu específico por su ID
    @GetMapping("/psus/{id}")
    public ResponseEntity<?> buscarPsu(@PathVariable Long id) {
        try {
            Optional<Psu> item = psuService.findById(id);

            if (item.isPresent()) {
                return ResponseEntity.ok(item.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "La PSU ID: " + id + " no existe en la base de datos"));
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

    // ── POST /psus ──────────────────────────────────────────
    // Crea un nuevo psu
    @PostMapping("/psus")
    public ResponseEntity<?> crear(@Valid @RequestBody Psu psu, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        try {
            Psu nuevo = psuService.save(psu);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "mensaje", "Error al insertar en la base de datos",
                            "error", e.getMostSpecificCause() != null
                                    ? e.getMostSpecificCause().getMessage()
                                    : e.getMessage()
                    ));
        }
    }

    // ── PUT /psus/{id} ──────────────────────────────────────
    // Actualiza un psu existente
    @PutMapping("/psus/{id}")
    public ResponseEntity<?> actualizarPsu(
            @PathVariable Long id,
            @Valid @RequestBody Psu psu,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        try {
            if (psuService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "La PSU ID: " + id + " no existe en la base de datos"));
            }

            Psu actualizado = psuService.update(id, psu);
            return ResponseEntity.ok(actualizado);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "mensaje", "Error al actualizar en la base de datos",
                            "error", e.getMostSpecificCause() != null
                                    ? e.getMostSpecificCause().getMessage()
                                    : e.getMessage()
                    ));
        }
    }

    // ── DELETE /psus/{id} ───────────────────────────────────────
    // Elimina un psu por su ID
    @DeleteMapping("/psus/{id}")
    public ResponseEntity<?> eliminarPsu(@PathVariable Long id) {

        try {
            if (psuService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "La PSU ID: " + id + " no existe en la base de datos"));
            }

            psuService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "mensaje", "Error al eliminar en la base de datos",
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
