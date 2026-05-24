package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.domain.entities.Gpu;
import com.techplanner.componentservice.domain.services.IGpuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador REST para gestionar GPUs
 * Proporciona endpoints CRUD para gpus específicos
 */
@RestController
@RequestMapping({"/api/v1/component-service", "/api/${api.version}/component-service"})
@CrossOrigin(origins = {"http://localhost:4200"})
public class GpuRestController {

    private final IGpuService gpuService;

    public GpuRestController(IGpuService gpuService) {
        this.gpuService = gpuService;
    }

    // ── GET /gpus ──────────────────────────────────────────
    // Devuelve la lista de todos los gpus registrados
    @GetMapping("/gpus")
    public ResponseEntity<List<Gpu>> listarGpus() {
        return ResponseEntity.ok(gpuService.findAll());
    }

    // ── GET /gpus/{id} ─────────────────────────────────────
    // Busca un gpu específico por su ID
    @GetMapping("/gpus/{id}")
    public ResponseEntity<Gpu> buscarGpu(@PathVariable Long id) {
        return ResponseEntity.ok(gpuService.findById(id));
    }

    // ── POST /gpus ──────────────────────────────────────────
    // Crea un nuevo gpu
    @PostMapping("/gpus")
    public ResponseEntity<?> crear(@Valid @RequestBody Gpu gpu, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(gpuService.save(gpu));
    }

    // ── PUT /gpus/{id} ──────────────────────────────────────
    // Actualiza un gpu existente
    @PutMapping("/gpus/{id}")
    public ResponseEntity<?> actualizarGpu(
            @PathVariable Long id,
            @Valid @RequestBody Gpu gpu,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        return ResponseEntity.ok(gpuService.update(id, gpu));
    }

    // ── DELETE /gpus/{id} ───────────────────────────────────────
    // Elimina un gpu por su ID
    @DeleteMapping("/gpus/{id}")
    public ResponseEntity<?> eliminarGpu(@PathVariable Long id) {

        gpuService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private List<String> construirErrores(BindingResult result) {
        List<String> errores = new ArrayList<>();
        result.getFieldErrors().forEach(error ->
                errores.add("El campo '" + error.getField() + "' " + error.getDefaultMessage()));
        return errores;
    }
}
