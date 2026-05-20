package com.ecomarket.logistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El origen es obligatorio")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    private String destino;

    @NotNull(message = "La distancia es obligatoria")
    private Double distanciaKm;

    public Ruta() {}

    public Ruta(Long id, String origen, String destino, Double distanciaKm) {
        this.id = id;
        this.origen = origen;
        this.distanciaKm = distanciaKm;
    }

    public Long getId() { return id; }
    public Long getIdRuta() { return id; } // Alias requerido por el controlador legado
    public void setId(Long id) { this.id = id; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public String getDestination() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }
}
