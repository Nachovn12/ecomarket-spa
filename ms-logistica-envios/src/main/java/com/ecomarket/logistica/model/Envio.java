package com.ecomarket.logistica.model;

import com.ecomarket.logistica.model.enums.EstadoEnvio;
import jakarta.persistence.*;

@Entity
@Table(name = "envios")
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnvio;
    
    @Column(nullable = false)
    private Long idPedido;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado;
    
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;
    
    @ManyToOne
    @JoinColumn(name = "id_ruta")
    private Ruta ruta;

    // Getters y Setters
    public Long getIdEnvio() { return idEnvio; }
    public void setIdEnvio(Long idEnvio) { this.idEnvio = idEnvio; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    public Ruta getRuta() { return ruta; }
    public void setRuta(Ruta ruta) { this.ruta = ruta; }
}
