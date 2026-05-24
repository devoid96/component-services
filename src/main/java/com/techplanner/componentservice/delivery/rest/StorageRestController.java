package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.domain.entities.Storage;
import com.techplanner.componentservice.domain.services.IStorageService;
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
public class StorageRestController {

    @Autowired
    private IStorageService storageService;

    @GetMapping("/storages")
    public ResponseEntity<List<Storage>> listarStorages() {
        return ResponseEntity.ok(storageService.findAll());
    }

    @GetMapping("/storages/{id}")
    public ResponseEntity<Storage> buscarStorage(@PathVariable Long id) {
        return ResponseEntity.ok(storageService.findById(id));
    }

    @PostMapping("/storages")
    public ResponseEntity<?> crearStorage(@Valid @RequestBody Storage storage, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(storageService.save(storage));
    }

    @PutMapping("/storages/{id}")
    public ResponseEntity<?> actualizarStorage(
            @PathVariable Long id,
            @Valid @RequestBody Storage storage,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }

        return ResponseEntity.ok(storageService.update(id, storage));
    }

    @DeleteMapping("/storages/{id}")
    public ResponseEntity<?> eliminarStorage(@PathVariable Long id) {
        storageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private List<String> construirErrores(BindingResult result) {
        List<String> errores = new ArrayList<>();
        result.getFieldErrors().forEach(error ->
                errores.add("El campo '" + error.getField() + "' " + error.getDefaultMessage()));
        return errores;
    }
}