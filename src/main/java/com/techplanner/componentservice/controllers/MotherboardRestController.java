package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Motherboard;
import com.techplanner.componentservice.services.IMotherboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        public ResponseEntity<List<Motherboard>> listarMotherboards() {
                List<Motherboard> items = motherboardService.findAll();
                return ResponseEntity.ok(items);
        }

        // ── GET /motherboards/{id} ─────────────────────────────────────
        // Busca un motherboard específico por su ID
        @GetMapping("/motherboards/{id}")
        public ResponseEntity<?> buscarMotherboard(@PathVariable Long id) {
                Optional<Motherboard> item = motherboardService.findById(id);

                if (item.isPresent()) {
                        return ResponseEntity.ok(item.get());
                }

                return ResponseEntity.notFound().build();
        }

        // ── POST /motherboards ──────────────────────────────────────────
        // Crea un nuevo motherboard
        @PostMapping("/motherboards")
        public ResponseEntity<Motherboard> crear(@RequestBody Motherboard motherboard) {
                Motherboard nuevo = motherboardService.save(motherboard);
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        }

        // ── PUT /motherboards/{id} ──────────────────────────────────────
        // Actualiza un motherboard existente
        @PutMapping("/motherboards/{id}")
        public ResponseEntity<?> actualizarMotherboard(
                        @PathVariable Long id,
                        @RequestBody Motherboard motherboard) {

                if (motherboardService.findById(id).isEmpty()) {
                        return ResponseEntity.notFound().build();
                }

                Motherboard actualizado = motherboardService.update(id, motherboard);
                return ResponseEntity.ok(actualizado);
        }

        // ── DELETE /motherboards ───────────────────────────────────────
        // Elimina un motherboard enviando el ID en el body JSON
        @DeleteMapping("/motherboards")
        public ResponseEntity<?> eliminarMotherboard(@RequestBody Motherboard motherboard) {

                if (motherboard.getId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(Map.of("error", "Debe enviar el ID a eliminar"));
                }

                Optional<Motherboard> encontrado = motherboardService.findById(motherboard.getId());

                if (encontrado.isEmpty()) {
                        return ResponseEntity.notFound().build();
                }

                motherboardService.delete(encontrado.get());
                return ResponseEntity.ok(Map.of("mensaje", "Motherboard eliminado correctamente"));
        }
}
