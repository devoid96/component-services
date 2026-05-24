package com.techplanner.componentservice.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "cpus")
public class Cpu extends Component {

    @NotBlank(message = "El socket no puede estar vacío")
    private String socket;

    @NotNull(message = "Los núcleos no pueden estar vacíos")
    @Min(value = 1, message = "Los núcleos deben ser al menos 1")
    private Integer cores;

    @NotNull(message = "Los hilos no pueden estar vacíos")
    @Min(value = 1, message = "Los hilos deben ser al menos 1")
    private Integer threads;

    @NotNull(message = "El TDP no puede estar vacío")
    @Positive(message = "El TDP debe ser mayor a 0")
    private Integer tdp;

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public Integer getCores() {
        return cores;
    }

    public void setCores(Integer cores) {
        this.cores = cores;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Integer getTdp() {
        return tdp;
    }

    public void setTdp(Integer tdp) {
        this.tdp = tdp;
    }
}