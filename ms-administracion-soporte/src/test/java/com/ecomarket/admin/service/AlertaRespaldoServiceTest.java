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
class AlertaRespaldoServiceTest {

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

    @Test
    @DisplayName("AC-5: registrar alerta manual retorna AlertaSistemaResponseDTO")
    void registrarAlerta_datosValidos_retornaDTO() {
        AlertaSistemaRequestDTO req = new AlertaSistemaRequestDTO();
        req.setMicroservicio("ms-pedidos");
        req.setTipoAlerta("STOCK_CRITICO");
        req.setDescripcion("Stock bajo el minimo permitido");

        AlertaSistema alerta = AlertaSistema.builder()
                .idAlerta(1L)
                .microservicio("ms-pedidos")
                .tipoAlerta("STOCK_CRITICO")
                .descripcion("Stock bajo el minimo permitido")
                .resuelta(false)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        when(alertaSistemaRepository.save(any())).thenReturn(alerta);

        AlertaSistemaResponseDTO resp = service.registrarAlerta(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getTipoAlerta()).isEqualTo("STOCK_CRITICO");
        assertThat(resp.getResuelta()).isFalse();
        verify(alertaSistemaRepository).save(any());
    }

    @Test
    @DisplayName("AC-5: resolver alerta existente cambia resuelta a true")
    void resolverAlerta_existente_marcaResuelta() {
        AlertaSistema alerta = AlertaSistema.builder()
                .idAlerta(1L)
                .microservicio("ms-catalogo")
                .tipoAlerta("ERRORES_DETECTADOS")
                .resuelta(false)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        when(alertaSistemaRepository.findById(1L)).thenReturn(Optional.of(alerta));
        when(alertaSistemaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        AlertaSistemaResponseDTO resp = service.resolverAlerta(1L);

        assertThat(resp.getResuelta()).isTrue();
        assertThat(resp.getFechaResolucion()).isNotNull();
    }

    @Test
    @DisplayName("AC-5: resolver alerta inexistente lanza RecursoNoEncontradoException")
    void resolverAlerta_inexistente_lanzaExcepcion() {
        when(alertaSistemaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.resolverAlerta(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    @DisplayName("AC-5: listar alertas activas retorna solo no resueltas")
    void listarAlertasActivas_retornaListaNoResueltas() {
        AlertaSistema alerta = AlertaSistema.builder()
                .idAlerta(1L)
                .microservicio("ms-reportes")
                .tipoAlerta("MICROSERVICIO_NO_DISPONIBLE")
                .resuelta(false)
                .fechaGeneracion(LocalDateTime.now())
                .build();

        when(alertaSistemaRepository.findByResueltaFalse()).thenReturn(List.of(alerta));

        List<AlertaSistemaResponseDTO> lista = service.listarAlertasActivas();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getResuelta()).isFalse();
    }

    @Test
    @DisplayName("AC-6: programar respaldo retorna DTO con estado PROGRAMADO")
    void programarRespaldo_datosValidos_retornaDTO() {
        RespaldoDatosRequestDTO req = new RespaldoDatosRequestDTO();
        req.setOrigenDatos("bd_ventas");
        req.setFrecuencia("DIARIA");
        req.setResponsable("admin1");
        req.setFechaProgramada(LocalDateTime.now().plusDays(1));

        RespaldoDatos respaldo = RespaldoDatos.builder()
                .idRespaldo(1L)
                .origenDatos("bd_ventas")
                .frecuencia("DIARIA")
                .responsable("admin1")
                .estado("PROGRAMADO")
                .resultado("Respaldo programado correctamente")
                .build();

        when(respaldoDatosRepository.save(any())).thenReturn(respaldo);

        RespaldoDatosResponseDTO resp = service.programarRespaldo(req);

        assertThat(resp.getEstado()).isEqualTo("PROGRAMADO");
        assertThat(resp.getOrigenDatos()).isEqualTo("bd_ventas");
        verify(respaldoDatosRepository).save(any());
    }

    @Test
    @DisplayName("AC-6: ejecutar respaldo cambia estado a EJECUTADO")
    void ejecutarRespaldo_existente_cambiaEstado() {
        RespaldoDatos respaldo = RespaldoDatos.builder()
                .idRespaldo(1L)
                .origenDatos("bd_usuarios")
                .estado("PROGRAMADO")
                .build();

        when(respaldoDatosRepository.findById(1L)).thenReturn(Optional.of(respaldo));
        when(respaldoDatosRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RespaldoDatosResponseDTO resp = service.ejecutarRespaldo(1L);

        assertThat(resp.getEstado()).isEqualTo("EJECUTADO");
        assertThat(resp.getFechaEjecucion()).isNotNull();
    }

    @Test
    @DisplayName("AC-6: restaurar respaldo ejecutado cambia estado a RESTAURADO")
    void restaurarRespaldo_ejecutado_cambiaEstado() {
        RespaldoDatos respaldo = RespaldoDatos.builder()
                .idRespaldo(1L)
                .origenDatos("bd_admin")
                .estado("EJECUTADO")
                .build();

        when(respaldoDatosRepository.findById(1L)).thenReturn(Optional.of(respaldo));
        when(respaldoDatosRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RespaldoDatosResponseDTO resp = service.restaurarRespaldo(1L);

        assertThat(resp.getEstado()).isEqualTo("RESTAURADO");
        assertThat(resp.getFechaRestauracion()).isNotNull();
    }

    @Test
    @DisplayName("AC-6: restaurar respaldo no ejecutado lanza IllegalArgumentException")
    void restaurarRespaldo_noEjecutado_lanzaExcepcion() {
        RespaldoDatos respaldo = RespaldoDatos.builder()
                .idRespaldo(1L)
                .origenDatos("bd_admin")
                .estado("PROGRAMADO")
                .build();

        when(respaldoDatosRepository.findById(1L)).thenReturn(Optional.of(respaldo));

        assertThatThrownBy(() -> service.restaurarRespaldo(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}