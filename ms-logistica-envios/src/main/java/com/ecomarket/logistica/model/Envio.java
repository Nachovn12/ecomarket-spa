package com.ecomarket.logistica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.ecomarket.logistica.model.enums.EstadoEnvio;

@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;

    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "ruta_id")
    private Ruta ruta;

    public Envio() {}

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoEnvio.PENDIENTE;
        }
    }

    public Long getId() { return id; }
    public Long getIdEnvio() { return id; } // Alias requerido por EnvioController
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public void setIdPedido(Long pedidoId) { this.pedidoId = pedidoId; } // Alias requerido por LogisticaService

    public EstadoEnvio getEstado() { return estado; }
    public void setStatus(EstadoEnvio estado) { this.estado = estado; } // Flexibilidad opcional
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public Ruta getRuta() { return ruta; }
    public void setRuta(Ruta ruta) { this.ruta = ruta; }
}
