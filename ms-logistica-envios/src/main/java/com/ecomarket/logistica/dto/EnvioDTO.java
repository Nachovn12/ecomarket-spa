package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoEnvio;

public class EnvioDTO {
    private Long idPedido;
    private EstadoEnvio estado;
    private Long idProveedor;
    private Long idRuta;

    // Getters y Setters
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }
    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }
    public Long getIdRuta() { return idRuta; }
    public void setIdRuta(Long idRuta) { this.idRuta = idRuta; }
}
