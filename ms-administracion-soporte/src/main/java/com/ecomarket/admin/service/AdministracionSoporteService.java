package com.ecomarket.admin.service;

import com.ecomarket.admin.dto.*;
import com.ecomarket.admin.entity.*;
import com.ecomarket.admin.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministracionSoporteService {

    private final TiendaRepository tiendaRepository;
    private final AsignacionPersonalRepository asignacionPersonalRepository;
    private final TicketSoporteRepository ticketSoporteRepository;
    private final RespuestaSoporteRepository respuestaSoporteRepository;
    private final AlertaSistemaRepository alertaSistemaRepository;
    private final MetricaSistemaRepository metricaSistemaRepository;
    private final RespaldoDatosRepository respaldoDatosRepository;

    public TiendaResponseDTO crearTienda(TiendaRequestDTO request) {
        validarHorarios(request.getHorarioApertura(), request.getHorarioCierre());

        Tienda tienda = Tienda.builder()
                .nombre(request.getNombre().trim())
                .ciudad(request.getCiudad().trim())
                .horarioApertura(request.getHorarioApertura())
                .horarioCierre(request.getHorarioCierre())
                .politicasLocales(normalizarTextoOpcional(request.getPoliticasLocales()))
                .activa(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return mapearTienda(tiendaRepository.save(tienda));
    }

    public TiendaResponseDTO actualizarTienda(Long idTienda, TiendaRequestDTO request) {
        validarHorarios(request.getHorarioApertura(), request.getHorarioCierre());

        Tienda tienda = obtenerTiendaPorId(idTienda);
        tienda.setNombre(request.getNombre().trim());
        tienda.setCiudad(request.getCiudad().trim());
        tienda.setHorarioApertura(request.getHorarioApertura());
        tienda.setHorarioCierre(request.getHorarioCierre());
        tienda.setPoliticasLocales(normalizarTextoOpcional(request.getPoliticasLocales()));
        tienda.setFechaActualizacion(LocalDateTime.now());

        return mapearTienda(tiendaRepository.save(tienda));
    }

    public TiendaResponseDTO consultarTienda(Long idTienda) {
        return mapearTienda(obtenerTiendaPorId(idTienda));
    }

    public List<TiendaResponseDTO> listarTiendas() {
        return tiendaRepository.findAll().stream()
                .map(this::mapearTienda)
                .toList();
    }

    public AsignacionPersonalResponseDTO asignarPersonal(AsignacionPersonalRequestDTO request) {
        obtenerTiendaPorId(request.getIdTienda());

        AsignacionPersonal asignacion = AsignacionPersonal.builder()
                .idUsuarioInterno(request.getIdUsuarioInterno())
                .idTienda(request.getIdTienda())
                .cargo(request.getCargo().trim())
                .activa(true)
                .fechaAsignacion(LocalDateTime.now())
                .build();

        return mapearAsignacion(asignacionPersonalRepository.save(asignacion));
    }

    public List<AsignacionPersonalResponseDTO> listarPersonalPorTienda(Long idTienda) {
        obtenerTiendaPorId(idTienda);

        return asignacionPersonalRepository.findByIdTiendaAndActivaTrue(idTienda).stream()
                .map(this::mapearAsignacion)
                .toList();
    }

    public TicketSoporteResponseDTO crearTicketSoporte(TicketSoporteRequestDTO request) {
        TicketSoporte ticket = TicketSoporte.builder()
                .asunto(request.getAsunto().trim())
                .descripcion(request.getDescripcion().trim())
                .nombreContacto(request.getNombreContacto().trim())
                .correoContacto(request.getCorreoContacto().trim().toLowerCase())
                .prioridad(request.getPrioridad())
                .estado(EstadoTicket.ABIERTO)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return mapearTicket(ticketSoporteRepository.save(ticket));
    }

    public TicketSoporteResponseDTO consultarTicket(Long idTicket) {
        return mapearTicket(obtenerTicketPorId(idTicket));
    }

    public List<TicketSoporteResponseDTO> listarTickets() {
        return ticketSoporteRepository.findAll().stream()
                .map(this::mapearTicket)
                .toList();
    }

    public TicketSoporteResponseDTO actualizarEstadoTicket(Long idTicket, EstadoTicket estado) {
        TicketSoporte ticket = obtenerTicketPorId(idTicket);
        ticket.setEstado(estado);
        ticket.setFechaActualizacion(LocalDateTime.now());

        return mapearTicket(ticketSoporteRepository.save(ticket));
    }

    public RespuestaSoporteResponseDTO responderTicket(RespuestaSoporteRequestDTO request) {
        TicketSoporte ticket = obtenerTicketPorId(request.getIdTicket());

        RespuestaSoporte respuesta = RespuestaSoporte.builder()
                .idTicket(request.getIdTicket())
                .mensaje(request.getMensaje().trim())
                .respondidoPor(request.getRespondidoPor().trim())
                .fechaRespuesta(LocalDateTime.now())
                .build();

        if (ticket.getEstado() == EstadoTicket.ABIERTO) {
            ticket.setEstado(EstadoTicket.EN_ATENCION);
            ticket.setFechaActualizacion(LocalDateTime.now());
            ticketSoporteRepository.save(ticket);
        }

        return mapearRespuesta(respuestaSoporteRepository.save(respuesta));
    }

    public List<RespuestaSoporteResponseDTO> listarRespuestasTicket(Long idTicket) {
        obtenerTicketPorId(idTicket);

        return respuestaSoporteRepository.findByIdTicket(idTicket).stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    public MetricaSistemaResponseDTO registrarMetrica(MetricaSistemaRequestDTO request) {
        MetricaSistema metrica = MetricaSistema.builder()
                .microservicio(request.getMicroservicio().trim())
                .disponible(request.getDisponible())
                .tiempoRespuestaMs(request.getTiempoRespuestaMs())
                .erroresDetectados(request.getErroresDetectados())
                .fechaRegistro(LocalDateTime.now())
                .build();

        MetricaSistema metricaGuardada = metricaSistemaRepository.save(metrica);

        if (Boolean.FALSE.equals(request.getDisponible()) || request.getErroresDetectados() > 0) {
            AlertaSistema alerta = AlertaSistema.builder()
                    .microservicio(request.getMicroservicio().trim())
                    .tipoAlerta(Boolean.FALSE.equals(request.getDisponible()) ? "MICROSERVICIO_NO_DISPONIBLE" : "ERRORES_DETECTADOS")
                    .descripcion("Se detectó una condición de alerta en el microservicio " + request.getMicroservicio().trim())
                    .resuelta(false)
                    .fechaGeneracion(LocalDateTime.now())
                    .build();

            alertaSistemaRepository.save(alerta);
        }

        return mapearMetrica(metricaGuardada);
    }

    public List<MetricaSistemaResponseDTO> listarMetricas() {
        return metricaSistemaRepository.findAll().stream()
                .map(this::mapearMetrica)
                .toList();
    }

    public List<AlertaSistemaResponseDTO> listarAlertasActivas() {
        return alertaSistemaRepository.findByResueltaFalse().stream()
                .map(this::mapearAlerta)
                .toList();
    }

    public AlertaSistemaResponseDTO registrarAlerta(AlertaSistemaRequestDTO request) {
        AlertaSistema alerta = AlertaSistema.builder()
                .microservicio(request.getMicroservicio().trim())
                .tipoAlerta(request.getTipoAlerta().trim())
                .descripcion(request.getDescripcion().trim())
                .resuelta(false)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        return mapearAlerta(alertaSistemaRepository.save(alerta));
    }

    public AlertaSistemaResponseDTO resolverAlerta(Long idAlerta) {
        AlertaSistema alerta = alertaSistemaRepository.findById(idAlerta)
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada con id: " + idAlerta));

        alerta.setResuelta(true);
        alerta.setFechaResolucion(LocalDateTime.now());

        return mapearAlerta(alertaSistemaRepository.save(alerta));
    }

    public RespaldoDatosResponseDTO programarRespaldo(RespaldoDatosRequestDTO request) {
        RespaldoDatos respaldo = RespaldoDatos.builder()
                .origenDatos(request.getOrigenDatos().trim())
                .frecuencia(request.getFrecuencia().trim())
                .responsable(request.getResponsable().trim())
                .estado("PROGRAMADO")
                .resultado("Respaldo programado correctamente")
                .fechaProgramada(request.getFechaProgramada())
                .build();

        return mapearRespaldo(respaldoDatosRepository.save(respaldo));
    }

    public RespaldoDatosResponseDTO ejecutarRespaldo(Long idRespaldo) {
        RespaldoDatos respaldo = obtenerRespaldoPorId(idRespaldo);

        respaldo.setEstado("EJECUTADO");
        respaldo.setResultado("Respaldo ejecutado correctamente");
        respaldo.setFechaEjecucion(LocalDateTime.now());

        return mapearRespaldo(respaldoDatosRepository.save(respaldo));
    }

    public RespaldoDatosResponseDTO restaurarRespaldo(Long idRespaldo) {
        RespaldoDatos respaldo = obtenerRespaldoPorId(idRespaldo);

        if (!"EJECUTADO".equalsIgnoreCase(respaldo.getEstado())) {
            throw new IllegalArgumentException("Solo se puede restaurar un respaldo ejecutado");
        }

        respaldo.setEstado("RESTAURADO");
        respaldo.setResultado("Restauración ejecutada correctamente");
        respaldo.setFechaRestauracion(LocalDateTime.now());

        return mapearRespaldo(respaldoDatosRepository.save(respaldo));
    }

    public List<RespaldoDatosResponseDTO> listarRespaldos() {
        return respaldoDatosRepository.findAll().stream()
                .map(this::mapearRespaldo)
                .toList();
    }

    private Tienda obtenerTiendaPorId(Long idTienda) {
        return tiendaRepository.findById(idTienda)
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada con id: " + idTienda));
    }

    private TicketSoporte obtenerTicketPorId(Long idTicket) {
        return ticketSoporteRepository.findById(idTicket)
                .orElseThrow(() -> new IllegalArgumentException("Ticket de soporte no encontrado con id: " + idTicket));
    }

    private RespaldoDatos obtenerRespaldoPorId(Long idRespaldo) {
        return respaldoDatosRepository.findById(idRespaldo)
                .orElseThrow(() -> new IllegalArgumentException("Respaldo no encontrado con id: " + idRespaldo));
    }

    private void validarHorarios(java.time.LocalTime apertura, java.time.LocalTime cierre) {
        if (!apertura.isBefore(cierre)) {
            throw new IllegalArgumentException("El horario de apertura debe ser anterior al horario de cierre");
        }
    }

    private String normalizarTextoOpcional(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    private TiendaResponseDTO mapearTienda(Tienda tienda) {
        return TiendaResponseDTO.builder()
                .idTienda(tienda.getIdTienda())
                .nombre(tienda.getNombre())
                .ciudad(tienda.getCiudad())
                .horarioApertura(tienda.getHorarioApertura())
                .horarioCierre(tienda.getHorarioCierre())
                .politicasLocales(tienda.getPoliticasLocales())
                .activa(tienda.getActiva())
                .fechaCreacion(tienda.getFechaCreacion())
                .fechaActualizacion(tienda.getFechaActualizacion())
                .build();
    }

    private AsignacionPersonalResponseDTO mapearAsignacion(AsignacionPersonal asignacion) {
        return AsignacionPersonalResponseDTO.builder()
                .idAsignacion(asignacion.getIdAsignacion())
                .idUsuarioInterno(asignacion.getIdUsuarioInterno())
                .idTienda(asignacion.getIdTienda())
                .cargo(asignacion.getCargo())
                .activa(asignacion.getActiva())
                .fechaAsignacion(asignacion.getFechaAsignacion())
                .build();
    }

    private TicketSoporteResponseDTO mapearTicket(TicketSoporte ticket) {
        return TicketSoporteResponseDTO.builder()
                .idTicket(ticket.getIdTicket())
                .asunto(ticket.getAsunto())
                .descripcion(ticket.getDescripcion())
                .nombreContacto(ticket.getNombreContacto())
                .correoContacto(ticket.getCorreoContacto())
                .prioridad(ticket.getPrioridad())
                .estado(ticket.getEstado())
                .fechaCreacion(ticket.getFechaCreacion())
                .fechaActualizacion(ticket.getFechaActualizacion())
                .build();
    }

    private RespuestaSoporteResponseDTO mapearRespuesta(RespuestaSoporte respuesta) {
        return RespuestaSoporteResponseDTO.builder()
                .idRespuesta(respuesta.getIdRespuesta())
                .idTicket(respuesta.getIdTicket())
                .mensaje(respuesta.getMensaje())
                .respondidoPor(respuesta.getRespondidoPor())
                .fechaRespuesta(respuesta.getFechaRespuesta())
                .build();
    }

    private MetricaSistemaResponseDTO mapearMetrica(MetricaSistema metrica) {
        return MetricaSistemaResponseDTO.builder()
                .idMetrica(metrica.getIdMetrica())
                .microservicio(metrica.getMicroservicio())
                .disponible(metrica.getDisponible())
                .tiempoRespuestaMs(metrica.getTiempoRespuestaMs())
                .erroresDetectados(metrica.getErroresDetectados())
                .fechaRegistro(metrica.getFechaRegistro())
                .build();
    }

    private AlertaSistemaResponseDTO mapearAlerta(AlertaSistema alerta) {
        return AlertaSistemaResponseDTO.builder()
                .idAlerta(alerta.getIdAlerta())
                .microservicio(alerta.getMicroservicio())
                .tipoAlerta(alerta.getTipoAlerta())
                .descripcion(alerta.getDescripcion())
                .resuelta(alerta.getResuelta())
                .fechaGeneracion(alerta.getFechaGeneracion())
                .fechaResolucion(alerta.getFechaResolucion())
                .build();
    }

    private RespaldoDatosResponseDTO mapearRespaldo(RespaldoDatos respaldo) {
        return RespaldoDatosResponseDTO.builder()
                .idRespaldo(respaldo.getIdRespaldo())
                .origenDatos(respaldo.getOrigenDatos())
                .frecuencia(respaldo.getFrecuencia())
                .responsable(respaldo.getResponsable())
                .estado(respaldo.getEstado())
                .resultado(respaldo.getResultado())
                .fechaProgramada(respaldo.getFechaProgramada())
                .fechaEjecucion(respaldo.getFechaEjecucion())
                .fechaRestauracion(respaldo.getFechaRestauracion())
                .build();
    }
}
