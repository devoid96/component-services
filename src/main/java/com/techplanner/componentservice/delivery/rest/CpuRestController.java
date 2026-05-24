package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.domain.entities.Cpu;
import com.techplanner.componentservice.domain.services.ICpuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api/v1/component-service", "/api/${api.version}/component-service"})
@CrossOrigin(origins = {"http://localhost:4200"})
public class CpuRestController {

    @Autowired
    private ICpuService cpuService;

    @GetMapping("/cpus")
    public ResponseEntity<List<Cpu>> listarCpus() {
        return ResponseEntity.ok(cpuService.findAll());
    }

    @GetMapping("/cpus/{id}")
    public ResponseEntity<Cpu> buscarCpu(@PathVariable Long id) {
        return ResponseEntity.ok(cpuService.findById(id));
    }

    @PostMapping("/cpus")
    public ResponseEntity<?> crearCpu(@Valid @RequestBody Cpu cpu, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cpuService.save(cpu));
    }

    @PutMapping("/cpus/{id}")
    public ResponseEntity<?> actualizarCpu(
            @PathVariable Long id,
            @Valid @RequestBody Cpu cpu,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        return ResponseEntity.ok(cpuService.update(id, cpu));
    }

    @DeleteMapping("/cpus/{id}")
    public ResponseEntity<?> eliminarCpu(@PathVariable Long id) {

        cpuService.delete(id);

        return ResponseEntity.noContent().build();
    }

    private List<String> construirErrores(BindingResult result) {
        List<String> errores = new ArrayList<>();
        result.getFieldErrors().forEach(error ->
                errores.add("El campo '" + error.getField() + "' " + error.getDefaultMessage()));
        return errores;
    }
}
