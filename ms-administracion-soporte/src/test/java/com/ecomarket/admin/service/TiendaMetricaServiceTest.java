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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TiendaMetricaServiceTest {

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

    private Tienda tiendaEjemplo() {
        return Tienda.builder()
                .idTienda(1L)
                .nombre("Tienda Santiago")
                .ciudad("Santiago")
                .horarioApertura(LocalTime.of(9, 0))
                .horarioCierre(LocalTime.of(21, 0))
                .activa(true)
                .build();
    }

    @Test
    @DisplayName("AC-3: crear tienda con datos validos retorna TiendaResponseDTO")
    void crearTienda_datosValidos_retornaDTO() {
        TiendaRequestDTO req = new TiendaRequestDTO();
        req.setNombre("Tienda Santiago");
        req.setCiudad("Santiago");
        req.setHorarioApertura(LocalTime.of(9, 0));
        req.setHorarioCierre(LocalTime.of(21, 0));

        when(tiendaRepository.save(any(Tienda.class))).thenReturn(tiendaEjemplo());

        TiendaResponseDTO resp = service.crearTienda(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getNombre()).isEqualTo("Tienda Santiago");
        assertThat(resp.getCiudad()).isEqualTo("Santiago");
        verify(tiendaRepository).save(any(Tienda.class));
    }

    @Test
    @DisplayName("AC-3: crear tienda con horario invalido lanza IllegalArgumentException")
    void crearTienda_horarioInvalido_lanzaExcepcion() {
        TiendaRequestDTO req = new TiendaRequestDTO();
        req.setNombre("Tienda Valdivia");
        req.setCiudad("Valdivia");
        req.setHorarioApertura(LocalTime.of(21, 0));
        req.setHorarioCierre(LocalTime.of(9, 0));

        assertThatThrownBy(() -> service.crearTienda(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("AC-3: consultar tienda existente retorna DTO")
    void consultarTienda_existente_retornaDTO() {
        when(tiendaRepository.findById(1L)).thenReturn(Optional.of(tiendaEjemplo()));

        TiendaResponseDTO resp = service.consultarTienda(1L);

        assertThat(resp.getIdTienda()).isEqualTo(1L);
        assertThat(resp.getNombre()).isEqualTo("Tienda Santiago");
    }

    @Test
    @DisplayName("AC-3: consultar tienda inexistente lanza RecursoNoEncontradoException")
    void consultarTienda_inexistente_lanzaExcepcion() {
        when(tiendaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.consultarTienda(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    @DisplayName("AC-3: listar tiendas retorna lista no nula")
    void listarTiendas_retornaLista() {
        when(tiendaRepository.findAll()).thenReturn(List.of(tiendaEjemplo()));

        List<TiendaResponseDTO> lista = service.listarTiendas();

        assertThat(lista).hasSize(1);
    }

    @Test
    @DisplayName("AC-4: registrar metrica con MS disponible no genera alerta")
    void registrarMetrica_msDisponible_noGeneraAlerta() {
        MetricaSistemaRequestDTO req = new MetricaSistemaRequestDTO();
        req.setMicroservicio("ms-catalogo");
        req.setDisponible(true);
        req.setTiempoRespuestaMs(120L);
        req.setErroresDetectados(0);

        MetricaSistema metrica = MetricaSistema.builder()
                .idMetrica(1L)
                .microservicio("ms-catalogo")
                .disponible(true)
                .tiempoRespuestaMs(120L)
                .erroresDetectados(0)
                .build();

        when(metricaSistemaRepository.save(any())).thenReturn(metrica);

        MetricaSistemaResponseDTO resp = service.registrarMetrica(req);

        assertThat(resp.getMicroservicio()).isEqualTo("ms-catalogo");
        assertThat(resp.getDisponible()).isTrue();
        verify(alertaSistemaRepository, never()).save(any());
    }

    @Test
    @DisplayName("AC-4: registrar metrica con MS no disponible genera alerta automatica")
    void registrarMetrica_msNoDisponible_generaAlerta() {
        MetricaSistemaRequestDTO req = new MetricaSistemaRequestDTO();
        req.setMicroservicio("ms-pedidos");
        req.setDisponible(false);
        req.setTiempoRespuestaMs(0L);
        req.setErroresDetectados(0);

        MetricaSistema metrica = MetricaSistema.builder()
                .idMetrica(2L)
                .microservicio("ms-pedidos")
                .disponible(false)
                .tiempoRespuestaMs(0L)
                .erroresDetectados(0)
                .build();

        when(metricaSistemaRepository.save(any())).thenReturn(metrica);
        when(alertaSistemaRepository.save(any())).thenReturn(new AlertaSistema());

        service.registrarMetrica(req);

        verify(alertaSistemaRepository).save(any());
    }

    @Test
    @DisplayName("AC-4: registrar metrica con errores detectados genera alerta")
    void registrarMetrica_conErrores_generaAlerta() {
        MetricaSistemaRequestDTO req = new MetricaSistemaRequestDTO();
        req.setMicroservicio("ms-inventario");
        req.setDisponible(true);
        req.setTiempoRespuestaMs(200L);
        req.setErroresDetectados(5);

        MetricaSistema metrica = MetricaSistema.builder()
                .idMetrica(3L)
                .microservicio("ms-inventario")
                .disponible(true)
                .erroresDetectados(5)
                .build();

        when(metricaSistemaRepository.save(any())).thenReturn(metrica);
        when(alertaSistemaRepository.save(any())).thenReturn(new AlertaSistema());

        service.registrarMetrica(req);

        verify(alertaSistemaRepository).save(any());
    }
}