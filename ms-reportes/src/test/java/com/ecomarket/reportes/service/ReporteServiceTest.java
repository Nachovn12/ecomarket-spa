package com.ecomarket.reportes.service;

import com.ecomarket.reportes.dto.ReporteFiltroRequestDTO;
import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.dto.ReporteRendimientoDTO;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    // AC-2: listarReportes retorna todos los reportes del repositorio
    @Test
    void listarReportes_retornaListaDelRepositorio() {
        Reporte r1 = new Reporte(); r1.setId(1L); r1.setTipo(TipoReporte.VENTAS);
        Reporte r2 = new Reporte(); r2.setId(2L); r2.setTipo(TipoReporte.INVENTARIO);
        when(reporteRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Reporte> result = reporteService.listarReportes();

        assertThat(result).hasSize(2);
        verify(reporteRepository).findAll();
    }

    // AC-2: obtenerReportePorId con id válido retorna el reporte correcto
    @Test
    void obtenerReportePorId_conIdValido_retornaReporte() {
        Reporte reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTipo(TipoReporte.VENTAS);
        reporte.setIdTienda(1L);
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));

        Reporte result = reporteService.obtenerReportePorId(1L);

        assertNotNull(result);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTipo()).isEqualTo(TipoReporte.VENTAS);
    }

    // AC-2: eliminarReporte con id existente llama a deleteById
    @Test
    void eliminarReporte_conIdExistente_llamaDeleteById() {
        when(reporteRepository.existsById(1L)).thenReturn(true);

        reporteService.eliminarReporte(1L);

        verify(reporteRepository).deleteById(1L);
    }

    // AC-2: eliminarReporte con id inexistente lanza ReporteNotFoundException
    @Test
    void eliminarReporte_conIdInexistente_lanzaReporteNotFoundException() {
        when(reporteRepository.existsById(99L)).thenReturn(false);

        assertThrows(ReporteNotFoundException.class, () -> reporteService.eliminarReporte(99L));
        verify(reporteRepository, never()).deleteById(any());
    }

    // AC-2: listarPorTipo retorna reportes filtrados por tipo
    @Test
    void listarPorTipo_retornaReportesFiltradosPorTipo() {
        Reporte r = new Reporte(); r.setId(1L); r.setTipo(TipoReporte.VENTAS);
        when(reporteRepository.findByTipo(TipoReporte.VENTAS)).thenReturn(List.of(r));

        List<Reporte> result = reporteService.listarPorTipo(TipoReporte.VENTAS);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTipo()).isEqualTo(TipoReporte.VENTAS);
    }

    // AC-2: listarPorTienda retorna reportes de una tienda específica
    @Test
    void listarPorTienda_retornaReportesDeTienda() {
        Reporte r1 = new Reporte(); r1.setId(1L); r1.setTipo(TipoReporte.VENTAS); r1.setIdTienda(3L);
        Reporte r2 = new Reporte(); r2.setId(2L); r2.setTipo(TipoReporte.INVENTARIO); r2.setIdTienda(3L);
        when(reporteRepository.findByIdTienda(3L)).thenReturn(List.of(r1, r2));

        List<Reporte> result = reporteService.listarPorTienda(3L);

        assertThat(result).hasSize(2);
        verify(reporteRepository).findByIdTienda(3L);
    }

    // AC-1: generarReporteRendimiento con filtro válido retorna DTO con métricas correctas
    @Test
    void generarReporteRendimiento_conFiltroValido_retornaDTO() {
        ReporteFiltroRequestDTO filtro = buildFiltro(2L,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        when(reporteRepository.save(any(Reporte.class))).thenAnswer(inv -> inv.getArgument(0));
        when(indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES))
                .thenReturn(List.of(buildKpi(1L, TipoKPI.VENTAS_TOTALES, 120000.0)));
        when(indicadorKPIRepository.findByTipo(TipoKPI.PEDIDOS_ENTREGADOS))
                .thenReturn(List.of(buildKpi(2L, TipoKPI.PEDIDOS_ENTREGADOS, 1.0),
                                    buildKpi(3L, TipoKPI.PEDIDOS_ENTREGADOS, 1.0)));
        when(indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO))
                .thenReturn(List.of(buildKpi(4L, TipoKPI.STOCK_BAJO, 2.0)));
        when(indicadorKPIRepository.findByTipo(TipoKPI.RENDIMIENTO_TIENDA))
                .thenReturn(List.of(buildKpi(5L, TipoKPI.RENDIMIENTO_TIENDA, 0.9),
                                    buildKpi(6L, TipoKPI.RENDIMIENTO_TIENDA, 0.8)));

        ReporteRendimientoDTO result = reporteService.generarReporteRendimiento(filtro);

        assertThat(result.getIdTienda()).isEqualTo(2L);
        assertThat(result.getVentasPorTienda()).isEqualTo(120000.0);
        assertThat(result.getPedidosEntregados()).isEqualTo(2);
        assertThat(result.getStockBajo()).isEqualTo(1);
        assertThat(result.getRendimientoOperativo()).isEqualTo(0.85, org.assertj.core.data.Offset.offset(0.001));
    }

    // AC-3: fechaInicio posterior a fechaFin lanza ReporteException
    @Test
    void generarReporteVentas_conFechaInicioPostFechaFin_lanzaReporteException() {
        ReporteFiltroRequestDTO filtro = buildFiltro(1L,
                LocalDate.of(2026, 12, 31), LocalDate.of(2026, 1, 1));

        assertThrows(ReporteException.class, () -> reporteService.generarReporteVentas(filtro));
        verify(reporteRepository, never()).save(any());
    }
}
