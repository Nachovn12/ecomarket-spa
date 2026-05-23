package com.ecomarket.reportes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "indicadores_kpi")
public class IndicadorKPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El tipo de KPI es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoKPI tipo;

    @NotNull(message = "El valor del KPI es obligatorio")
    @PositiveOrZero(message = "El valor del KPI no puede ser negativo")
    @Column(nullable = false)
    private Double valor;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    private LocalDateTime fechaCalculo;

    @PrePersist
    public void prePersist() {
        this.fechaCalculo = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoKPI getTipo() { return tipo; }
    public void setTipo(TipoKPI tipo) { this.tipo = tipo; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaCalculo() { return fechaCalculo; }
    public void setFechaCalculo(LocalDateTime fechaCalculo) { this.fechaCalculo = fechaCalculo; }
}