package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.delivery.dto.compatibility.CompatibilityAnalysisRequest;
import com.techplanner.componentservice.delivery.mapper.CompatibilityComponentMapper;
import com.techplanner.compatibilitylib.analyzers.CompatibilityAnalyzer;
import com.techplanner.compatibilitylib.models.CompatibilityResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CompatibilityServiceImpl implements ICompatibilityService {

    private final CompatibilityAnalyzer compatibilityAnalyzer;
    private final CompatibilityComponentMapper compatibilityComponentMapper;

    public CompatibilityServiceImpl(CompatibilityAnalyzer compatibilityAnalyzer,
                                    CompatibilityComponentMapper compatibilityComponentMapper) {
        this.compatibilityAnalyzer = compatibilityAnalyzer;
        this.compatibilityComponentMapper = compatibilityComponentMapper;
    }

    @Override
    public CompatibilityResult analyze(CompatibilityAnalysisRequest request) {
        try {
            return compatibilityAnalyzer.analyze(compatibilityComponentMapper.toCompatibilityRequest(request));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}