package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class EnvioDTO {

    @Positive(message = "El ID debe ser positivo")
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID del pedido debe ser positivo")
    private Long idPedido;

    @NotBlank(message = "El origen es obligatorio")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    private String destino;

    private EstadoEnvio estado;
    private String ubicacionActual;
    private String observacion;
    private String motivoIncidencia;

    @NotNull(message = "La fecha estimada de entrega es obligatoria")
    @FutureOrPresent(message = "La fecha estimada de entrega no puede estar en el pasado")
    private LocalDateTime fechaEstimadaEntrega;

    @Positive(message = "El ID del proveedor debe ser positivo")
    private Long proveedorId;

    @Positive(message = "El ID de la ruta debe ser positivo")
    private Long rutaEntregaId;

    public EnvioDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }

    public String getUbicacionActual() { return ubicacionActual; }
    public void setUbicacionActual(String ubicacionActual) { this.ubicacionActual = ubicacionActual; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getMotivoIncidencia() { return motivoIncidencia; }
    public void setMotivoIncidencia(String motivoIncidencia) { this.motivoIncidencia = motivoIncidencia; }

    public LocalDateTime getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) {
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
    }

    public Long getProveedorId() { return proveedorId; }
    public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }

    public Long getRutaEntregaId() { return rutaEntregaId; }
    public void setRutaEntregaId(Long rutaEntregaId) { this.rutaEntregaId = rutaEntregaId; }
}
