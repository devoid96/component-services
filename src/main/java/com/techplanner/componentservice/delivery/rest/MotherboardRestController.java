package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.domain.entities.Motherboard;
import com.techplanner.componentservice.domain.services.IMotherboardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        public ResponseEntity<List<Motherboard>> listarMotherboards() {
                return ResponseEntity.ok(motherboardService.findAll());
        }

        // ── GET /motherboards/{id} ─────────────────────────────────────
        // Busca un motherboard específico por su ID
        @GetMapping("/motherboards/{id}")
        public ResponseEntity<Motherboard> buscarMotherboard(@PathVariable Long id) {
                return ResponseEntity.ok(motherboardService.findById(id));
        }

        // ── POST /motherboards ──────────────────────────────────────────
        // Crea un nuevo motherboard
        @PostMapping("/motherboards")
        public ResponseEntity<?> crear(@Valid @RequestBody Motherboard motherboard, BindingResult result) {
                if (result.hasErrors()) {
                        return ResponseEntity.badRequest().body(construirErrores(result));
                }

                return ResponseEntity.status(HttpStatus.CREATED).body(motherboardService.save(motherboard));
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

                return ResponseEntity.ok(motherboardService.update(id, motherboard));
        }

        // ── DELETE /motherboards/{id} ───────────────────────────────────────
        // Elimina un motherboard por su ID
        @DeleteMapping("/motherboards/{id}")
        public ResponseEntity<?> eliminarMotherboard(@PathVariable Long id) {

                motherboardService.delete(id);
                return ResponseEntity.noContent().build();
        }

        private List<String> construirErrores(BindingResult result) {
                List<String> errores = new ArrayList<>();
                result.getFieldErrors().forEach(error ->
                                errores.add("El campo '" + error.getField() + "' " + error.getDefaultMessage()));
                return errores;
        }
}
