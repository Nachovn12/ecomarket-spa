package com.ecomarket.reportes.service;

import com.ecomarket.reportes.dto.ReporteFiltroRequestDTO;
import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.dto.ReporteVentasDTO;
import com.ecomarket.reportes.exception.ReporteException;
import com.ecomarket.reportes.exception.ReporteNotFoundException;
import com.ecomarket.reportes.model.IndicadorKPI;
import com.ecomarket.reportes.model.Reporte;
import com.ecomarket.reportes.model.TipoKPI;
import com.ecomarket.reportes.model.TipoReporte;
import com.ecomarket.reportes.repository.IndicadorKPIRepository;
import com.ecomarket.reportes.repository.ReporteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private IndicadorKPIRepository indicadorKPIRepository;

    @InjectMocks
    private ReporteService reporteService;

    private IndicadorKPI buildKpi(Long id, TipoKPI tipo, double valor) {
        IndicadorKPI kpi = new IndicadorKPI();
        kpi.setId(id);
        kpi.setTipo(tipo);
        kpi.setValor(valor);
        return kpi;
    }

    private ReporteFiltroRequestDTO buildFiltro(Long idTienda, LocalDate inicio, LocalDate fin) {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(idTienda);
        filtro.setFechaInicio(inicio);
        filtro.setFechaFin(fin);
        return filtro;
    }

    // AC-1: generarReporteVentas — suma KPIs y mapea DTO correctamente
    @Test
    void generarReporteVentas_conFiltroValido_retornaVentasTotalesCorrectas() {
        ReporteFiltroRequestDTO filtro = buildFiltro(1L,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));
        List<IndicadorKPI> kpis = List.of(
                buildKpi(1L, TipoKPI.VENTAS_TOTALES, 100.0),
                buildKpi(2L, TipoKPI.VENTAS_TOTALES, 150.0));
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(inv -> inv.getArgument(0));
        when(indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES)).thenReturn(kpis);

        ReporteVentasDTO dto = reporteService.generarReporteVentas(filtro);

        assertThat(dto.getVentasTotales()).isEqualTo(250.0);
        assertThat(dto.getTotalTransacciones()).isEqualTo(2);
        assertThat(dto.getIdTienda()).isEqualTo(1L);
        verify(reporteRepository).save(any(Reporte.class));
    }

    // AC-1: generarReporteInventario — cuenta productos bajo stock y disponibles
    @Test
    void generarReporteInventario_retornaProductosBajoStockCorrecto() {
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(inv -> inv.getArgument(0));
        when(indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO))
                .thenReturn(List.of(
                        buildKpi(1L, TipoKPI.STOCK_BAJO, 3.0),
                        buildKpi(2L, TipoKPI.STOCK_BAJO, 2.0),
                        buildKpi(3L, TipoKPI.STOCK_BAJO, 1.0)));
        when(indicadorKPIRepository.findByTipo(TipoKPI.ROTACION_INVENTARIO))
                .thenReturn(List.of(
                        buildKpi(4L, TipoKPI.ROTACION_INVENTARIO, 1.0),
                        buildKpi(5L, TipoKPI.ROTACION_INVENTARIO, 1.0)));

        ReporteInventarioDTO dto = reporteService.generarReporteInventario(1L);

        assertThat(dto.getProductosBajoStock()).isEqualTo(3);
        assertThat(dto.getProductosDisponibles()).isEqualTo(20);
        assertThat(dto.getIdTienda()).isEqualTo(1L);
    }

    // AC-3: fechaInicio nula lanza ReporteException (no llama a save)
    @Test
    void generarReporteVentas_conFechaInicioNula_lanzaReporteException() {
        ReporteFiltroRequestDTO filtro = buildFiltro(1L, null, LocalDate.of(2026, 6, 30));

        assertThrows(ReporteException.class, () -> reporteService.generarReporteVentas(filtro));
        verify(reporteRepository, never()).save(any());
    }

    // AC-3: fechaFin nula lanza ReporteException (no llama a save)
    @Test
    void generarReporteVentas_conFechaFinNula_lanzaReporteException() {
        ReporteFiltroRequestDTO filtro = buildFiltro(1L, LocalDate.of(2026, 6, 1), null);

        assertThrows(ReporteException.class, () -> reporteService.generarReporteVentas(filtro));
        verify(reporteRepository, never()).save(any());
    }

    // AC-3: reporte inexistente lanza ReporteNotFoundException
    @Test
    void obtenerReportePorId_conIdInexistente_lanzaReporteNotFoundException() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ReporteNotFoundException.class, () -> reporteService.obtenerReportePorId(99L));
    }

    // AC-1: crearReporte persiste y retorna el reporte guardado con id
    @Test
    void crearReporte_retornaReporteGuardadoConId() {
        Reporte entrada = new Reporte();
        entrada.setTipo(TipoReporte.VENTAS);
        entrada.setIdTienda(1L);
        Reporte guardado = new Reporte();
        guardado.setId(1L);
        guardado.setTipo(TipoReporte.VENTAS);
        when(reporteRepository.save(entrada)).thenReturn(guardado);

        Reporte resultado = reporteService.crearReporte(entrada);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTipo()).isEqualTo(TipoReporte.VENTAS);
        verify(reporteRepository).save(entrada);
    }
}
