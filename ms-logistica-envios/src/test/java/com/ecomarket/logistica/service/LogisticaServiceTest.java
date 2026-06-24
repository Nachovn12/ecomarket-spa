package com.ecomarket.logistica.service;

import com.ecomarket.logistica.dto.EnvioDTO;
import com.ecomarket.logistica.exception.ResourceNotFoundException;
import com.ecomarket.logistica.model.Envio;
import com.ecomarket.logistica.model.Proveedor;
import com.ecomarket.logistica.model.SeguimientoEnvio;
import com.ecomarket.logistica.model.enums.EstadoEnvio;
import com.ecomarket.logistica.repository.EnvioRepository;
import com.ecomarket.logistica.repository.ProveedorRepository;
import com.ecomarket.logistica.repository.RutaEntregaRepository;
import com.ecomarket.logistica.repository.SeguimientoEnvioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogisticaServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private RutaEntregaRepository rutaEntregaRepository;

    @Mock
    private SeguimientoEnvioRepository seguimientoEnvioRepository;

    @InjectMocks
    private LogisticaService logisticaService;

    @Test
    void asignarMejorProveedor_Exito() {
        Envio envio = new Envio();
        envio.setOrigen("Santiago");
        envio.setDestino("Concepcion");

        Proveedor provLento = new Proveedor();
        provLento.setId(1L);
        provLento.setPlazoDespachoHoras(24);

        Proveedor provRapido = new Proveedor();
        provRapido.setId(2L);
        provRapido.setPlazoDespachoHoras(12);

        List<Proveedor> proveedores = List.of(provLento, provRapido);

        Proveedor asignado = logisticaService.asignarMejorProveedor(envio, proveedores);

        assertNotNull(asignado);
        assertEquals(2L, asignado.getId());
        assertEquals(2L, envio.getProveedor().getId());
        assertNotNull(envio.getFechaEstimadaEntrega());
    }

    @Test
    void asignarMejorProveedor_ListaVacia_LanzaExcepcion() {
        Envio envio = new Envio();
        assertThrows(IllegalArgumentException.class, () -> logisticaService.asignarMejorProveedor(envio, List.of()));
    }

    @Test
    void crearEnvio_SinProveedor_UsaEtaCalculator() {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdPedido(100L);
        dto.setOrigen("Arica");
        dto.setDestino("Iquique");

        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> {
            Envio e = i.getArgument(0);
            e.setId(1L);
            return e;
        });

        Envio creado = logisticaService.crearEnvio(dto);

        assertNotNull(creado.getId());
        assertEquals(EstadoEnvio.PREPARADO, creado.getEstado());
        assertNull(creado.getProveedor());
        assertNotNull(creado.getFechaEstimadaEntrega());
        verify(seguimientoEnvioRepository, times(1)).save(any(SeguimientoEnvio.class));
    }

    @Test
    void crearEnvio_ConProveedor_Exito() {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdPedido(100L);
        dto.setOrigen("Arica");
        dto.setDestino("Iquique");
        dto.setProveedorId(10L);

        Proveedor prov = new Proveedor();
        prov.setId(10L);
        prov.setActivo(true);

        when(proveedorRepository.findById(10L)).thenReturn(Optional.of(prov));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> {
            Envio e = i.getArgument(0);
            e.setId(1L);
            return e;
        });

        Envio creado = logisticaService.crearEnvio(dto);

        assertNotNull(creado.getId());
        assertEquals(10L, creado.getProveedor().getId());
        verify(seguimientoEnvioRepository, times(1)).save(any(SeguimientoEnvio.class));
    }

    @Test
    void crearEnvio_ConFechaEstimada_Exito() {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdPedido(100L);
        dto.setOrigen("Arica");
        dto.setDestino("Iquique");
        dto.setFechaEstimadaEntrega(java.time.LocalDateTime.now().plusDays(2));

        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> {
            Envio e = i.getArgument(0);
            e.setId(1L);
            return e;
        });

        Envio creado = logisticaService.crearEnvio(dto);
        assertNotNull(creado.getFechaEstimadaEntrega());
    }

    @Test
    void crearEnvio_ProveedorInactivo_Excepcion() {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdPedido(100L);
        dto.setOrigen("Arica");
        dto.setDestino("Iquique");
        dto.setProveedorId(10L);

        Proveedor prov = new Proveedor();
        prov.setId(10L);
        prov.setActivo(false);

        when(proveedorRepository.findById(10L)).thenReturn(Optional.of(prov));
        assertThrows(com.ecomarket.logistica.exception.ConflictoNegocioException.class, () -> logisticaService.crearEnvio(dto));
    }

    @Test
    void obtenerEnvioPorId_NoExiste_LanzaExcepcion() {
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> logisticaService.obtenerEnvioPorId(99L));
    }

    @Test
    void obtenerEnvios_Exito() {
        Envio e = new Envio();
        when(envioRepository.findAll()).thenReturn(List.of(e));
        assertEquals(1, logisticaService.obtenerEnvios().size());
    }

    @Test
    void obtenerEnviosPorPedido_Exito() {
        Envio e = new Envio();
        when(envioRepository.findByIdPedido(1L)).thenReturn(List.of(e));
        assertEquals(1, logisticaService.obtenerEnviosPorPedido(1L).size());
    }

    @Test
    void crearProveedor_Exito() {
        com.ecomarket.logistica.dto.ProveedorDTO dto = new com.ecomarket.logistica.dto.ProveedorDTO();
        dto.setRazonSocial("Test");
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(i -> i.getArgument(0));
        Proveedor p = logisticaService.crearProveedor(dto);
        assertEquals("Test", p.getRazonSocial());
    }

    @Test
    void crearRuta_Exito() {
        com.ecomarket.logistica.dto.RutaEntregaDTO dto = new com.ecomarket.logistica.dto.RutaEntregaDTO();
        when(rutaEntregaRepository.save(any(com.ecomarket.logistica.model.RutaEntrega.class))).thenAnswer(i -> i.getArgument(0));
        com.ecomarket.logistica.model.RutaEntrega r = logisticaService.crearRuta(dto);
        assertEquals(com.ecomarket.logistica.model.enums.EstadoRuta.PLANIFICADA, r.getEstado());
    }

    @Test
    void cambiarEstadoEnvio_Exito() {
        Envio envio = new Envio();
        envio.setEstado(EstadoEnvio.PREPARADO);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));
        
        com.ecomarket.logistica.dto.CambioEstadoRequestDTO req = new com.ecomarket.logistica.dto.CambioEstadoRequestDTO();
        req.setEstado(EstadoEnvio.EN_CAMINO);
        req.setActualizadoPor("User");
        req.setUbicacion("Nueva Ubicacion");
        
        Envio res = logisticaService.cambiarEstadoEnvio(1L, req);
        assertEquals(EstadoEnvio.EN_CAMINO, res.getEstado());
        assertEquals("Nueva Ubicacion", res.getUbicacionActual());
    }

    // Cubre la rama: if (request.getUbicacion() != null) → false
    @Test
    void cambiarEstadoEnvio_SinUbicacion_Exito() {
        Envio envio = new Envio();
        envio.setEstado(EstadoEnvio.PREPARADO);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));

        com.ecomarket.logistica.dto.CambioEstadoRequestDTO req = new com.ecomarket.logistica.dto.CambioEstadoRequestDTO();
        req.setEstado(EstadoEnvio.EN_CAMINO);
        req.setActualizadoPor("User");
        // sin ubicacion → rama false del if

        Envio res = logisticaService.cambiarEstadoEnvio(1L, req);
        assertEquals(EstadoEnvio.EN_CAMINO, res.getEstado());
        assertNull(res.getUbicacionActual());
    }

    // Cubre la rama: ternario (request.getObservacion() != null) → true
    @Test
    void registrarIncidencia_ConObservacion_Exito() {
        Envio e = new Envio();
        e.setEstado(EstadoEnvio.EN_CAMINO);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(e));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));

        com.ecomarket.logistica.dto.IncidenciaRequestDTO req = new com.ecomarket.logistica.dto.IncidenciaRequestDTO();
        req.setMotivoIncidencia("Accidente");
        req.setObservacion("El paquete cayó al agua");
        req.setActualizadoPor("Admin");

        Envio res = logisticaService.registrarIncidencia(1L, req);
        assertEquals(EstadoEnvio.CON_INCIDENCIA, res.getEstado());
        assertEquals("Accidente", res.getMotivoIncidencia());
    }

    // Cubre ramas: if (dto.getOrigen() != null) → false, if (dto.getDestino() != null) → false
    @Test
    void actualizarEnvio_SinCambios_Exito() {
        Envio e = new Envio();
        e.setOrigen("OrigenOriginal");
        e.setDestino("DestinoOriginal");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(e));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));

        com.ecomarket.logistica.dto.EnvioDTO dto = new com.ecomarket.logistica.dto.EnvioDTO();
        // sin setear origen ni destino → ambas ramas false

        Envio res = logisticaService.actualizarEnvio(1L, dto);
        assertEquals("OrigenOriginal", res.getOrigen());
        assertEquals("DestinoOriginal", res.getDestino());
    }

    @Test
    void actualizarEnvio_Exito() {
        Envio e = new Envio();
        when(envioRepository.findById(1L)).thenReturn(Optional.of(e));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));
        
        com.ecomarket.logistica.dto.EnvioDTO dto = new com.ecomarket.logistica.dto.EnvioDTO();
        dto.setOrigen("NuevoOrigen");
        dto.setDestino("NuevoDestino");
        
        Envio res = logisticaService.actualizarEnvio(1L, dto);
        assertEquals("NuevoOrigen", res.getOrigen());
        assertEquals("NuevoDestino", res.getDestino());
    }


    @Test
    void eliminarEnvio_Exito() {
        Envio e = new Envio();
        when(envioRepository.findById(1L)).thenReturn(Optional.of(e));
        logisticaService.eliminarEnvio(1L);
        verify(envioRepository, times(1)).delete(e);
    }

    @Test
    void registrarIncidencia_Exito() {
        Envio e = new Envio();
        e.setEstado(EstadoEnvio.EN_CAMINO);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(e));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));

        com.ecomarket.logistica.dto.IncidenciaRequestDTO req = new com.ecomarket.logistica.dto.IncidenciaRequestDTO();
        req.setMotivoIncidencia("Robo");
        req.setObservacion("Test");
        req.setActualizadoPor("Admin");
        
        Envio res = logisticaService.registrarIncidencia(1L, req);
        assertEquals(EstadoEnvio.CON_INCIDENCIA, res.getEstado());
        assertEquals("Robo", res.getMotivoIncidencia());
    }

    @Test
    void obtenerProveedores_Exito() {
        Proveedor p = new Proveedor();
        when(proveedorRepository.findAll()).thenReturn(List.of(p));
        assertEquals(1, logisticaService.obtenerProveedores().size());
    }

    @Test
    void obtenerProveedorPorId_Exito() {
        Proveedor p = new Proveedor();
        p.setId(1L);
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        assertEquals(1L, logisticaService.obtenerProveedorPorId(1L).getId());
    }

    @Test
    void actualizarProveedor_Exito() {
        Proveedor p = new Proveedor();
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(i -> i.getArgument(0));

        com.ecomarket.logistica.dto.ProveedorDTO dto = new com.ecomarket.logistica.dto.ProveedorDTO();
        dto.setRazonSocial("NuevaRazon");
        dto.setContacto("NuevoContacto");
        dto.setEmail("a@a.com");
        dto.setTelefono("123");
        dto.setCobertura("LOCAL");

        Proveedor res = logisticaService.actualizarProveedor(1L, dto);
        assertEquals("NuevaRazon", res.getRazonSocial());
        assertEquals("NuevoContacto", res.getContacto());
        assertEquals("a@a.com", res.getEmail());
    }

    @Test
    void desactivarProveedor_Exito() {
        Proveedor p = new Proveedor();
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        logisticaService.desactivarProveedor(1L);
        verify(proveedorRepository, times(1)).save(p);
        assertEquals(false, p.getActivo());
    }

    @Test
    void obtenerRutas_Exito() {
        com.ecomarket.logistica.model.RutaEntrega r = new com.ecomarket.logistica.model.RutaEntrega();
        when(rutaEntregaRepository.findAll()).thenReturn(List.of(r));
        assertEquals(1, logisticaService.obtenerRutas().size());
    }

    @Test
    void obtenerRutaPorId_Exito() {
        com.ecomarket.logistica.model.RutaEntrega r = new com.ecomarket.logistica.model.RutaEntrega();
        r.setId(1L);
        when(rutaEntregaRepository.findById(1L)).thenReturn(Optional.of(r));
        assertEquals(1L, logisticaService.obtenerRutaPorId(1L).getId());
    }

    @Test
    void actualizarRuta_Exito() {
        com.ecomarket.logistica.model.RutaEntrega r = new com.ecomarket.logistica.model.RutaEntrega();
        when(rutaEntregaRepository.findById(1L)).thenReturn(Optional.of(r));
        when(rutaEntregaRepository.save(any(com.ecomarket.logistica.model.RutaEntrega.class))).thenAnswer(i -> i.getArgument(0));

        com.ecomarket.logistica.dto.RutaEntregaDTO dto = new com.ecomarket.logistica.dto.RutaEntregaDTO();
        dto.setEstado(com.ecomarket.logistica.model.enums.EstadoRuta.EN_CURSO);

        com.ecomarket.logistica.model.RutaEntrega res = logisticaService.actualizarRuta(1L, dto);
        assertEquals(com.ecomarket.logistica.model.enums.EstadoRuta.EN_CURSO, res.getEstado());
    }

    @Test
    void eliminarRuta_Exito() {
        com.ecomarket.logistica.model.RutaEntrega r = new com.ecomarket.logistica.model.RutaEntrega();
        when(rutaEntregaRepository.findById(1L)).thenReturn(Optional.of(r));
        logisticaService.eliminarRuta(1L);
        verify(rutaEntregaRepository, times(1)).delete(r);
    }

    @Test
    void cambiarEstadoRuta_Exito() {
        com.ecomarket.logistica.model.RutaEntrega r = new com.ecomarket.logistica.model.RutaEntrega();
        when(rutaEntregaRepository.findById(1L)).thenReturn(Optional.of(r));
        when(rutaEntregaRepository.save(any(com.ecomarket.logistica.model.RutaEntrega.class))).thenAnswer(i -> i.getArgument(0));

        com.ecomarket.logistica.model.RutaEntrega res = logisticaService.cambiarEstadoRuta(1L, com.ecomarket.logistica.model.enums.EstadoRuta.FINALIZADA);
        assertEquals(com.ecomarket.logistica.model.enums.EstadoRuta.FINALIZADA, res.getEstado());
    }

    @Test
    void crearEnvio_Excepciones() {
        com.ecomarket.logistica.dto.EnvioDTO dto1 = new com.ecomarket.logistica.dto.EnvioDTO(); // sin idPedido
        assertThrows(IllegalArgumentException.class, () -> logisticaService.crearEnvio(dto1));

        com.ecomarket.logistica.dto.EnvioDTO dto2 = new com.ecomarket.logistica.dto.EnvioDTO();
        dto2.setIdPedido(1L); // sin origen
        assertThrows(IllegalArgumentException.class, () -> logisticaService.crearEnvio(dto2));

        com.ecomarket.logistica.dto.EnvioDTO dto3 = new com.ecomarket.logistica.dto.EnvioDTO();
        dto3.setIdPedido(1L);
        dto3.setOrigen("O"); // sin destino
        assertThrows(IllegalArgumentException.class, () -> logisticaService.crearEnvio(dto3));
    }

    @Test
    void cambiarEstadoEnvio_Entregado_Excepcion() {
        Envio envio = new Envio();
        envio.setEstado(EstadoEnvio.ENTREGADO);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        com.ecomarket.logistica.dto.CambioEstadoRequestDTO req = new com.ecomarket.logistica.dto.CambioEstadoRequestDTO();
        assertThrows(com.ecomarket.logistica.exception.ConflictoNegocioException.class, () -> logisticaService.cambiarEstadoEnvio(1L, req));
    }

    @Test
    void registrarIncidencia_Entregado_Excepcion() {
        Envio envio = new Envio();
        envio.setEstado(EstadoEnvio.ENTREGADO);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        com.ecomarket.logistica.dto.IncidenciaRequestDTO req = new com.ecomarket.logistica.dto.IncidenciaRequestDTO();
        assertThrows(com.ecomarket.logistica.exception.ConflictoNegocioException.class, () -> logisticaService.registrarIncidencia(1L, req));
    }

    @Test
    void activarProveedor_Exito() {
        Proveedor p = new Proveedor();
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        logisticaService.activarProveedor(1L);
        assertTrue(p.getActivo());
    }

    @Test
    void obtenerProveedoresActivos_Exito() {
        when(proveedorRepository.findByActivoTrue()).thenReturn(List.of(new Proveedor()));
        assertEquals(1, logisticaService.obtenerProveedoresActivos().size());
    }

    @Test
    void buscarProveedores_Exito() {
        when(proveedorRepository.findByTipoProveedorAndCobertura("A", "B")).thenReturn(List.of(new Proveedor()));
        assertEquals(1, logisticaService.buscarProveedores("A", "B").size());
    }

    @Test
    void cambiarEstadoRuta_Finalizada_Excepcion() {
        com.ecomarket.logistica.model.RutaEntrega r = new com.ecomarket.logistica.model.RutaEntrega();
        r.setEstado(com.ecomarket.logistica.model.enums.EstadoRuta.FINALIZADA);
        when(rutaEntregaRepository.findById(1L)).thenReturn(Optional.of(r));
        assertThrows(com.ecomarket.logistica.exception.ConflictoNegocioException.class, () -> logisticaService.cambiarEstadoRuta(1L, com.ecomarket.logistica.model.enums.EstadoRuta.EN_CURSO));
    }

    @Test
    void obtenerSeguimiento_Exito() {
        Envio e = new Envio();
        e.setId(1L);
        when(envioRepository.findById(1L)).thenReturn(Optional.of(e));
        
        SeguimientoEnvio se = new SeguimientoEnvio();
        when(seguimientoEnvioRepository.findByEnvioIdOrderByFechaRegistroDesc(1L)).thenReturn(List.of(se));
        
        List<SeguimientoEnvio> lista = logisticaService.obtenerSeguimiento(1L);
        assertEquals(1, lista.size());
    }

    @Test
    void toResponse_Envio_ConProveedor_Y_Ruta() {
        com.ecomarket.logistica.model.RutaEntrega ruta = new com.ecomarket.logistica.model.RutaEntrega();
        ruta.setId(5L);
        Proveedor prov = new Proveedor();
        prov.setId(3L);

        Envio e = new Envio();
        e.setId(1L);
        e.setIdPedido(100L);
        e.setOrigen("A");
        e.setDestino("B");
        e.setEstado(EstadoEnvio.EN_CAMINO);
        e.setProveedor(prov);
        e.setRutaEntrega(ruta);

        com.ecomarket.logistica.dto.EnvioResponse r = logisticaService.toResponse(e);
        assertNotNull(r);
        assertEquals(1L, r.getId());
        assertEquals(3L, r.getProveedorId());
        assertEquals(5L, r.getRutaEntregaId());
    }

    @Test
    void toResponse_Envio_Null() {
        assertNull(logisticaService.toResponse((Envio) null));
    }

    @Test
    void toResponse_Proveedor_Completo() {
        Proveedor p = new Proveedor();
        p.setId(1L);
        p.setRazonSocial("EcoLogistica");
        p.setRut("12345678-9");
        p.setContacto("Juan");
        p.setEmail("juan@eco.cl");
        p.setTelefono("999999999");
        p.setTipoProveedor("TRANSPORTE");
        p.setCobertura("NACIONAL");
        p.setActivo(true);

        com.ecomarket.logistica.dto.ProveedorResponse r = logisticaService.toResponse(p);
        assertNotNull(r);
        assertEquals("EcoLogistica", r.getRazonSocial());
        assertTrue(r.getActivo());
    }

    @Test
    void toResponse_Proveedor_Null() {
        assertNull(logisticaService.toResponse((Proveedor) null));
    }

    @Test
    void toResponse_RutaEntrega_Completa() {
        com.ecomarket.logistica.model.RutaEntrega ruta = new com.ecomarket.logistica.model.RutaEntrega();
        ruta.setId(1L);
        ruta.setEstado(com.ecomarket.logistica.model.enums.EstadoRuta.EN_CURSO);

        com.ecomarket.logistica.dto.RutaEntregaResponse r = logisticaService.toResponse(ruta);
        assertNotNull(r);
        assertEquals(1L, r.getId());
        assertEquals(com.ecomarket.logistica.model.enums.EstadoRuta.EN_CURSO, r.getEstado());
    }

    @Test
    void toResponse_RutaEntrega_Null() {
        assertNull(logisticaService.toResponse((com.ecomarket.logistica.model.RutaEntrega) null));
    }

    @Test
    void toResponse_Seguimiento_Completo() {
        Envio e = new Envio();
        e.setId(10L);

        SeguimientoEnvio s = new SeguimientoEnvio();
        s.setId(1L);
        s.setEnvio(e);
        s.setEstado(EstadoEnvio.EN_CAMINO);
        s.setUbicacion("Bodega Central");
        s.setObservacion("En ruta");
        s.setActualizadoPor("conductor1");

        com.ecomarket.logistica.dto.SeguimientoEnvioResponse r = logisticaService.toResponse(s);
        assertNotNull(r);
        assertEquals(1L, r.getId());
        assertEquals(10L, r.getIdEnvio());
        assertEquals("Bodega Central", r.getUbicacion());
    }

    @Test
    void toResponse_Seguimiento_Null() {
        assertNull(logisticaService.toResponse((SeguimientoEnvio) null));
    }

    @Test
    void crearRuta_SinEstado_UsaPlanificada() {
        com.ecomarket.logistica.dto.RutaEntregaDTO dto = new com.ecomarket.logistica.dto.RutaEntregaDTO();
        // sin estado → debe usar PLANIFICADA
        when(rutaEntregaRepository.save(any(com.ecomarket.logistica.model.RutaEntrega.class))).thenAnswer(i -> i.getArgument(0));
        com.ecomarket.logistica.model.RutaEntrega r = logisticaService.crearRuta(dto);
        assertEquals(com.ecomarket.logistica.model.enums.EstadoRuta.PLANIFICADA, r.getEstado());
    }

    @Test
    void actualizarRuta_SinEstado_NoModifica() {
        com.ecomarket.logistica.model.RutaEntrega ruta = new com.ecomarket.logistica.model.RutaEntrega();
        ruta.setEstado(com.ecomarket.logistica.model.enums.EstadoRuta.PLANIFICADA);
        when(rutaEntregaRepository.findById(1L)).thenReturn(Optional.of(ruta));
        when(rutaEntregaRepository.save(any(com.ecomarket.logistica.model.RutaEntrega.class))).thenAnswer(i -> i.getArgument(0));
        com.ecomarket.logistica.dto.RutaEntregaDTO dto = new com.ecomarket.logistica.dto.RutaEntregaDTO();
        // sin estado
        com.ecomarket.logistica.model.RutaEntrega r = logisticaService.actualizarRuta(1L, dto);
        assertEquals(com.ecomarket.logistica.model.enums.EstadoRuta.PLANIFICADA, r.getEstado());
    }

    // Cubre ramas false de los 5 null checks en actualizarProveedor
    @Test
    void actualizarProveedor_ConCamposNulos_NoModifica() {
        Proveedor p = new Proveedor();
        p.setRazonSocial("RazonOriginal");
        p.setContacto("ContactoOriginal");
        p.setEmail("original@eco.cl");
        p.setTelefono("111111111");
        p.setCobertura("NACIONAL");
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(i -> i.getArgument(0));

        com.ecomarket.logistica.dto.ProveedorDTO dto = new com.ecomarket.logistica.dto.ProveedorDTO();
        // sin setear nada → todos los if(dto.getX() != null) evalúan false

        Proveedor res = logisticaService.actualizarProveedor(1L, dto);
        assertEquals("RazonOriginal", res.getRazonSocial());
        assertEquals("ContactoOriginal", res.getContacto());
        assertEquals("original@eco.cl", res.getEmail());
        assertEquals("111111111", res.getTelefono());
        assertEquals("NACIONAL", res.getCobertura());
    }

    // Cubre ternarios en toResponse(Envio) cuando proveedor=null y ruta=null
    @Test
    void toResponse_Envio_SinProveedor_SinRuta() {
        Envio e = new Envio();
        e.setId(1L);
        e.setIdPedido(100L);
        e.setOrigen("A");
        e.setDestino("B");
        e.setEstado(EstadoEnvio.PREPARADO);
        // proveedor y ruta quedan null

        com.ecomarket.logistica.dto.EnvioResponse r = logisticaService.toResponse(e);
        assertNotNull(r);
        assertEquals(1L, r.getId());
        assertNull(r.getProveedorId());    // cubre rama: proveedor == null → null
        assertNull(r.getRutaEntregaId()); // cubre rama: rutaEntrega == null → null
    }

    // Cubre el ternario en toResponse(SeguimientoEnvio) cuando envio=null
    @Test
    void toResponse_Seguimiento_SinEnvio() {
        SeguimientoEnvio s = new SeguimientoEnvio();
        s.setId(5L);
        s.setEstado(EstadoEnvio.PREPARADO);
        s.setActualizadoPor("sistema");
        // envio queda null

        com.ecomarket.logistica.dto.SeguimientoEnvioResponse r = logisticaService.toResponse(s);
        assertNotNull(r);
        assertEquals(5L, r.getId());
        assertNull(r.getIdEnvio()); // cubre rama: envio == null → null
    }
}


