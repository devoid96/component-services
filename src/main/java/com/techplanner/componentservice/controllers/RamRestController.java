package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Ram;
import com.techplanner.componentservice.services.IRamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Ram>> listarRams() {
        List<Ram> items = ramService.findAll();
        return ResponseEntity.ok(items);
    }

    // ── GET /rams/{id} ─────────────────────────────────────
    // Busca un ram específico por su ID
    @GetMapping("/rams/{id}")
    public ResponseEntity<?> buscarRam(@PathVariable Long id) {
        Optional<Ram> item = ramService.findById(id);

        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }

        return ResponseEntity.notFound().build();
    }

    // ── POST /rams ──────────────────────────────────────────
    // Crea un nuevo ram
    @PostMapping("/rams")
    public ResponseEntity<Ram> crear(@RequestBody Ram ram) {
        Ram nuevo = ramService.save(ram);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // ── PUT /rams/{id} ──────────────────────────────────────
    // Actualiza un ram existente
    @PutMapping("/rams/{id}")
    public ResponseEntity<?> actualizarRam(
            @PathVariable Long id,
            @RequestBody Ram ram) {

        if (ramService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ram actualizado = ramService.update(id, ram);
        return ResponseEntity.ok(actualizado);
    }

    // ── DELETE /rams ───────────────────────────────────────
    // Elimina un ram enviando el ID en el body JSON
    @DeleteMapping("/rams")
    public ResponseEntity<?> eliminarRam(@RequestBody Ram ram) {

        if (ram.getId() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Debe enviar el ID a eliminar"));
        }

        Optional<Ram> encontrado = ramService.findById(ram.getId());

        if (encontrado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ramService.delete(encontrado.get());
        return ResponseEntity.ok(Map.of("mensaje", "Ram eliminado correctamente"));
    }
}
