package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.delivery.dto.compatibility.CompatibilityAnalysisRequest;
import com.techplanner.compatibilitylib.models.CompatibilityResult;

public interface ICompatibilityService {

    CompatibilityResult analyze(CompatibilityAnalysisRequest request);
}