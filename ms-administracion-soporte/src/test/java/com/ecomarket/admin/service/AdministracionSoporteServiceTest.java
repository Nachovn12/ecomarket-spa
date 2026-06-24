package com.ecomarket.admin.service;

import com.ecomarket.admin.dto.*;
import com.ecomarket.admin.exception.RecursoNoEncontradoException;
import com.ecomarket.admin.model.*;
import com.ecomarket.admin.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdministracionSoporteServiceTest {

    @Mock private TiendaRepository tiendaRepository;
    @Mock private AsignacionPersonalRepository asignacionPersonalRepository;
    @Mock private TicketSoporteRepository ticketSoporteRepository;
    @Mock private RespuestaSoporteRepository respuestaSoporteRepository;
    @Mock private AlertaSistemaRepository alertaSistemaRepository;
    @Mock private MetricaSistemaRepository metricaSistemaRepository;
    @Mock private RespaldoDatosRepository respaldoDatosRepository;
    @Mock private UsuarioInternoClientService usuarioInternoClientService;

    @InjectMocks
    private AdministracionSoporteService service;

    // --- Helpers ---
    private Tienda tiendaEjemplo() {
        return Tienda.builder()
                .idTienda(1L)
                .nombre("Tienda Central")
                .ciudad("Santiago")
                .horarioApertura(LocalTime.of(9, 0))
                .horarioCierre(LocalTime.of(18, 0))
                .activa(true)
                .build();
    }

    private TicketSoporte ticketEjemplo() {
        return TicketSoporte.builder()
                .idTicket(1L)
                .asunto("Problema")
                .descripcion("Falla en login")
                .nombreContacto("Juan")
                .correoContacto("juan@test.com")
                .prioridad(PrioridadTicket.ALTA)
                .estado(EstadoTicket.ABIERTO)
                .build();
    }

    private RespaldoDatos respaldoEjemplo() {
        return RespaldoDatos.builder()
                .idRespaldo(1L)
                .origenDatos("DB")
                .frecuencia("DIARIA")
                .responsable("Admin")
                .estado("PROGRAMADO")
                .build();
    }

    // ==========================================
    // TIENDAS
    // ==========================================

    @Test
    void crearTienda_Exito() {
        TiendaRequestDTO req = new TiendaRequestDTO();
        req.setNombre("Nueva Tienda");
        req.setCiudad("Valpo");
        req.setHorarioApertura(LocalTime.of(8, 0));
        req.setHorarioCierre(LocalTime.of(20, 0));
        req.setPoliticasLocales("  Politica 1  ");

        when(tiendaRepository.save(any(Tienda.class))).thenAnswer(i -> {
            Tienda t = i.getArgument(0);
            t.setIdTienda(1L);
            return t;
        });

        TiendaResponseDTO resp = service.crearTienda(req);
        assertNotNull(resp);
        assertEquals("Nueva Tienda", resp.getNombre());
        assertEquals("Politica 1", resp.getPoliticasLocales());
    }

    @Test
    void crearTienda_Falla_HorariosInvalidos() {
        TiendaRequestDTO req = new TiendaRequestDTO();
        req.setNombre("Nueva Tienda");
        req.setCiudad("Valpo");
        req.setHorarioApertura(LocalTime.of(20, 0));
        req.setHorarioCierre(LocalTime.of(8, 0));

        assertThrows(IllegalArgumentException.class, () -> service.crearTienda(req));
    }

    @Test
    void actualizarTienda_Exito() {
        Tienda t = tiendaEjemplo();
        when(tiendaRepository.findById(1L)).thenReturn(Optional.of(t));
        when(tiendaRepository.save(any(Tienda.class))).thenAnswer(i -> i.getArgument(0));

        TiendaRequestDTO req = new TiendaRequestDTO();
        req.setNombre("Actualizada");
        req.setCiudad("Santiago");
        req.setHorarioApertura(LocalTime.of(9, 0));
        req.setHorarioCierre(LocalTime.of(18, 0));
        req.setPoliticasLocales(""); // blank test

        TiendaResponseDTO resp = service.actualizarTienda(1L, req);
        assertEquals("Actualizada", resp.getNombre());
        assertNull(resp.getPoliticasLocales());
    }

    @Test
    void consultarTienda_Exito() {
        when(tiendaRepository.findById(1L)).thenReturn(Optional.of(tiendaEjemplo()));
        TiendaResponseDTO resp = service.consultarTienda(1L);
        assertEquals("Tienda Central", resp.getNombre());
    }

    @Test
    void consultarTienda_NoEncontrada() {
        when(tiendaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.consultarTienda(99L));
    }

    @Test
    void listarTiendas_Exito() {
        when(tiendaRepository.findAll()).thenReturn(List.of(tiendaEjemplo()));
        List<TiendaResponseDTO> lista = service.listarTiendas();
        assertEquals(1, lista.size());
    }

    @Test
    void eliminarTienda_Exito() {
        when(tiendaRepository.existsById(1L)).thenReturn(true);
        service.eliminarTienda(1L);
        verify(tiendaRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarTienda_NoEncontrada() {
        when(tiendaRepository.existsById(99L)).thenReturn(false);
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminarTienda(99L));
    }

    // ==========================================
    // PERSONAL
    // ==========================================

    @Test
    void asignarPersonal_Exito() {
        when(tiendaRepository.findById(1L)).thenReturn(Optional.of(tiendaEjemplo()));
        doNothing().when(usuarioInternoClientService).validarUsuarioInternoExiste(10L);

        when(asignacionPersonalRepository.save(any(AsignacionPersonal.class))).thenAnswer(i -> {
            AsignacionPersonal a = i.getArgument(0);
            a.setIdAsignacion(1L);
            return a;
        });

        AsignacionPersonalRequestDTO req = new AsignacionPersonalRequestDTO();
        req.setIdTienda(1L);
        req.setIdUsuarioInterno(10L);
        req.setCargo("  Vendedor  ");

        AsignacionPersonalResponseDTO resp = service.asignarPersonal(req);
        assertEquals("Vendedor", resp.getCargo());
        assertEquals(10L, resp.getIdUsuarioInterno());
    }

    @Test
    void listarPersonalPorTienda_Exito() {
        when(tiendaRepository.findById(1L)).thenReturn(Optional.of(tiendaEjemplo()));
        AsignacionPersonal a = AsignacionPersonal.builder().idAsignacion(1L).tienda(tiendaEjemplo()).activa(true).build();
        when(asignacionPersonalRepository.findByTiendaIdTiendaAndActivaTrue(1L)).thenReturn(List.of(a));

        List<AsignacionPersonalResponseDTO> lista = service.listarPersonalPorTienda(1L);
        assertEquals(1, lista.size());
    }

    // ==========================================
    // TICKETS DE SOPORTE
    // ==========================================

    @Test
    void crearTicketSoporte_Exito() {
        TicketSoporteRequestDTO req = new TicketSoporteRequestDTO();
        req.setAsunto("Falla");
        req.setDescripcion("Falla en pagos");
        req.setNombreContacto("Pedro");
        req.setCorreoContacto("PEDRO@TEST.COM");
        req.setPrioridad(PrioridadTicket.MEDIA);

        when(ticketSoporteRepository.save(any())).thenAnswer(i -> {
            TicketSoporte t = i.getArgument(0);
            t.setIdTicket(1L);
            return t;
        });

        TicketSoporteResponseDTO resp = service.crearTicketSoporte(req);
        assertEquals("pedro@test.com", resp.getCorreoContacto()); // lowercase
        assertEquals(EstadoTicket.ABIERTO, resp.getEstado());
    }

    @Test
    void consultarTicket_Exito() {
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticketEjemplo()));
        TicketSoporteResponseDTO resp = service.consultarTicket(1L);
        assertEquals(EstadoTicket.ABIERTO, resp.getEstado());
    }

    @Test
    void listarTickets_Exito() {
        when(ticketSoporteRepository.findAll()).thenReturn(List.of(ticketEjemplo()));
        assertEquals(1, service.listarTickets().size());
    }

    @Test
    void actualizarEstadoTicket_Exito() {
        TicketSoporte t = ticketEjemplo();
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(t));
        when(ticketSoporteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TicketSoporteResponseDTO resp = service.actualizarEstadoTicket(1L, EstadoTicket.RESUELTO);
        assertEquals(EstadoTicket.RESUELTO, resp.getEstado());
    }

    @Test
    void responderTicket_Abierto_CambiaAEnAtencion() {
        TicketSoporte t = ticketEjemplo(); // estado ABIERTO
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(t));
        when(respuestaSoporteRepository.save(any())).thenAnswer(i -> {
            RespuestaSoporte r = i.getArgument(0);
            r.setIdRespuesta(1L);
            return r;
        });

        RespuestaSoporteRequestDTO req = new RespuestaSoporteRequestDTO();
        req.setMensaje("Estamos revisando");
        req.setRespondidoPor("Soporte");

        RespuestaSoporteResponseDTO resp = service.responderTicket(1L, req);
        assertEquals("Estamos revisando", resp.getMensaje());
        verify(ticketSoporteRepository, times(1)).save(t); // se actualizo el ticket a EN_ATENCION
        assertEquals(EstadoTicket.EN_ATENCION, t.getEstado());
    }

    @Test
    void responderTicket_YaEnAtencion_NoCambiaEstado() {
        TicketSoporte t = ticketEjemplo();
        t.setEstado(EstadoTicket.EN_ATENCION); // No entra al if
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(t));
        when(respuestaSoporteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RespuestaSoporteRequestDTO req = new RespuestaSoporteRequestDTO();
        req.setMensaje("Sigue en revision");
        req.setRespondidoPor("Soporte");

        service.responderTicket(1L, req);
        verify(ticketSoporteRepository, times(0)).save(t); // no se actualiza estado
    }

    @Test
    void listarRespuestasTicket_Exito() {
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticketEjemplo()));
        RespuestaSoporte r = RespuestaSoporte.builder().idRespuesta(1L).ticket(ticketEjemplo()).build();
        when(respuestaSoporteRepository.findByTicketIdTicket(1L)).thenReturn(List.of(r));

        assertEquals(1, service.listarRespuestasTicket(1L).size());
    }

    @Test
    void eliminarTicket_Exito() {
        when(ticketSoporteRepository.existsById(1L)).thenReturn(true);
        service.eliminarTicket(1L);
        verify(ticketSoporteRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarTicket_Falla_NoEncontrado() {
        when(ticketSoporteRepository.existsById(99L)).thenReturn(false);
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminarTicket(99L));
    }

    // ==========================================
    // METRICAS Y ALERTAS
    // ==========================================

    @Test
    void registrarMetrica_SinAlerta() {
        MetricaSistemaRequestDTO req = new MetricaSistemaRequestDTO();
        req.setMicroservicio("ms-usuarios");
        req.setDisponible(true);
        req.setTiempoRespuestaMs(150L);
        req.setErroresDetectados(0);

        when(metricaSistemaRepository.save(any())).thenAnswer(i -> {
            MetricaSistema m = i.getArgument(0);
            m.setIdMetrica(1L);
            return m;
        });

        MetricaSistemaResponseDTO resp = service.registrarMetrica(req);
        assertEquals(0, resp.getErroresDetectados());
        verify(alertaSistemaRepository, times(0)).save(any());
    }

    @Test
    void registrarMetrica_ConAlerta_NoDisponible() {
        MetricaSistemaRequestDTO req = new MetricaSistemaRequestDTO();
        req.setMicroservicio("ms-usuarios");
        req.setDisponible(false); // genera alerta MICROSERVICIO_NO_DISPONIBLE
        req.setTiempoRespuestaMs(5000L);
        req.setErroresDetectados(0);

        when(metricaSistemaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.registrarMetrica(req);
        verify(alertaSistemaRepository, times(1)).save(argThat(a -> a.getTipoAlerta().equals("MICROSERVICIO_NO_DISPONIBLE")));
    }

    @Test
    void registrarMetrica_ConAlerta_ConErrores() {
        MetricaSistemaRequestDTO req = new MetricaSistemaRequestDTO();
        req.setMicroservicio("ms-usuarios");
        req.setDisponible(true);
        req.setTiempoRespuestaMs(150L);
        req.setErroresDetectados(5); // genera alerta ERRORES_DETECTADOS

        when(metricaSistemaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.registrarMetrica(req);
        verify(alertaSistemaRepository, times(1)).save(argThat(a -> a.getTipoAlerta().equals("ERRORES_DETECTADOS")));
    }

    @Test
    void listarMetricas_Exito() {
        when(metricaSistemaRepository.findAll()).thenReturn(List.of(new MetricaSistema()));
        assertEquals(1, service.listarMetricas().size());
    }

    @Test
    void listarAlertasActivas_Exito() {
        when(alertaSistemaRepository.findByResueltaFalse()).thenReturn(List.of(new AlertaSistema()));
        assertEquals(1, service.listarAlertasActivas().size());
    }

    @Test
    void registrarAlerta_Exito() {
        AlertaSistemaRequestDTO req = new AlertaSistemaRequestDTO();
        req.setMicroservicio("ms-ventas");
        req.setTipoAlerta("CPU_HIGH");
        req.setDescripcion("Uso de CPU 99%");

        when(alertaSistemaRepository.save(any())).thenAnswer(i -> {
            AlertaSistema a = i.getArgument(0);
            a.setIdAlerta(1L);
            return a;
        });

        AlertaSistemaResponseDTO resp = service.registrarAlerta(req);
        assertEquals("CPU_HIGH", resp.getTipoAlerta());
    }

    @Test
    void resolverAlerta_Exito() {
        AlertaSistema a = AlertaSistema.builder().idAlerta(1L).resuelta(false).build();
        when(alertaSistemaRepository.findById(1L)).thenReturn(Optional.of(a));
        when(alertaSistemaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        AlertaSistemaResponseDTO resp = service.resolverAlerta(1L);
        assertTrue(resp.getResuelta());
    }

    @Test
    void resolverAlerta_NoEncontrada() {
        when(alertaSistemaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.resolverAlerta(99L));
    }

    // ==========================================
    // RESPALDOS
    // ==========================================

    @Test
    void programarRespaldo_Exito() {
        RespaldoDatosRequestDTO req = new RespaldoDatosRequestDTO();
        req.setOrigenDatos("DB");
        req.setFrecuencia("DIARIA");
        req.setResponsable("Admin");
        req.setFechaProgramada(LocalDateTime.now().plusDays(1));

        when(respaldoDatosRepository.save(any())).thenAnswer(i -> {
            RespaldoDatos r = i.getArgument(0);
            r.setIdRespaldo(1L);
            return r;
        });

        RespaldoDatosResponseDTO resp = service.programarRespaldo(req);
        assertEquals("PROGRAMADO", resp.getEstado());
    }

    @Test
    void ejecutarRespaldo_Exito() {
        RespaldoDatos r = respaldoEjemplo(); // estado PROGRAMADO
        when(respaldoDatosRepository.findById(1L)).thenReturn(Optional.of(r));
        when(respaldoDatosRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RespaldoDatosResponseDTO resp = service.ejecutarRespaldo(1L);
        assertEquals("EJECUTADO", resp.getEstado());
        assertNotNull(resp.getFechaEjecucion());
    }

    @Test
    void restaurarRespaldo_Exito() {
        RespaldoDatos r = respaldoEjemplo();
        r.setEstado("EJECUTADO");
        when(respaldoDatosRepository.findById(1L)).thenReturn(Optional.of(r));
        when(respaldoDatosRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RespaldoDatosResponseDTO resp = service.restaurarRespaldo(1L);
        assertEquals("RESTAURADO", resp.getEstado());
    }

    @Test
    void restaurarRespaldo_Falla_EstadoIncorrecto() {
        RespaldoDatos r = respaldoEjemplo();
        r.setEstado("PROGRAMADO"); // No es EJECUTADO
        when(respaldoDatosRepository.findById(1L)).thenReturn(Optional.of(r));

        assertThrows(IllegalArgumentException.class, () -> service.restaurarRespaldo(1L));
    }

    @Test
    void listarRespaldos_Exito() {
        when(respaldoDatosRepository.findAll()).thenReturn(List.of(respaldoEjemplo()));
        assertEquals(1, service.listarRespaldos().size());
    }

    @Test
    void eliminarRespaldo_Exito() {
        when(respaldoDatosRepository.findById(1L)).thenReturn(Optional.of(respaldoEjemplo()));
        service.eliminarRespaldo(1L);
        verify(respaldoDatosRepository, times(1)).delete(any());
    }

    @Test
    void eliminarRespaldo_Falla_NoEncontrado() {
        when(respaldoDatosRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminarRespaldo(99L));
    }
}
