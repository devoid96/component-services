package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Motherboard;
import com.techplanner.componentservice.services.IMotherboardService;
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
 * Controlador REST para gestionar Motherboards
 * Proporciona endpoints CRUD para motherboards específicos
 */
@RestController
@RequestMapping({"/api/v1/component-service", "/api/${api.version}/component-service"})
@CrossOrigin(origins = {"http://localhost:4200"})
public class MotherboardRestController {

        private final IMotherboardService motherboardService;

        public MotherboardRestController(IMotherboardService motherboardService) {
                this.motherboardService = motherboardService;
        }

        // ── GET /motherboards ──────────────────────────────────────────
        // Devuelve la lista de todos los motherboards registrados
        @GetMapping("/motherboards")
        public ResponseEntity<?> listarMotherboards() {
                try {
                        List<Motherboard> items = motherboardService.findAll();
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

        // ── GET /motherboards/{id} ─────────────────────────────────────
        // Busca un motherboard específico por su ID
        @GetMapping("/motherboards/{id}")
        public ResponseEntity<?> buscarMotherboard(@PathVariable Long id) {
                try {
                        Optional<Motherboard> item = motherboardService.findById(id);

                        if (item.isPresent()) {
                                return ResponseEntity.ok(item.get());
                        }

                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(Map.of("mensaje", "La Motherboard ID: " + id + " no existe en la base de datos"));
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

        // ── POST /motherboards ──────────────────────────────────────────
        // Crea un nuevo motherboard
        @PostMapping("/motherboards")
        public ResponseEntity<?> crear(@Valid @RequestBody Motherboard motherboard, BindingResult result) {
                if (result.hasErrors()) {
                        return ResponseEntity.badRequest().body(construirErrores(result));
                }

                try {
                        Motherboard nuevo = motherboardService.save(motherboard);
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

        // ── PUT /motherboards/{id} ──────────────────────────────────────
        // Actualiza un motherboard existente
        @PutMapping("/motherboards/{id}")
        public ResponseEntity<?> actualizarMotherboard(
                        @PathVariable Long id,
                        @Valid @RequestBody Motherboard motherboard,
                        BindingResult result) {

                if (result.hasErrors()) {
                        return ResponseEntity.badRequest().body(construirErrores(result));
                }

                try {
                        if (motherboardService.findById(id).isEmpty()) {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(Map.of("mensaje", "La Motherboard ID: " + id + " no existe en la base de datos"));
                        }

                        Motherboard actualizado = motherboardService.update(id, motherboard);
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

        // ── DELETE /motherboards/{id} ───────────────────────────────────────
        // Elimina un motherboard por su ID
        @DeleteMapping("/motherboards/{id}")
        public ResponseEntity<?> eliminarMotherboard(@PathVariable Long id) {

                try {
                        if (motherboardService.findById(id).isEmpty()) {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(Map.of("mensaje", "La Motherboard ID: " + id + " no existe en la base de datos"));
                        }

                        motherboardService.delete(id);
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
