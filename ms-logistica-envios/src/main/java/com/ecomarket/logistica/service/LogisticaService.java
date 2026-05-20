package com.ecomarket.logistica.service;

import com.ecomarket.logistica.dto.EnvioDTO;
import com.ecomarket.logistica.model.Envio;
import com.ecomarket.logistica.model.Proveedor;
import com.ecomarket.logistica.model.Ruta;
import com.ecomarket.logistica.model.enums.EstadoEnvio;
import com.ecomarket.logistica.repository.EnvioRepository;
import com.ecomarket.logistica.repository.ProveedorRepository;
import com.ecomarket.logistica.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogisticaService {

    @Autowired
    private EnvioRepository envioRepository;
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private RutaRepository rutaRepository;

    // --- Logica de Proveedores ---
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }
    public List<Proveedor> obtenerProveedores() {
        return proveedorRepository.findAll();
    }
    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }
    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }

    // --- Logica de Rutas ---
    public Ruta guardarRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }
    public List<Ruta> obtenerRutas() {
        return rutaRepository.findAll();
    }
    public Optional<Ruta> obtenerRutaPorId(Long id) {
        return rutaRepository.findById(id);
    }
    public void eliminarRuta(Long id) {
        rutaRepository.deleteById(id);
    }

    // --- Logica de Envios ---
    public Envio crearEnvio(EnvioDTO dto) {
        Envio envio = new Envio();
        envio.setIdPedido(dto.getIdPedido());
        envio.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadoEnvio.PREPARACION);
        
        if (dto.getIdProveedor() != null) {
            proveedorRepository.findById(dto.getIdProveedor()).ifPresent(envio::setProveedor);
        }
        if (dto.getIdRuta() != null) {
            rutaRepository.findById(dto.getIdRuta()).ifPresent(envio::setRuta);
        }
        
        return envioRepository.save(envio);
    }

    public List<Envio> obtenerEnvios() {
        return envioRepository.findAll();
    }

    public Optional<Envio> obtenerEnvioPorId(Long id) {
        return envioRepository.findById(id);
    }

    public Optional<Envio> actualizarEstadoEnvio(Long id, EstadoEnvio nuevoEstado) {
        return envioRepository.findById(id).map(envio -> {
            envio.setEstado(nuevoEstado);
            return envioRepository.save(envio);
        });
    }

    public void eliminarEnvio(Long id) {
        envioRepository.deleteById(id);
    }
}
