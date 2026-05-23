package com.techplanner.componentservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "components")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "La marca no puede estar vacía")
    private String brand;

    @Column(nullable = false)
    @NotBlank(message = "El modelo no puede estar vacío")
    private String model;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    // Constructor vacío obligatorio para JPA
    public Component() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
