package com.techplanner.componentservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "motherboards")
public class Motherboard extends Component {

    @NotBlank(message = "El socket no puede estar vacío")
    private String socket;

    @NotBlank(message = "El tipo de RAM no puede estar vacío")
    private String ramType;

    @NotNull(message = "La RAM máxima no puede estar vacía")
    @Positive(message = "La RAM máxima debe ser mayor a 0")
    private Integer maxRam;

    @NotBlank(message = "La versión PCIe no puede estar vacía")
    private String pcieVersion;

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getRamType() {
        return ramType;
    }

    public void setRamType(String ramType) {
        this.ramType = ramType;
    }

    public Integer getMaxRam() {
        return maxRam;
    }

    public void setMaxRam(Integer maxRam) {
        this.maxRam = maxRam;
    }

    public String getPcieVersion() {
        return pcieVersion;
    }

    public void setPcieVersion(String pcieVersion) {
        this.pcieVersion = pcieVersion;
    }
}