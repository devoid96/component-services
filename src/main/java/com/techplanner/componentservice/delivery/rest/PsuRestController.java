package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.domain.entities.Psu;
import com.techplanner.componentservice.domain.services.IPsuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        return ResponseEntity.ok(psuService.findAll());
    }

    // ── GET /psus/{id} ─────────────────────────────────────
    // Busca un psu específico por su ID
    @GetMapping("/psus/{id}")
    public ResponseEntity<Psu> buscarPsu(@PathVariable Long id) {
        return ResponseEntity.ok(psuService.findById(id));
    }

    // ── POST /psus ──────────────────────────────────────────
    // Crea un nuevo psu
    @PostMapping("/psus")
    public ResponseEntity<?> crear(@Valid @RequestBody Psu psu, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(psuService.save(psu));
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

        return ResponseEntity.ok(psuService.update(id, psu));
    }

    // ── DELETE /psus/{id} ───────────────────────────────────────
    // Elimina un psu por su ID
    @DeleteMapping("/psus/{id}")
    public ResponseEntity<?> eliminarPsu(@PathVariable Long id) {

        psuService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private List<String> construirErrores(BindingResult result) {
        List<String> errores = new ArrayList<>();
        result.getFieldErrors().forEach(error ->
                errores.add("El campo '" + error.getField() + "' " + error.getDefaultMessage()));
        return errores;
    }
}
