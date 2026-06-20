package com.ecomarket.admin.controller;

import com.ecomarket.admin.dto.*;
import com.ecomarket.admin.model.*;
import com.ecomarket.admin.service.AdministracionSoporteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdministracionSoporteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdministracionSoporteService administracionSoporteService;

    @InjectMocks
    private AdministracionSoporteController administracionSoporteController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Aquí está la magia: levantamos MockMvc sin usar @WebMvcTest
        mockMvc = MockMvcBuilders.standaloneSetup(administracionSoporteController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

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
                .andExpect(jsonPath("$.nombre").value("Tienda Santiago"));
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
                .andExpect(jsonPath("$[0].nombre").value("Tienda Valdivia"));
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
                .andExpect(jsonPath("$.idTienda").value(1));
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
                .andExpect(jsonPath("$.estado").value("ABIERTO"));
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
                .andExpect(jsonPath("$[0].idTicket").value(1));
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
                .andExpect(jsonPath("$.resuelta").value(false));
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
                .andExpect(jsonPath("$[0].resuelta").value(false));
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
                .andExpect(jsonPath("$.estado").value("PROGRAMADO"));
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
                .andExpect(jsonPath("$[0].estado").value("EJECUTADO"));
    }
}