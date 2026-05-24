package com.techplanner.componentservice.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "storages")
public class Storage extends Component {

    @NotNull(message = "La capacidad no puede estar vacía")
    @Positive(message = "La capacidad debe ser mayor a 0")
    private Integer capacityGb;

    @NotBlank(message = "El tipo de almacenamiento no puede estar vacío")
    private String type;

    @NotBlank(message = "El tipo de interfaz no puede estar vacío")
    private String interfaceType;

    public Integer getCapacityGb() {
        return capacityGb;
    }

    public void setCapacityGb(Integer capacityGb) {
        this.capacityGb = capacityGb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }
}