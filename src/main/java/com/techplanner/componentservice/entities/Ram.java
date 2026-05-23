package com.techplanner.componentservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "rams")
public class Ram extends Component {

    @NotBlank(message = "El tipo de memoria no puede estar vacío")
    private String type;

    @NotNull(message = "La capacidad no puede estar vacía")
    @Positive(message = "La capacidad debe ser mayor a 0")
    private Integer capacity;

    @NotNull(message = "La velocidad no puede estar vacía")
    @Positive(message = "La velocidad debe ser mayor a 0")
    private Integer speed;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }
}