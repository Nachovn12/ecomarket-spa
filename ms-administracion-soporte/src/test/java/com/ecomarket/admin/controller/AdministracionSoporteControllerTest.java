package com.ecomarket.admin.controller;

import com.ecomarket.admin.dto.*;
import com.ecomarket.admin.model.*;
import com.ecomarket.admin.service.AdministracionSoporteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdministracionSoporteController.class)
class AdministracionSoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdministracionSoporteService administracionSoporteService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("AC-2: POST /api/admin/tiendas retorna 201")
    void crearTienda_retorna201() throws Exception {
        TiendaRequestDTO req = new TiendaRequestDTO();
        req.setNombre("Tienda Santiago");
        req.setCiudad("Santiago");
        req.setHorarioApertura(LocalTime.of(9, 0));
        req.setHorarioCierre(LocalTime.of(21, 0));

        TiendaResponseDTO resp = TiendaResponseDTO.builder()
                .idTienda(1L)
                .nombre("Tienda Santiago")
                .ciudad("Santiago")
                .activa(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(administracionSoporteService.crearTienda(any())).thenReturn(resp);

        mockMvc.perform(post("/api/admin/tiendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTienda").value(1))
                .andExpect(jsonPath("$.nombre").value("Tienda Santiago"))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    @DisplayName("AC-2: GET /api/admin/tiendas retorna 200 con lista")
    void listarTiendas_retorna200() throws Exception {
        TiendaResponseDTO resp = TiendaResponseDTO.builder()
                .idTienda(1L)
                .nombre("Tienda Valdivia")
                .ciudad("Valdivia")
                .activa(true)
                .build();

        when(administracionSoporteService.listarTiendas()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/admin/tiendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTienda").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Tienda Valdivia"))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    @DisplayName("AC-2: GET /api/admin/tiendas/{id} retorna 200")
    void consultarTienda_retorna200() throws Exception {
        TiendaResponseDTO resp = TiendaResponseDTO.builder()
                .idTienda(1L)
                .nombre("Tienda Antofagasta")
                .ciudad("Antofagasta")
                .activa(true)
                .build();

        when(administracionSoporteService.consultarTienda(1L)).thenReturn(resp);

        mockMvc.perform(get("/api/admin/tiendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTienda").value(1))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    @DisplayName("AC-2: POST /api/soporte/tickets retorna 201")
    void crearTicket_retorna201() throws Exception {
        TicketSoporteRequestDTO req = new TicketSoporteRequestDTO();
        req.setAsunto("Problema con envio");
        req.setDescripcion("No llega el pedido");
        req.setNombreContacto("Juan Perez");
        req.setCorreoContacto("juan@correo.cl");
        req.setPrioridad(PrioridadTicket.ALTA);

        TicketSoporteResponseDTO resp = TicketSoporteResponseDTO.builder()
                .idTicket(1L)
                .asunto("Problema con envio")
                .estado(EstadoTicket.ABIERTO)
                .prioridad(PrioridadTicket.ALTA)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(administracionSoporteService.crearTicketSoporte(any())).thenReturn(resp);

        mockMvc.perform(post("/api/soporte/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTicket").value(1))
                .andExpect(jsonPath("$.estado").value("ABIERTO"))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    @DisplayName("AC-2: GET /api/soporte/tickets retorna 200 con lista")
    void listarTickets_retorna200() throws Exception {
        TicketSoporteResponseDTO resp = TicketSoporteResponseDTO.builder()
                .idTicket(1L)
                .asunto("Consulta factura")
                .estado(EstadoTicket.ABIERTO)
                .prioridad(PrioridadTicket.MEDIA)
                .build();

        when(administracionSoporteService.listarTickets()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/soporte/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTicket").value(1))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    @DisplayName("AC-2: POST /api/admin/alertas retorna 201")
    void generarAlerta_retorna201() throws Exception {
        AlertaSistemaRequestDTO req = new AlertaSistemaRequestDTO();
        req.setMicroservicio("ms-pedidos");
        req.setTipoAlerta("STOCK_CRITICO");
        req.setDescripcion("Stock bajo minimo");

        AlertaSistemaResponseDTO resp = AlertaSistemaResponseDTO.builder()
                .idAlerta(1L)
                .microservicio("ms-pedidos")
                .tipoAlerta("STOCK_CRITICO")
                .resuelta(false)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        when(administracionSoporteService.registrarAlerta(any())).thenReturn(resp);

        mockMvc.perform(post("/api/admin/alertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAlerta").value(1))
                .andExpect(jsonPath("$.resuelta").value(false))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    @DisplayName("AC-2: GET /api/admin/alertas/activas retorna 200")
    void listarAlertasActivas_retorna200() throws Exception {
        AlertaSistemaResponseDTO resp = AlertaSistemaResponseDTO.builder()
                .idAlerta(1L)
                .microservicio("ms-catalogo")
                .tipoAlerta("MICROSERVICIO_NO_DISPONIBLE")
                .resuelta(false)
                .build();

        when(administracionSoporteService.listarAlertasActivas()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/admin/alertas/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resuelta").value(false))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    @DisplayName("AC-2: POST /api/admin/respaldos retorna 201")
    void programarRespaldo_retorna201() throws Exception {
        RespaldoDatosRequestDTO req = new RespaldoDatosRequestDTO();
        req.setOrigenDatos("bd_ventas");
        req.setFrecuencia("DIARIA");
        req.setResponsable("admin1");
        req.setFechaProgramada(LocalDateTime.now().plusDays(1));

        RespaldoDatosResponseDTO resp = RespaldoDatosResponseDTO.builder()
                .idRespaldo(1L)
                .origenDatos("bd_ventas")
                .estado("PROGRAMADO")
                .build();

        when(administracionSoporteService.programarRespaldo(any())).thenReturn(resp);

        mockMvc.perform(post("/api/admin/respaldos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PROGRAMADO"))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    @DisplayName("AC-2: GET /api/admin/respaldos retorna 200")
    void listarRespaldos_retorna200() throws Exception {
        RespaldoDatosResponseDTO resp = RespaldoDatosResponseDTO.builder()
                .idRespaldo(1L)
                .origenDatos("bd_admin")
                .estado("EJECUTADO")
                .build();

        when(administracionSoporteService.listarRespaldos()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/admin/respaldos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("EJECUTADO"))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    @DisplayName("AC-2: PUT /api/admin/tiendas/{id} retorna 200")
    void actualizarTienda_retorna200() throws Exception {
        TiendaRequestDTO req = new TiendaRequestDTO();
        req.setNombre("Tienda Actualizada");
        req.setCiudad("Santiago");
        req.setHorarioApertura(LocalTime.of(9, 0));
        req.setHorarioCierre(LocalTime.of(21, 0));

        TiendaResponseDTO resp = TiendaResponseDTO.builder()
                .idTienda(1L)
                .nombre("Tienda Actualizada")
                .activa(true)
                .build();

        when(administracionSoporteService.actualizarTienda(any(), any())).thenReturn(resp);

        mockMvc.perform(put("/api/admin/tiendas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tienda Actualizada"));
    }

    @Test
    @DisplayName("AC-2: DELETE /api/admin/tiendas/{id} retorna 204")
    void eliminarTienda_retorna204() throws Exception {
        mockMvc.perform(delete("/api/admin/tiendas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("AC-2: POST /api/admin/tiendas/{id}/personal retorna 201")
    void asignarPersonal_retorna201() throws Exception {
        AsignacionPersonalRequestDTO req = new AsignacionPersonalRequestDTO();
        req.setIdUsuarioInterno(10L);
        req.setIdTienda(1L);
        req.setCargo("Cajero");

        AsignacionPersonalResponseDTO resp = AsignacionPersonalResponseDTO.builder()
                .idAsignacion(1L)
                .idTienda(1L)
                .idUsuarioInterno(10L)
                .cargo("Cajero")
                .build();

        when(administracionSoporteService.asignarPersonal(any())).thenReturn(resp);

        mockMvc.perform(post("/api/admin/tiendas/1/personal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cargo").value("Cajero"));
    }

    @Test
    @DisplayName("AC-2: GET /api/admin/tiendas/{id}/personal retorna 200")
    void listarPersonal_retorna200() throws Exception {
        AsignacionPersonalResponseDTO resp = AsignacionPersonalResponseDTO.builder()
                .idAsignacion(1L)
                .cargo("Gerente")
                .build();

        when(administracionSoporteService.listarPersonalPorTienda(1L)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/admin/tiendas/1/personal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cargo").value("Gerente"));
    }

    @Test
    @DisplayName("AC-2: GET /api/soporte/tickets/{id} retorna 200")
    void consultarTicket_retorna200() throws Exception {
        TicketSoporteResponseDTO resp = TicketSoporteResponseDTO.builder()
                .idTicket(1L)
                .asunto("Asunto Test")
                .build();

        when(administracionSoporteService.consultarTicket(1L)).thenReturn(resp);

        mockMvc.perform(get("/api/soporte/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asunto").value("Asunto Test"));
    }

    @Test
    @DisplayName("AC-2: PATCH /api/soporte/tickets/{id}/estado retorna 200")
    void actualizarEstadoTicket_retorna200() throws Exception {
        TicketSoporteResponseDTO resp = TicketSoporteResponseDTO.builder()
                .idTicket(1L)
                .estado(EstadoTicket.CERRADO)
                .build();

        when(administracionSoporteService.actualizarEstadoTicket(any(), any())).thenReturn(resp);

        mockMvc.perform(patch("/api/soporte/tickets/1/estado")
                        .param("estado", "CERRADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CERRADO"));
    }

    @Test
    @DisplayName("AC-2: DELETE /api/soporte/tickets/{id} retorna 204")
    void eliminarTicket_retorna204() throws Exception {
        mockMvc.perform(delete("/api/soporte/tickets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("AC-2: POST /api/soporte/tickets/{id}/respuestas retorna 201")
    void responderTicket_retorna201() throws Exception {
        RespuestaSoporteRequestDTO req = new RespuestaSoporteRequestDTO();
        req.setIdTicket(1L);
        req.setMensaje("Solucionado");
        req.setRespondidoPor("Admin");

        RespuestaSoporteResponseDTO resp = RespuestaSoporteResponseDTO.builder()
                .idRespuesta(1L)
                .mensaje("Solucionado")
                .build();

        when(administracionSoporteService.responderTicket(any(), any())).thenReturn(resp);

        mockMvc.perform(post("/api/soporte/tickets/1/respuestas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Solucionado"));
    }

    @Test
    @DisplayName("AC-2: GET /api/soporte/tickets/{id}/respuestas retorna 200")
    void listarRespuestasTicket_retorna200() throws Exception {
        RespuestaSoporteResponseDTO resp = RespuestaSoporteResponseDTO.builder()
                .idRespuesta(1L)
                .mensaje("Hola")
                .build();

        when(administracionSoporteService.listarRespuestasTicket(1L)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/soporte/tickets/1/respuestas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mensaje").value("Hola"));
    }

    @Test
    @DisplayName("AC-2: POST /api/admin/metricas retorna 201")
    void registrarMetrica_retorna201() throws Exception {
        MetricaSistemaRequestDTO req = new MetricaSistemaRequestDTO();
        req.setMicroservicio("ms-inventario");
        req.setDisponible(true);
        req.setErroresDetectados(0);
        req.setTiempoRespuestaMs(120L);

        MetricaSistemaResponseDTO resp = MetricaSistemaResponseDTO.builder()
                .idMetrica(1L)
                .microservicio("ms-inventario")
                .build();

        when(administracionSoporteService.registrarMetrica(any())).thenReturn(resp);

        mockMvc.perform(post("/api/admin/metricas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.microservicio").value("ms-inventario"));
    }

    @Test
    @DisplayName("AC-2: GET /api/admin/metricas retorna 200")
    void listarMetricas_retorna200() throws Exception {
        MetricaSistemaResponseDTO resp = MetricaSistemaResponseDTO.builder()
                .idMetrica(1L)
                .build();

        when(administracionSoporteService.listarMetricas()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/admin/metricas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMetrica").value(1));
    }

    @Test
    @DisplayName("AC-2: PATCH /api/admin/alertas/{id}/resolver retorna 200")
    void resolverAlerta_retorna200() throws Exception {
        AlertaSistemaResponseDTO resp = AlertaSistemaResponseDTO.builder()
                .idAlerta(1L)
                .resuelta(true)
                .build();

        when(administracionSoporteService.resolverAlerta(1L)).thenReturn(resp);

        mockMvc.perform(patch("/api/admin/alertas/1/resolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resuelta").value(true));
    }

    @Test
    @DisplayName("AC-2: PATCH /api/admin/respaldos/{id}/ejecutar retorna 200")
    void ejecutarRespaldo_retorna200() throws Exception {
        RespaldoDatosResponseDTO resp = RespaldoDatosResponseDTO.builder()
                .idRespaldo(1L)
                .estado("EJECUTADO")
                .build();

        when(administracionSoporteService.ejecutarRespaldo(1L)).thenReturn(resp);

        mockMvc.perform(patch("/api/admin/respaldos/1/ejecutar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EJECUTADO"));
    }

    @Test
    @DisplayName("AC-2: PATCH /api/admin/respaldos/{id}/restaurar retorna 200")
    void restaurarRespaldo_retorna200() throws Exception {
        RespaldoDatosResponseDTO resp = RespaldoDatosResponseDTO.builder()
                .idRespaldo(1L)
                .estado("RESTAURADO")
                .build();

        when(administracionSoporteService.restaurarRespaldo(1L)).thenReturn(resp);

        mockMvc.perform(patch("/api/admin/respaldos/1/restaurar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RESTAURADO"));
    }

    @Test
    @DisplayName("AC-2: DELETE /api/admin/respaldos/{id} retorna 204")
    void eliminarRespaldo_retorna204() throws Exception {
        mockMvc.perform(delete("/api/admin/respaldos/1"))
                .andExpect(status().isNoContent());
    }
}
