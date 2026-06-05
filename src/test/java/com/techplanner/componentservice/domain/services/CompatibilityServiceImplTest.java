package com.techplanner.componentservice.domain.services;

import com.techplanner.componentservice.delivery.dto.compatibility.CompatibilityAnalysisRequest;
import com.techplanner.componentservice.delivery.mapper.CompatibilityComponentMapper;
import com.techplanner.compatibilitylib.analyzers.CompatibilityAnalyzer;
import com.techplanner.compatibilitylib.enums.CompatibilityStatus;
import com.techplanner.compatibilitylib.models.CompatibilityRequest;
import com.techplanner.compatibilitylib.models.CompatibilityResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompatibilityServiceImplTest {

    @Test
    @DisplayName("analyze delega en CompatibilityAnalyzer con el request mapeado")
    void analyze_delegatesToAnalyzer() {
        CompatibilityAnalyzer analyzer = mock(CompatibilityAnalyzer.class);
        CompatibilityComponentMapper mapper = new CompatibilityComponentMapper();
        CompatibilityServiceImpl service = new CompatibilityServiceImpl(analyzer, mapper);

        CompatibilityAnalysisRequest request = sampleRequest();
        CompatibilityRequest mappedRequest = mapper.toCompatibilityRequest(request);
        CompatibilityResult expected = CompatibilityResult.builder()
                .compatible(true)
                .status(CompatibilityStatus.COMPATIBLE)
                .compatibilityScore(90)
                .estimatedPowerConsumption(430)
                .recommendedPsu(550)
                .build();

        when(analyzer.analyze(mappedRequest)).thenReturn(expected);

        CompatibilityResult result = service.analyze(request);

        assertThat(result).isSameAs(expected);
        verify(analyzer).analyze(mappedRequest);
    }

    private CompatibilityAnalysisRequest sampleRequest() {
        return new CompatibilityAnalysisRequest(
                new CompatibilityAnalysisRequest.CpuInput("AMD", "Ryzen 7 7800X3D", "AM5", 8, 16, 120, false, new BigDecimal("2399000")),
                new CompatibilityAnalysisRequest.GpuInput("NVIDIA", "RTX 4070", "AD104", 12, 650, "PCIE_4", 267, new BigDecimal("3299000")),
                new CompatibilityAnalysisRequest.RamInput("Corsair", "Vengeance", "DDR5", 32, 6000, 1.35d, 2, new BigDecimal("899000")),
                new CompatibilityAnalysisRequest.MotherboardInput("ASUS", "TUF Gaming B650", "AM5", "DDR5", 128, 4, 6000, "PCIE_5", "ATX", 4, 2, new BigDecimal("1299000")),
                new CompatibilityAnalysisRequest.PsuInput("Corsair", "RM850e", 850, "80+ Gold", true, "ATX", new BigDecimal("749000")),
                new CompatibilityAnalysisRequest.StorageInput("Kingston", "NV2", "NVME", "PCIe 4.0", 1000, 3500, 2800, new BigDecimal("399000"))
        );
    }
}