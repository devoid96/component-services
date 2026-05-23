package com.techplanner.componentservice.controllers;

import com.techplanner.componentservice.entities.Cpu;
import com.techplanner.componentservice.services.ICpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Cpu>> listarCpus() {
        List<Cpu> cpus = cpuService.findAll();
        return ResponseEntity.ok(cpus);
    }

    @GetMapping("/cpus/{id}")
    public ResponseEntity<?> buscarCpu(@PathVariable Long id) {

        Optional<Cpu> cpu = cpuService.findById(id);

        if (cpu.isPresent()) {
            return ResponseEntity.ok(cpu.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", "No existe un CPU con id " + id));
    }

    @PostMapping("/cpus")
    public ResponseEntity<Cpu> crearCpu(@RequestBody Cpu cpu) {

        Cpu nuevo = cpuService.save(cpu);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevo);
    }

    @PutMapping("/cpus/{id}")
    public ResponseEntity<?> actualizarCpu(
            @PathVariable Long id,
            @RequestBody Cpu cpu) {

        if (cpuService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No existe un CPU con id " + id));
        }

        Cpu actualizado = cpuService.update(id, cpu);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/cpus")
    public ResponseEntity<?> eliminarCpu(
            @RequestBody Cpu cpu) {

        if (cpu.getId() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "Debe enviar el id del CPU a eliminar"));
        }

        Optional<Cpu> cpuEncontrado = cpuService.findById(cpu.getId());

        if (cpuEncontrado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No existe un CPU con id " + cpu.getId()));
        }

        cpuService.delete(cpuEncontrado.get());

        return ResponseEntity.ok(Map.of("mensaje", "CPU eliminado correctamente"));
    }
}
