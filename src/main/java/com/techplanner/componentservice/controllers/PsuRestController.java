package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Psu;
import com.techplanner.componentservice.services.IPsuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Psu>> listarPsus() {
        List<Psu> items = psuService.findAll();
        return ResponseEntity.ok(items);
    }

    // ── GET /psus/{id} ─────────────────────────────────────
    // Busca un psu específico por su ID
    @GetMapping("/psus/{id}")
    public ResponseEntity<?> buscarPsu(@PathVariable Long id) {
        Optional<Psu> item = psuService.findById(id);

        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }

        return ResponseEntity.notFound().build();
    }

    // ── POST /psus ──────────────────────────────────────────
    // Crea un nuevo psu
    @PostMapping("/psus")
    public ResponseEntity<Psu> crear(@RequestBody Psu psu) {
        Psu nuevo = psuService.save(psu);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // ── PUT /psus/{id} ──────────────────────────────────────
    // Actualiza un psu existente
    @PutMapping("/psus/{id}")
    public ResponseEntity<?> actualizarPsu(
            @PathVariable Long id,
            @RequestBody Psu psu) {

        if (psuService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Psu actualizado = psuService.update(id, psu);
        return ResponseEntity.ok(actualizado);
    }

    // ── DELETE /psus ───────────────────────────────────────
    // Elimina un psu enviando el ID en el body JSON
    @DeleteMapping("/psus")
    public ResponseEntity<?> eliminarPsu(@RequestBody Psu psu) {

        if (psu.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Debe enviar el ID a eliminar"));
        }

        Optional<Psu> encontrado = psuService.findById(psu.getId());

        if (encontrado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        psuService.delete(encontrado.get());
        return ResponseEntity.ok(Map.of("mensaje", "Psu eliminado correctamente"));
    }
}
