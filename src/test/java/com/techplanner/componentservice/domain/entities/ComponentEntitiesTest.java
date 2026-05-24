package com.techplanner.componentservice.domain.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentEntitiesTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDownValidator() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("Component getters y setters funcionan correctamente")
    void shouldSetAndGetComponentFields_whenUsed_returnAssignedValues() {
        Component component = createComponent();

        assertThat(component.getId()).isEqualTo(1L);
        assertThat(component.getBrand()).isEqualTo("AMD");
        assertThat(component.getModel()).isEqualTo("Ryzen 7 7800X3D");
        assertThat(component.getPrice()).isEqualByComparingTo("2399000");
    }

    @Test
    @DisplayName("Component válido no genera violaciones")
    void shouldValidateComponent_whenValid_returnNoViolations() {
        assertThat(validator.validate(createComponent())).isEmpty();
    }

    @Test
    @DisplayName("Component inválido genera violaciones")
    void shouldValidateComponent_whenInvalid_returnViolations() {
        Set<String> properties = validator.validate(new Component()).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertThat(properties).contains("brand", "model", "price");
    }

    @Test
    @DisplayName("Cpu getters, setters y validación funcionan correctamente")
    void shouldValidateCpu_whenValidAndInvalid_returnExpectedResults() {
        Cpu cpu = createCpu();

        assertThat(cpu.getSocket()).isEqualTo("AM5");
        assertThat(cpu.getCores()).isEqualTo(8);
        assertThat(validator.validate(cpu)).isEmpty();

        Set<String> properties = validator.validate(new Cpu()).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertThat(properties).contains("socket", "cores", "threads", "tdp");
    }

    @Test
    @DisplayName("Gpu getters, setters y validación funcionan correctamente")
    void shouldValidateGpu_whenValidAndInvalid_returnExpectedResults() {
        Gpu gpu = createGpu();

        assertThat(gpu.getVram()).isEqualTo(12);
        assertThat(validator.validate(gpu)).isEmpty();

        Set<String> properties = validator.validate(new Gpu()).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertThat(properties).contains("vram", "tdp", "pcieVersion");
    }

    @Test
    @DisplayName("Ram getters, setters y validación funcionan correctamente")
    void shouldValidateRam_whenValidAndInvalid_returnExpectedResults() {
        Ram ram = createRam();

        assertThat(ram.getType()).isEqualTo("DDR5");
        assertThat(validator.validate(ram)).isEmpty();

        Set<String> properties = validator.validate(new Ram()).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertThat(properties).contains("type", "capacity", "speed");
    }

    @Test
    @DisplayName("Storage getters, setters y validación funcionan correctamente")
    void shouldValidateStorage_whenValidAndInvalid_returnExpectedResults() {
        Storage storage = createStorage();

        assertThat(storage.getCapacityGb()).isEqualTo(2000);
        assertThat(storage.getType()).isEqualTo("NVMe");
        assertThat(storage.getInterfaceType()).isEqualTo("M.2");
        assertThat(validator.validate(storage)).isEmpty();

        Set<String> properties = validator.validate(new Storage()).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertThat(properties).contains("capacityGb", "type", "interfaceType");
    }

    @Test
    @DisplayName("Motherboard getters, setters y validación funcionan correctamente")
    void shouldValidateMotherboard_whenValidAndInvalid_returnExpectedResults() {
        Motherboard motherboard = createMotherboard();

        assertThat(motherboard.getRamType()).isEqualTo("DDR5");
        assertThat(validator.validate(motherboard)).isEmpty();

        Set<String> properties = validator.validate(new Motherboard()).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertThat(properties).contains("socket", "ramType", "maxRam", "pcieVersion");
    }

    @Test
    @DisplayName("Psu getters, setters y validación funcionan correctamente")
    void shouldValidatePsu_whenValidAndInvalid_returnExpectedResults() {
        Psu psu = createPsu();

        assertThat(psu.getEfficiency()).isEqualTo("80+ Gold");
        assertThat(validator.validate(psu)).isEmpty();

        Set<String> properties = validator.validate(new Psu()).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertThat(properties).contains("wattage", "efficiency");
    }

    private Component createComponent() {
        Component component = new Component();
        component.setId(1L);
        component.setBrand("AMD");
        component.setModel("Ryzen 7 7800X3D");
        component.setPrice(new BigDecimal("2399000"));
        return component;
    }

    private Cpu createCpu() {
        Cpu cpu = new Cpu();
        cpu.setId(1L);
        cpu.setBrand("AMD");
        cpu.setModel("Ryzen 7 7800X3D");
        cpu.setPrice(new BigDecimal("2399000"));
        cpu.setSocket("AM5");
        cpu.setCores(8);
        cpu.setThreads(16);
        cpu.setTdp(120);
        return cpu;
    }

    private Gpu createGpu() {
        Gpu gpu = new Gpu();
        gpu.setId(4L);
        gpu.setBrand("NVIDIA");
        gpu.setModel("RTX 4070 Ti");
        gpu.setPrice(new BigDecimal("4299000"));
        gpu.setVram(12);
        gpu.setTdp(285);
        gpu.setPcieVersion("PCIe 4.0");
        return gpu;
    }

    private Ram createRam() {
        Ram ram = new Ram();
        ram.setId(7L);
        ram.setBrand("Corsair");
        ram.setModel("Vengeance");
        ram.setPrice(new BigDecimal("399900"));
        ram.setType("DDR5");
        ram.setCapacity(32);
        ram.setSpeed(6000);
        return ram;
    }

    private Motherboard createMotherboard() {
        Motherboard motherboard = new Motherboard();
        motherboard.setId(10L);
        motherboard.setBrand("ASUS");
        motherboard.setModel("ROG Strix B650");
        motherboard.setPrice(new BigDecimal("1299000"));
        motherboard.setSocket("AM5");
        motherboard.setRamType("DDR5");
        motherboard.setMaxRam(128);
        motherboard.setPcieVersion("PCIe 5.0");
        return motherboard;
    }

    private Psu createPsu() {
        Psu psu = new Psu();
        psu.setId(13L);
        psu.setBrand("Corsair");
        psu.setModel("RM850x");
        psu.setPrice(new BigDecimal("699900"));
        psu.setWattage(850);
        psu.setEfficiency("80+ Gold");
        return psu;
    }

    private Storage createStorage() {
        Storage storage = new Storage();
        storage.setId(20L);
        storage.setBrand("Samsung");
        storage.setModel("990 PRO");
        storage.setPrice(new BigDecimal("1799000"));
        storage.setCapacityGb(2000);
        storage.setType("NVMe");
        storage.setInterfaceType("M.2");
        return storage;
    }
}