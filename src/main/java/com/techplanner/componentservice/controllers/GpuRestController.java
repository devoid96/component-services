package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Gpu;
import com.techplanner.componentservice.services.IGpuService;
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
    public ResponseEntity<?> listarGpus() {
        try {
            List<Gpu> items = gpuService.findAll();
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

    // ── GET /gpus/{id} ─────────────────────────────────────
    // Busca un gpu específico por su ID
    @GetMapping("/gpus/{id}")
    public ResponseEntity<?> buscarGpu(@PathVariable Long id) {
        try {
            Optional<Gpu> item = gpuService.findById(id);

            if (item.isPresent()) {
                return ResponseEntity.ok(item.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "El GPU ID: " + id + " no existe en la base de datos"));
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

    // ── POST /gpus ──────────────────────────────────────────
    // Crea un nuevo gpu
    @PostMapping("/gpus")
    public ResponseEntity<?> crear(@Valid @RequestBody Gpu gpu, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        try {
            Gpu nuevo = gpuService.save(gpu);
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

        try {
            if (gpuService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "El GPU ID: " + id + " no existe en la base de datos"));
            }

            Gpu actualizado = gpuService.update(id, gpu);
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

    // ── DELETE /gpus/{id} ───────────────────────────────────────
    // Elimina un gpu por su ID
    @DeleteMapping("/gpus/{id}")
    public ResponseEntity<?> eliminarGpu(@PathVariable Long id) {

        try {
            if (gpuService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "El GPU ID: " + id + " no existe en la base de datos"));
            }

            gpuService.delete(id);
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
