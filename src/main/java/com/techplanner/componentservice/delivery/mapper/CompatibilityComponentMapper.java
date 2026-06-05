package com.techplanner.componentservice.delivery.mapper;

import com.techplanner.componentservice.delivery.dto.compatibility.CompatibilityAnalysisRequest;
import com.techplanner.componentservice.domain.entities.Cpu;
import com.techplanner.componentservice.domain.entities.Gpu;
import com.techplanner.componentservice.domain.entities.Motherboard;
import com.techplanner.componentservice.domain.entities.Psu;
import com.techplanner.componentservice.domain.entities.Ram;
import com.techplanner.componentservice.domain.entities.Storage;
import com.techplanner.compatibilitylib.enums.CpuSocket;
import com.techplanner.compatibilitylib.enums.FormFactor;
import com.techplanner.compatibilitylib.enums.PcieVersion;
import com.techplanner.compatibilitylib.enums.RamType;
import com.techplanner.compatibilitylib.enums.StorageType;
import com.techplanner.compatibilitylib.models.CompatibilityRequest;
import com.techplanner.compatibilitylib.models.CPU;
import com.techplanner.compatibilitylib.models.GPU;
import com.techplanner.compatibilitylib.models.PSU;
import com.techplanner.compatibilitylib.models.RAM;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CompatibilityComponentMapper {

    public CompatibilityRequest toCompatibilityRequest(CompatibilityAnalysisRequest request) {
        return CompatibilityRequest.builder()
                .cpu(toCpu(request.cpu()))
                .gpu(toGpu(request.gpu()))
                .ram(toRam(request.ram()))
                .motherboard(toMotherboard(request.motherboard()))
                .psu(toPsu(request.psu()))
                .storage(toStorage(request.storage()))
                .build();
    }

    public CompatibilityRequest toCompatibilityRequest(Cpu cpu, Gpu gpu, Ram ram, com.techplanner.componentservice.domain.entities.Motherboard motherboard, Psu psu, Storage storage) {
        return CompatibilityRequest.builder()
                .cpu(cpu != null ? toCpu(cpu) : null)
                .gpu(gpu != null ? toGpu(gpu) : null)
                .ram(ram != null ? toRam(ram) : null)
                .motherboard(motherboard != null ? toMotherboard(motherboard) : null)
                .psu(psu != null ? toPsu(psu) : null)
                .storage(storage != null ? toStorage(storage) : null)
                .build();
    }

    public CPU toCpu(CompatibilityAnalysisRequest.CpuInput input) {
        return CPU.builder()
                .brand(input.brand())
                .model(input.model())
                .socket(parseCpuSocket(input.socket()))
                .cores(input.cores())
                .threads(input.threads())
                .tdp(input.tdp())
                .integratedGraphics(input.integratedGraphics())
                .price(input.price())
                .build();
    }

    public GPU toGpu(CompatibilityAnalysisRequest.GpuInput input) {
        return GPU.builder()
                .brand(input.brand())
                .model(input.model())
                .chipset(input.chipset())
                .vram(input.vram())
                .recommendedWattage(input.recommendedWattage())
                .pcieVersion(parsePcieVersion(input.pcieVersion()))
                .lengthMm(input.lengthMm())
                .price(input.price())
                .build();
    }

    public RAM toRam(CompatibilityAnalysisRequest.RamInput input) {
        return RAM.builder()
                .brand(input.brand())
                .model(input.model())
                .type(parseRamType(input.type()))
                .capacityGb(input.capacityGb())
                .speedMHz(input.speedMHz())
                .voltage(input.voltage())
                .sticks(input.sticks())
                .price(input.price())
                .build();
    }

    public com.techplanner.compatibilitylib.models.Motherboard toMotherboard(CompatibilityAnalysisRequest.MotherboardInput input) {
        return com.techplanner.compatibilitylib.models.Motherboard.builder()
                .brand(input.brand())
                .model(input.model())
                .socket(parseCpuSocket(input.socket()))
                .ramType(parseRamType(input.ramType()))
                .maxRam(input.maxRam())
                .ramSlots(input.ramSlots())
                .supportedRamSpeed(input.supportedRamSpeed())
                .pcieVersion(parsePcieVersion(input.pcieVersion()))
                .formFactor(parseFormFactor(input.formFactor()))
                .sataPorts(input.sataPorts())
                .m2Slots(input.m2Slots())
                .price(input.price())
                .build();
    }

    public PSU toPsu(CompatibilityAnalysisRequest.PsuInput input) {
        return PSU.builder()
                .brand(input.brand())
                .model(input.model())
                .wattage(input.wattage())
                .efficiency(input.efficiency())
                .modular(input.modular())
                .formFactor(parseFormFactor(input.formFactor()))
                .price(input.price())
                .build();
    }

    public com.techplanner.compatibilitylib.models.Storage toStorage(CompatibilityAnalysisRequest.StorageInput input) {
        return com.techplanner.compatibilitylib.models.Storage.builder()
                .brand(input.brand())
                .model(input.model())
                .type(parseStorageType(input.type()))
                .interfaceType(input.interfaceType())
                .capacityGb(input.capacityGb())
                .readSpeed(input.readSpeed())
                .writeSpeed(input.writeSpeed())
                .price(input.price())
                .build();
    }

    public CPU toCpu(Cpu cpu) {
        return CPU.builder()
                .brand(cpu.getBrand())
                .model(cpu.getModel())
                .socket(parseCpuSocket(cpu.getSocket()))
                .cores(cpu.getCores())
                .threads(cpu.getThreads())
                .tdp(cpu.getTdp())
                .integratedGraphics(false)
                .price(cpu.getPrice())
                .build();
    }

    public GPU toGpu(Gpu gpu) {
        return GPU.builder()
                .brand(gpu.getBrand())
                .model(gpu.getModel())
                .chipset(defaultString(gpu.getBrand(), gpu.getModel()))
                .vram(gpu.getVram())
                .recommendedWattage(gpu.getTdp())
                .pcieVersion(parsePcieVersion(gpu.getPcieVersion()))
                .lengthMm(0)
                .price(gpu.getPrice())
                .build();
    }

    public RAM toRam(Ram ram) {
        return RAM.builder()
                .brand(ram.getBrand())
                .model(ram.getModel())
                .type(parseRamType(ram.getType()))
                .capacityGb(ram.getCapacity())
                .speedMHz(ram.getSpeed())
                .voltage(1.35d)
                .sticks(2)
                .price(ram.getPrice())
                .build();
    }

    public com.techplanner.compatibilitylib.models.Motherboard toMotherboard(com.techplanner.componentservice.domain.entities.Motherboard motherboard) {
        return com.techplanner.compatibilitylib.models.Motherboard.builder()
                .brand(motherboard.getBrand())
                .model(motherboard.getModel())
                .socket(parseCpuSocket(motherboard.getSocket()))
                .ramType(parseRamType(motherboard.getRamType()))
                .maxRam(motherboard.getMaxRam())
                .ramSlots(4)
                .supportedRamSpeed(0)
                .pcieVersion(parsePcieVersion(motherboard.getPcieVersion()))
                .formFactor(FormFactor.ATX)
                .sataPorts(4)
                .m2Slots(2)
                .price(motherboard.getPrice())
                .build();
    }

    public PSU toPsu(Psu psu) {
        return PSU.builder()
                .brand(psu.getBrand())
                .model(psu.getModel())
                .wattage(psu.getWattage())
                .efficiency(psu.getEfficiency())
                .modular(false)
                .formFactor(FormFactor.ATX)
                .price(psu.getPrice())
                .build();
    }

    public com.techplanner.compatibilitylib.models.Storage toStorage(Storage storage) {
        return com.techplanner.compatibilitylib.models.Storage.builder()
                .brand(storage.getBrand())
                .model(storage.getModel())
                .type(parseStorageType(storage.getType()))
                .interfaceType(storage.getInterfaceType())
                .capacityGb(storage.getCapacityGb())
                .readSpeed(0)
                .writeSpeed(0)
                .price(storage.getPrice())
                .build();
    }

    private CpuSocket parseCpuSocket(String value) {
        return CpuSocket.valueOf(normalize(value));
    }

    private RamType parseRamType(String value) {
        return RamType.valueOf(normalize(value));
    }

    private PcieVersion parsePcieVersion(String value) {
        return PcieVersion.valueOf(normalize(value));
    }

    private FormFactor parseFormFactor(String value) {
        return FormFactor.valueOf(normalize(value));
    }

    private StorageType parseStorageType(String value) {
        return StorageType.valueOf(normalize(value));
    }

    private String normalize(String value) {
        return value.trim()
                .toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');
    }

    private String defaultString(String brand, String model) {
        return brand + " " + model;
    }
}