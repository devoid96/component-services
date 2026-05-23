package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Gpu;
import com.techplanner.componentservice.services.IGpuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        List<Gpu> items = gpuService.findAll();
        return ResponseEntity.ok(items);
    }

    // ── GET /gpus/{id} ─────────────────────────────────────
    // Busca un gpu específico por su ID
    @GetMapping("/gpus/{id}")
    public ResponseEntity<?> buscarGpu(@PathVariable Long id) {
        Optional<Gpu> item = gpuService.findById(id);

        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }

        return ResponseEntity.notFound().build();
    }

    // ── POST /gpus ──────────────────────────────────────────
    // Crea un nuevo gpu
    @PostMapping("/gpus")
    public ResponseEntity<Gpu> crear(@RequestBody Gpu gpu) {
        Gpu nuevo = gpuService.save(gpu);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // ── PUT /gpus/{id} ──────────────────────────────────────
    // Actualiza un gpu existente
    @PutMapping("/gpus/{id}")
    public ResponseEntity<?> actualizarGpu(
            @PathVariable Long id,
            @RequestBody Gpu gpu) {

        if (gpuService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Gpu actualizado = gpuService.update(id, gpu);
        return ResponseEntity.ok(actualizado);
    }

    // ── DELETE /gpus ───────────────────────────────────────
    // Elimina un gpu enviando el ID en el body JSON
    @DeleteMapping("/gpus")
    public ResponseEntity<?> eliminarGpu(@RequestBody Gpu gpu) {

        if (gpu.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Debe enviar el ID a eliminar"));
        }

        Optional<Gpu> encontrado = gpuService.findById(gpu.getId());

        if (encontrado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        gpuService.delete(encontrado.get());
        return ResponseEntity.ok(Map.of("mensaje", "Gpu eliminado correctamente"));
    }
}
