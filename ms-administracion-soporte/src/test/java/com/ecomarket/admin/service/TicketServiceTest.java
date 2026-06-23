package com.ecomarket.admin.service;

import com.ecomarket.admin.dto.*;
import com.ecomarket.admin.exception.RecursoNoEncontradoException;
import com.ecomarket.admin.model.*;
import com.ecomarket.admin.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

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

    private TicketSoporte ticketEjemplo(PrioridadTicket prioridad) {
        return TicketSoporte.builder()
                .idTicket(1L)
                .asunto("Problema con pedido")
                .descripcion("No llega el pedido")
                .nombreContacto("Juan Perez")
                .correoContacto("juan@correo.cl")
                .prioridad(prioridad)
                .estado(EstadoTicket.ABIERTO)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("AC-7: crear ticket con prioridad ALTA retorna DTO con estado ABIERTO")
    void crearTicket_prioridadAlta_retornaDTO() {
        TicketSoporteRequestDTO req = new TicketSoporteRequestDTO();
        req.setAsunto("Error en pagos");
        req.setDescripcion("Fallo en pasarela de pago");
        req.setNombreContacto("Maria Lopez");
        req.setCorreoContacto("maria@correo.cl");
        req.setPrioridad(PrioridadTicket.ALTA);

        when(ticketSoporteRepository.save(any())).thenReturn(ticketEjemplo(PrioridadTicket.ALTA));

        TicketSoporteResponseDTO resp = service.crearTicketSoporte(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getEstado()).isEqualTo(EstadoTicket.ABIERTO);
        assertThat(resp.getPrioridad()).isEqualTo(PrioridadTicket.ALTA);
        verify(ticketSoporteRepository).save(any());
    }

    @Test
    @DisplayName("AC-7: crear ticket con prioridad MEDIA retorna DTO correctamente")
    void crearTicket_prioridadMedia_retornaDTO() {
        TicketSoporteRequestDTO req = new TicketSoporteRequestDTO();
        req.setAsunto("Consulta sobre factura");
        req.setDescripcion("No recibo factura electronica");
        req.setNombreContacto("Pedro Soto");
        req.setCorreoContacto("pedro@correo.cl");
        req.setPrioridad(PrioridadTicket.MEDIA);

        when(ticketSoporteRepository.save(any())).thenReturn(ticketEjemplo(PrioridadTicket.MEDIA));

        TicketSoporteResponseDTO resp = service.crearTicketSoporte(req);

        assertThat(resp.getPrioridad()).isEqualTo(PrioridadTicket.MEDIA);
    }

    @Test
    @DisplayName("AC-7: crear ticket con prioridad BAJA retorna DTO correctamente")
    void crearTicket_prioridadBaja_retornaDTO() {
        TicketSoporteRequestDTO req = new TicketSoporteRequestDTO();
        req.setAsunto("Sugerencia de mejora");
        req.setDescripcion("Agregar modo oscuro");
        req.setNombreContacto("Ana Garcia");
        req.setCorreoContacto("ana@correo.cl");
        req.setPrioridad(PrioridadTicket.BAJA);

        when(ticketSoporteRepository.save(any())).thenReturn(ticketEjemplo(PrioridadTicket.BAJA));

        TicketSoporteResponseDTO resp = service.crearTicketSoporte(req);

        assertThat(resp.getPrioridad()).isEqualTo(PrioridadTicket.BAJA);
    }

    @Test
    @DisplayName("AC-8: actualizar estado de ticket a EN_ATENCION")
    void actualizarEstado_aEnAtencion_retornaDTO() {
        TicketSoporte ticket = ticketEjemplo(PrioridadTicket.ALTA);
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketSoporteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TicketSoporteResponseDTO resp = service.actualizarEstadoTicket(1L, EstadoTicket.EN_ATENCION);

        assertThat(resp.getEstado()).isEqualTo(EstadoTicket.EN_ATENCION);
    }

    @Test
    @DisplayName("AC-8: actualizar estado de ticket a CERRADO")
    void actualizarEstado_aCerrado_retornaDTO() {
        TicketSoporte ticket = ticketEjemplo(PrioridadTicket.ALTA);
        ticket.setEstado(EstadoTicket.EN_ATENCION);
        when(ticketSoporteRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketSoporteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TicketSoporteResponseDTO resp = service.actualizarEstadoTicket(1L, EstadoTicket.CERRADO);

        assertThat(resp.getEstado()).isEqualTo(EstadoTicket.CERRADO);
    }

    @Test
    @DisplayName("AC-8: consultar ticket inexistente lanza RecursoNoEncontradoException")
    void consultarTicket_inexistente_lanzaExcepcion() {
        when(ticketSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.consultarTicket(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    @DisplayName("AC-8: listar tickets retorna lista no vacia")
    void listarTickets_retornaLista() {
        when(ticketSoporteRepository.findAll()).thenReturn(List.of(ticketEjemplo(PrioridadTicket.MEDIA)));

        List<TicketSoporteResponseDTO> lista = service.listarTickets();

        assertThat(lista).hasSize(1);
    }
}