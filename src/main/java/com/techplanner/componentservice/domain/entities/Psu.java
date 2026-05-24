package com.techplanner.componentservice.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "psus")
public class Psu extends Component {

    @NotNull(message = "La potencia no puede estar vacía")
    @Positive(message = "La potencia debe ser mayor a 0")
    private Integer wattage;

    @NotBlank(message = "La eficiencia no puede estar vacía")
    private String efficiency;

    public Integer getWattage() {
        return wattage;
    }

    public void setWattage(Integer wattage) {
        this.wattage = wattage;
    }

    public String getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }
}