package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.domain.entities.Ram;
import com.techplanner.componentservice.domain.services.IRamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        return ResponseEntity.ok(ramService.findAll());
    }

    // ── GET /rams/{id} ─────────────────────────────────────
    // Busca un ram específico por su ID
    @GetMapping("/rams/{id}")
    public ResponseEntity<Ram> buscarRam(@PathVariable Long id) {
        return ResponseEntity.ok(ramService.findById(id));
    }

    // ── POST /rams ──────────────────────────────────────────
    // Crea un nuevo ram
    @PostMapping("/rams")
    public ResponseEntity<?> crear(@Valid @RequestBody Ram ram, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ramService.save(ram));
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

        return ResponseEntity.ok(ramService.update(id, ram));
    }

    // ── DELETE /rams/{id} ───────────────────────────────────────
    // Elimina un ram por su ID
    @DeleteMapping("/rams/{id}")
    public ResponseEntity<?> eliminarRam(@PathVariable Long id) {

        ramService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private List<String> construirErrores(BindingResult result) {
        List<String> errores = new ArrayList<>();
        result.getFieldErrors().forEach(error ->
                errores.add("El campo '" + error.getField() + "' " + error.getDefaultMessage()));
        return errores;
    }
}
