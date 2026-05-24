package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Cpu;
import com.techplanner.componentservice.services.ICpuService;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"/api/v1/component-service", "/api/${api.version}/component-service"})
@CrossOrigin(origins = {"http://localhost:4200"})
public class CpuRestController {

    @Autowired
    private ICpuService cpuService;

    @GetMapping("/cpus")
    public ResponseEntity<?> listarCpus() {
        try {
            List<Cpu> cpus = cpuService.findAll();
            return ResponseEntity.ok(cpus);
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

    @GetMapping("/cpus/{id}")
    public ResponseEntity<?> buscarCpu(@PathVariable Long id) {
        try {
            Optional<Cpu> cpu = cpuService.findById(id);

            if (cpu.isPresent()) {
                return ResponseEntity.ok(cpu.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "El CPU ID: " + id + " no existe en la base de datos"));
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

    @PostMapping("/cpus")
    public ResponseEntity<?> crearCpu(@Valid @RequestBody Cpu cpu, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        try {
            Cpu nuevo = cpuService.save(cpu);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevo);
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

    @PutMapping("/cpus/{id}")
    public ResponseEntity<?> actualizarCpu(
            @PathVariable Long id,
            @Valid @RequestBody Cpu cpu,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        try {
            if (cpuService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "El CPU ID: " + id + " no existe en la base de datos"));
            }

            Cpu actualizado = cpuService.update(id, cpu);

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

    @DeleteMapping("/cpus/{id}")
    public ResponseEntity<?> eliminarCpu(@PathVariable Long id) {

        try {
            if (cpuService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "El CPU ID: " + id + " no existe en la base de datos"));
            }

            cpuService.delete(id);

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
