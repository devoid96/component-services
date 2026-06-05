package com.techplanner.componentservice.delivery.rest;

import com.techplanner.componentservice.domain.services.ICompatibilityService;
import com.techplanner.compatibilitylib.enums.CompatibilityStatus;
import com.techplanner.compatibilitylib.models.CompatibilityResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompatibilityRestController.class)
class CompatibilityRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICompatibilityService compatibilityService;

    @Test
    @DisplayName("POST /compatibility/analyze retorna CompatibilityResult")
    void analyze_returnsCompatibilityResult() throws Exception {
        CompatibilityResult result = CompatibilityResult.builder()
                .compatible(true)
                .status(CompatibilityStatus.COMPATIBLE)
                .compatibilityScore(92)
                .estimatedPowerConsumption(430)
                .recommendedPsu(550)
                .build();
        when(compatibilityService.analyze(any())).thenReturn(result);

        mockMvc.perform(post("/api/v1/component-service/compatibility/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sampleBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.compatible").value(true))
                .andExpect(jsonPath("$.status").value("COMPATIBLE"))
                .andExpect(jsonPath("$.compatibilityScore").value(92));

        verify(compatibilityService, times(1)).analyze(any());
    }

    private String sampleBody() {
        return """
                {
                  "cpu": {"brand":"AMD","model":"Ryzen 7 7800X3D","socket":"AM5","cores":8,"threads":16,"tdp":120,"integratedGraphics":false,"price":2399000},
                  "gpu": {"brand":"NVIDIA","model":"RTX 4070","chipset":"AD104","vram":12,"recommendedWattage":650,"pcieVersion":"PCIE_4","lengthMm":267,"price":3299000},
                  "ram": {"brand":"Corsair","model":"Vengeance","type":"DDR5","capacityGb":32,"speedMHz":6000,"voltage":1.35,"sticks":2,"price":899000},
                  "motherboard": {"brand":"ASUS","model":"TUF Gaming B650","socket":"AM5","ramType":"DDR5","maxRam":128,"ramSlots":4,"supportedRamSpeed":6000,"pcieVersion":"PCIE_5","formFactor":"ATX","sataPorts":4,"m2Slots":2,"price":1299000},
                  "psu": {"brand":"Corsair","model":"RM850e","wattage":850,"efficiency":"80+ Gold","modular":true,"formFactor":"ATX","price":749000},
                  "storage": {"brand":"Kingston","model":"NV2","type":"NVME","interfaceType":"PCIe 4.0","capacityGb":1000,"readSpeed":3500,"writeSpeed":2800,"price":399000}
                }
                """;
    }
}