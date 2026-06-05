package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.delivery.dto.compatibility.CompatibilityAnalysisRequest;
import com.techplanner.componentservice.domain.services.ICompatibilityService;
import com.techplanner.compatibilitylib.models.CompatibilityResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/component-service/compatibility", "/api/${api.version}/component-service/compatibility"})
@CrossOrigin(origins = {"http://localhost:4200"})
public class CompatibilityRestController {

    private final ICompatibilityService compatibilityService;

    public CompatibilityRestController(ICompatibilityService compatibilityService) {
        this.compatibilityService = compatibilityService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<CompatibilityResult> analyze(@Valid @RequestBody CompatibilityAnalysisRequest request) {
        return ResponseEntity.ok(compatibilityService.analyze(request));
    }
}