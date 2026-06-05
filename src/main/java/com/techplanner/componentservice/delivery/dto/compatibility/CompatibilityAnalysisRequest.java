package com.techplanner.componentservice.delivery.dto.compatibility;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CompatibilityAnalysisRequest(
        @Valid @NotNull CpuInput cpu,
        @Valid @NotNull GpuInput gpu,
        @Valid @NotNull RamInput ram,
        @Valid @NotNull MotherboardInput motherboard,
        @Valid @NotNull PsuInput psu,
        @Valid @NotNull StorageInput storage
) {

    public record CpuInput(
            @NotBlank String brand,
            @NotBlank String model,
            @NotBlank String socket,
            @NotNull @Positive Integer cores,
            @NotNull @Positive Integer threads,
            @NotNull @Positive Integer tdp,
            @NotNull Boolean integratedGraphics,
            @NotNull @Positive BigDecimal price
    ) {
    }

    public record GpuInput(
            @NotBlank String brand,
            @NotBlank String model,
            @NotBlank String chipset,
            @NotNull @Positive Integer vram,
            @NotNull @Positive Integer recommendedWattage,
            @NotBlank String pcieVersion,
            @NotNull @Positive Integer lengthMm,
            @NotNull @Positive BigDecimal price
    ) {
    }

    public record RamInput(
            @NotBlank String brand,
            @NotBlank String model,
            @NotBlank String type,
            @NotNull @Positive Integer capacityGb,
            @NotNull @Positive Integer speedMHz,
            @NotNull @Positive Double voltage,
            @NotNull @Positive Integer sticks,
            @NotNull @Positive BigDecimal price
    ) {
    }

    public record MotherboardInput(
            @NotBlank String brand,
            @NotBlank String model,
            @NotBlank String socket,
            @NotBlank String ramType,
            @NotNull @Positive Integer maxRam,
            @NotNull @Positive Integer ramSlots,
            @NotNull @Positive Integer supportedRamSpeed,
            @NotBlank String pcieVersion,
            @NotBlank String formFactor,
            @NotNull @Positive Integer sataPorts,
            @NotNull @Positive Integer m2Slots,
            @NotNull @Positive BigDecimal price
    ) {
    }

    public record PsuInput(
            @NotBlank String brand,
            @NotBlank String model,
            @NotNull @Positive Integer wattage,
            @NotBlank String efficiency,
            @NotNull Boolean modular,
            @NotBlank String formFactor,
            @NotNull @Positive BigDecimal price
    ) {
    }

    public record StorageInput(
            @NotBlank String brand,
            @NotBlank String model,
            @NotBlank String type,
            @NotBlank String interfaceType,
            @NotNull @Positive Integer capacityGb,
            @NotNull @Positive Integer readSpeed,
            @NotNull @Positive Integer writeSpeed,
            @NotNull @Positive BigDecimal price
    ) {
    }
}