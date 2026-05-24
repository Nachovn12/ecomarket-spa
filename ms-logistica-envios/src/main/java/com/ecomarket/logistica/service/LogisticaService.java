package com.ecomarket.logistica.service;

import com.ecomarket.logistica.dto.*;
import com.ecomarket.logistica.exception.*;
import com.ecomarket.logistica.model.*;
import com.ecomarket.logistica.model.enums.*;
import com.ecomarket.logistica.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogisticaService {
    
    private static final Logger log = LoggerFactory.getLogger(LogisticaService.class);

    private final EnvioRepository envioRepository;
    private final ProveedorRepository proveedorRepository;
    private final RutaEntregaRepository rutaEntregaRepository;
    private final SeguimientoEnvioRepository seguimientoEnvioRepository;

    public LogisticaService(EnvioRepository envioRepository, ProveedorRepository proveedorRepository, 
                            RutaEntregaRepository rutaEntregaRepository, SeguimientoEnvioRepository seguimientoEnvioRepository) {
        this.envioRepository = envioRepository;
        this.proveedorRepository = proveedorRepository;
        this.rutaEntregaRepository = rutaEntregaRepository;
        this.seguimientoEnvioRepository = seguimientoEnvioRepository;
    }

    // ==========================================
    // LOGICA DE ENVIOS Y SEGUIMIENTOS
    // ==========================================
    @Transactional
    public Envio crearEnvio(EnvioDTO dto) {
        if (dto.getIdPedido() == null) throw new IllegalArgumentException("No se puede crear Envio sin idPedido");
        if (dto.getOrigen() == null || dto.getOrigen().isBlank()) throw new IllegalArgumentException("No se puede crear Envio sin origen");
        if (dto.getDestino() == null || dto.getDestino().isBlank()) throw new IllegalArgumentException("No se puede crear Envio sin destino");
        if (dto.getFechaEstimadaEntrega() == null) throw new IllegalArgumentException("No se puede crear Envio sin fecha estimada de entrega");

        Envio envio = new Envio();
        envio.setIdPedido(dto.getIdPedido());
        envio.setOrigen(dto.getOrigen());
        envio.setDestino(dto.getDestino());
        envio.setFechaEstimadaEntrega(dto.getFechaEstimadaEntrega());
        envio.setEstado(EstadoEnvio.PREPARADO);

        if (dto.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor informado no existe"));
            if (!proveedor.getActivo()) {
                log.error("Intento de asociar proveedor inactivo ID: {}", proveedor.getId());
                throw new ConflictoNegocioException("Solo proveedores activos pueden asociarse a envios");
            }
            envio.setProveedor(proveedor);
        }

        envio = envioRepository.save(envio);
        log.info("Envio creado exitosamente con ID: {}", envio.getId());
        registrarSeguimiento(envio, EstadoEnvio.PREPARADO, "Envio creado y en preparacion", "Sistema");
        
        return envio;
    }

    public List<Envio> obtenerEnvios() { return envioRepository.findAll(); }

    public Envio obtenerEnvioPorId(Long id) {
        return envioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado"));
    }

    public List<Envio> obtenerEnviosPorPedido(Long idPedido) {
        return envioRepository.findByIdPedido(idPedido);
    }

    @Transactional
    public Envio actualizarEnvio(Long id, EnvioDTO dto) {
        Envio envio = obtenerEnvioPorId(id);
        if (dto.getOrigen() != null) envio.setOrigen(dto.getOrigen());
        if (dto.getDestino() != null) envio.setDestino(dto.getDestino());
        return envioRepository.save(envio);
    }

    @Transactional
    public void eliminarEnvio(Long id) {
        Envio envio = obtenerEnvioPorId(id);
        envioRepository.delete(envio);
    }

    @Transactional
    public Envio cambiarEstadoEnvio(Long id, CambioEstadoRequestDTO request) {
        Envio envio = obtenerEnvioPorId(id);
        if (envio.getEstado() == EstadoEnvio.ENTREGADO) {
            log.warn("Intento de cambiar estado a un envio ya ENTREGADO (ID: {})", id);
            throw new ConflictoNegocioException("Si el envio esta ENTREGADO, no debe permitir volver a otro estado");
        }
        envio.setEstado(request.getEstado());
        if (request.getUbicacion() != null) envio.setUbicacionActual(request.getUbicacion());
        envio = envioRepository.save(envio);
        
        log.info("Estado del envio {} cambiado a {}", id, request.getEstado());
        registrarSeguimiento(envio, request.getEstado(), request.getObservacion(), request.getActualizadoPor());
        return envio;
    }

    @Transactional
    public Envio registrarIncidencia(Long id, IncidenciaRequestDTO request) {
        Envio envio = obtenerEnvioPorId(id);
        if (envio.getEstado() == EstadoEnvio.ENTREGADO) {
            throw new ConflictoNegocioException("No se puede registrar incidencia en un envio ENTREGADO");
        }
        envio.setEstado(EstadoEnvio.CON_INCIDENCIA);
        envio.setMotivoIncidencia(request.getMotivoIncidencia());
        envio = envioRepository.save(envio);
        
        log.info("Incidencia registrada en el envio {}: {}", id, request.getMotivoIncidencia());
        String obs = "INCIDENCIA: " + request.getMotivoIncidencia() + ". " + (request.getObservacion() != null ? request.getObservacion() : "");
        registrarSeguimiento(envio, EstadoEnvio.CON_INCIDENCIA, obs, request.getActualizadoPor());
        return envio;
    }

    public List<SeguimientoEnvio> obtenerSeguimiento(Long idEnvio) {
        obtenerEnvioPorId(idEnvio); // Validar que existe
        return seguimientoEnvioRepository.findByEnvioIdOrderByFechaRegistroDesc(idEnvio);
    }

    private void registrarSeguimiento(Envio envio, EstadoEnvio estado, String observacion, String actualizadoPor) {
        SeguimientoEnvio seguimiento = new SeguimientoEnvio();
        seguimiento.setEnvio(envio);
        seguimiento.setEstado(estado);
        seguimiento.setUbicacion(envio.getUbicacionActual());
        seguimiento.setObservacion(observacion);
        seguimiento.setActualizadoPor(actualizadoPor);
        seguimientoEnvioRepository.save(seguimiento);
    }

    // ==========================================
    // LOGICA DE PROVEEDORES
    // ==========================================
    @Transactional
    public Proveedor crearProveedor(ProveedorDTO dto) {
        Proveedor prov = new Proveedor();
        prov.setRazonSocial(dto.getRazonSocial());
        prov.setRut(dto.getRut());
        prov.setContacto(dto.getContacto());
        prov.setEmail(dto.getEmail());
        prov.setTelefono(dto.getTelefono());
        prov.setTipoProveedor(dto.getTipoProveedor());
        prov.setCobertura(dto.getCobertura());
        prov = proveedorRepository.save(prov);
        log.info("Proveedor creado exitosamente con ID: {}", prov.getId());
        return prov;
    }

    public List<Proveedor> obtenerProveedores() { return proveedorRepository.findAll(); }

    public Proveedor obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
    }

    @Transactional
    public Proveedor actualizarProveedor(Long id, ProveedorDTO dto) {
        Proveedor prov = obtenerProveedorPorId(id);
        if (dto.getRazonSocial() != null) prov.setRazonSocial(dto.getRazonSocial());
        if (dto.getContacto() != null) prov.setContacto(dto.getContacto());
        if (dto.getEmail() != null) prov.setEmail(dto.getEmail());
        if (dto.getTelefono() != null) prov.setTelefono(dto.getTelefono());
        if (dto.getCobertura() != null) prov.setCobertura(dto.getCobertura());
        return proveedorRepository.save(prov);
    }

    @Transactional
    public void activarProveedor(Long id) {
        Proveedor prov = obtenerProveedorPorId(id);
        prov.setActivo(true);
        proveedorRepository.save(prov);
        log.info("Proveedor {} activado", id);
    }

    @Transactional
    public void desactivarProveedor(Long id) {
        Proveedor prov = obtenerProveedorPorId(id);
        prov.setActivo(false);
        proveedorRepository.save(prov);
        log.info("Proveedor {} desactivado. Regla: no se elimina fisicamente.", id);
    }

    public List<Proveedor> obtenerProveedoresActivos() { return proveedorRepository.findByActivoTrue(); }

    public List<Proveedor> buscarProveedores(String tipo, String cobertura) {
        return proveedorRepository.findByTipoProveedorAndCobertura(tipo, cobertura);
    }

    // ==========================================
    // LOGICA DE RUTAS DE ENTREGA
    // ==========================================
    @Transactional
    public RutaEntrega crearRuta(RutaEntregaDTO dto) {
        RutaEntrega ruta = new RutaEntrega();

        if (dto.getEstado() != null) {
            ruta.setEstado(dto.getEstado());
        } else {
            ruta.setEstado(EstadoRuta.PLANIFICADA);
        }

        ruta = rutaEntregaRepository.save(ruta);
        log.info("Ruta creada con ID: {}", ruta.getId());
        return ruta;
    }

    public List<RutaEntrega> obtenerRutas() { return rutaEntregaRepository.findAll(); }

    public RutaEntrega obtenerRutaPorId(Long id) {
        return rutaEntregaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada"));
    }

    @Transactional
    public RutaEntrega actualizarRuta(Long id, RutaEntregaDTO dto) {
        RutaEntrega ruta = obtenerRutaPorId(id);

        if (dto.getEstado() != null) {
            ruta.setEstado(dto.getEstado());
        }

        ruta = rutaEntregaRepository.save(ruta);
        log.info("Ruta {} actualizada correctamente", id);
        return ruta;
    }

    @Transactional
    public void eliminarRuta(Long id) {
        RutaEntrega ruta = obtenerRutaPorId(id);
        rutaEntregaRepository.delete(ruta);
    }

    @Transactional
    public RutaEntrega cambiarEstadoRuta(Long id, EstadoRuta nuevoEstado) {
        RutaEntrega ruta = obtenerRutaPorId(id);
        if (ruta.getEstado() == EstadoRuta.FINALIZADA) {
            log.warn("Intento de cambiar estado a una ruta FINALIZADA (ID: {})", id);
            throw new ConflictoNegocioException("Ruta FINALIZADA no debe volver a estados anteriores");
        }
        ruta.setEstado(nuevoEstado);
        ruta = rutaEntregaRepository.save(ruta);
        log.info("Ruta {} cambio al estado {}", id, nuevoEstado);
        return ruta;
    }
}
