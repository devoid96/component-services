package com.techplanner.componentservice.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "gpus")
public class Gpu extends Component {

    @NotNull(message = "La VRAM no puede estar vacía")
    @Positive(message = "La VRAM debe ser mayor a 0")
    private Integer vram;

    @NotNull(message = "El TDP no puede estar vacío")
    @Positive(message = "El TDP debe ser mayor a 0")
    private Integer tdp;

    @NotBlank(message = "La versión PCIe no puede estar vacía")
    private String pcieVersion;

    public Integer getVram() {
        return vram;
    }

    public void setVram(Integer vram) {
        this.vram = vram;
    }

    public Integer getTdp() {
        return tdp;
    }

    public void setTdp(Integer tdp) {
        this.tdp = tdp;
    }

    public String getPcieVersion() {
        return pcieVersion;
    }

    public void setPcieVersion(String pcieVersion) {
        this.pcieVersion = pcieVersion;
    }
}