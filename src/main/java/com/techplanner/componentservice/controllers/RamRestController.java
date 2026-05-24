package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Ram;
import com.techplanner.componentservice.services.IRamService;
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
 * Controlador REST para gestionar RAMs
 * Proporciona endpoints CRUD para rams específicos
 */
@RestController
@RequestMapping({"/api/v1/component-service", "/api/${api.version}/component-service"})
@CrossOrigin(origins = {"http://localhost:4200"})
public class RamRestController {

    private final IRamService ramService;

    public RamRestController(IRamService ramService) {
        this.ramService = ramService;
    }

    // ── GET /rams ──────────────────────────────────────────
    // Devuelve la lista de todos los rams registrados
    @GetMapping("/rams")
    public ResponseEntity<?> listarRams() {
        try {
            List<Ram> items = ramService.findAll();
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

    // ── GET /rams/{id} ─────────────────────────────────────
    // Busca un ram específico por su ID
    @GetMapping("/rams/{id}")
    public ResponseEntity<?> buscarRam(@PathVariable Long id) {
        try {
            Optional<Ram> item = ramService.findById(id);

            if (item.isPresent()) {
                return ResponseEntity.ok(item.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "La RAM ID: " + id + " no existe en la base de datos"));
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

    // ── POST /rams ──────────────────────────────────────────
    // Crea un nuevo ram
    @PostMapping("/rams")
    public ResponseEntity<?> crear(@Valid @RequestBody Ram ram, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        try {
            Ram nuevo = ramService.save(ram);
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

    // ── PUT /rams/{id} ──────────────────────────────────────
    // Actualiza un ram existente
    @PutMapping("/rams/{id}")
    public ResponseEntity<?> actualizarRam(
            @PathVariable Long id,
            @Valid @RequestBody Ram ram,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        try {
            if (ramService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "La RAM ID: " + id + " no existe en la base de datos"));
            }

            Ram actualizado = ramService.update(id, ram);
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

    // ── DELETE /rams/{id} ───────────────────────────────────────
    // Elimina un ram por su ID
    @DeleteMapping("/rams/{id}")
    public ResponseEntity<?> eliminarRam(@PathVariable Long id) {

        try {
            if (ramService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "La RAM ID: " + id + " no existe en la base de datos"));
            }

            ramService.delete(id);
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
