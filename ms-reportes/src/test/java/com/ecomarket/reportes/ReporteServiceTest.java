package com.ecomarket.reportes;

import com.ecomarket.reportes.dto.IndicadorKPIResponseDTO;
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
import com.ecomarket.reportes.service.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private IndicadorKPIRepository indicadorKPIRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Reporte reporteVentas;
    private IndicadorKPI kpiVentas;
    private IndicadorKPI kpiBajoStock;
    private IndicadorKPI kpiRotacion;

    @BeforeEach
    void setUp() {
        reporteVentas = new Reporte();
        reporteVentas.setId(1L);
        reporteVentas.setTipo(TipoReporte.VENTAS);
        reporteVentas.setIdTienda(1L);

        kpiVentas = new IndicadorKPI();
        kpiVentas.setId(1L);
        kpiVentas.setTipo(TipoKPI.VENTAS_TOTALES);
        kpiVentas.setValor(50000.0);
        kpiVentas.setDescripcion("Ventas totales del mes");

        kpiBajoStock = new IndicadorKPI();
        kpiBajoStock.setId(2L);
        kpiBajoStock.setTipo(TipoKPI.STOCK_BAJO);
        kpiBajoStock.setValor(5.0);

        kpiRotacion = new IndicadorKPI();
        kpiRotacion.setId(3L);
        kpiRotacion.setTipo(TipoKPI.ROTACION_INVENTARIO);
        kpiRotacion.setValor(0.8);
    }

    @Test
    void listarReportes_retornaListaConElementos() {
        when(reporteRepository.findAll()).thenReturn(List.of(reporteVentas));

        List<Reporte> resultado = reporteService.listarReportes();

        assertEquals(1, resultado.size());
        assertEquals(TipoReporte.VENTAS, resultado.get(0).getTipo());
        verify(reporteRepository).findAll();
    }

    @Test
    void obtenerReportePorId_existente_retornaReporte() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporteVentas));

        Reporte resultado = reporteService.obtenerReportePorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(TipoReporte.VENTAS, resultado.getTipo());
        verify(reporteRepository).findById(1L);
    }

    @Test
    void obtenerReportePorId_inexistente_lanzaReporteNotFoundException() {
        when(reporteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ReporteNotFoundException.class, () -> reporteService.obtenerReportePorId(999L));
        verify(reporteRepository).findById(999L);
    }

    @Test
    void crearReporte_guardaYRetornaReporte() {
        Reporte nuevo = new Reporte();
        nuevo.setTipo(TipoReporte.INVENTARIO);
        nuevo.setIdTienda(2L);

        Reporte guardado = new Reporte();
        guardado.setId(5L);
        guardado.setTipo(TipoReporte.INVENTARIO);
        guardado.setIdTienda(2L);

        when(reporteRepository.save(nuevo)).thenReturn(guardado);

        Reporte resultado = reporteService.crearReporte(nuevo);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals(TipoReporte.INVENTARIO, resultado.getTipo());
        verify(reporteRepository).save(nuevo);
    }

    @Test
    void eliminarReporte_existente_llamaDeleteById() {
        when(reporteRepository.existsById(1L)).thenReturn(true);

        reporteService.eliminarReporte(1L);

        verify(reporteRepository).deleteById(1L);
    }

    @Test
    void eliminarReporte_inexistente_lanzaReporteNotFoundException() {
        when(reporteRepository.existsById(999L)).thenReturn(false);

        assertThrows(ReporteNotFoundException.class, () -> reporteService.eliminarReporte(999L));
        verify(reporteRepository, never()).deleteById(any());
    }

    @Test
    void generarReporteVentas_fechasNulas_lanzaReporteException() {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(1L);
        filtro.setFechaInicio(null);
        filtro.setFechaFin(null);

        assertThrows(ReporteException.class, () -> reporteService.generarReporteVentas(filtro));
        verify(reporteRepository, never()).save(any());
    }

    @Test
    void generarReporteVentas_fechaInicioPostFechaFin_lanzaReporteException() {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(1L);
        filtro.setFechaInicio(LocalDate.of(2026, 6, 30));
        filtro.setFechaFin(LocalDate.of(2026, 6, 1));

        assertThrows(ReporteException.class, () -> reporteService.generarReporteVentas(filtro));
        verify(reporteRepository, never()).save(any());
    }

    @Test
    void generarReporteVentas_valido_retornaDtoConTotalesCorrectos() {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(1L);
        filtro.setFechaInicio(LocalDate.of(2026, 6, 1));
        filtro.setFechaFin(LocalDate.of(2026, 6, 30));

        when(indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES))
                .thenReturn(List.of(kpiVentas));

        ReporteVentasDTO resultado = reporteService.generarReporteVentas(filtro);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdTienda());
        assertEquals(50000.0, resultado.getVentasTotales());
        assertEquals(1, resultado.getTotalTransacciones());
        assertEquals(5, resultado.getProductosVendidos());
        verify(reporteRepository).save(any(Reporte.class));
    }

    @Test
    void generarReporteInventario_valido_retornaDtoConProductos() {
        when(indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO))
                .thenReturn(List.of(kpiBajoStock));
        when(indicadorKPIRepository.findByTipo(TipoKPI.ROTACION_INVENTARIO))
                .thenReturn(List.of(kpiRotacion));

        ReporteInventarioDTO resultado = reporteService.generarReporteInventario(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdTienda());
        assertEquals(1, resultado.getProductosBajoStock());
        assertEquals(0, resultado.getProductosSinStock());
        verify(reporteRepository).save(any(Reporte.class));
    }

    @Test
    void listarKPIs_retornaListaConElementos() {
        when(indicadorKPIRepository.findAll()).thenReturn(List.of(kpiVentas, kpiBajoStock));

        List<IndicadorKPI> resultado = reporteService.listarKPIs();

        assertEquals(2, resultado.size());
        verify(indicadorKPIRepository).findAll();
    }

    @Test
    void crearKPI_guardaYRetornaKPI() {
        IndicadorKPI nuevo = new IndicadorKPI();
        nuevo.setTipo(TipoKPI.PEDIDOS_ENTREGADOS);
        nuevo.setValor(100.0);

        IndicadorKPI guardado = new IndicadorKPI();
        guardado.setId(10L);
        guardado.setTipo(TipoKPI.PEDIDOS_ENTREGADOS);
        guardado.setValor(100.0);

        when(indicadorKPIRepository.save(nuevo)).thenReturn(guardado);

        IndicadorKPI resultado = reporteService.crearKPI(nuevo);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(TipoKPI.PEDIDOS_ENTREGADOS, resultado.getTipo());
        verify(indicadorKPIRepository).save(nuevo);
    }

    @Test
    void eliminarKPI_inexistente_lanzaReporteNotFoundException() {
        when(indicadorKPIRepository.existsById(999L)).thenReturn(false);

        assertThrows(ReporteNotFoundException.class, () -> reporteService.eliminarKPI(999L));
        verify(indicadorKPIRepository, never()).deleteById(any());
    }

    @Test
    void toDTO_mapeaTodosLosCamposCorrectamente() {
        kpiVentas.setFechaCalculo(LocalDateTime.of(2026, 6, 22, 10, 0));

        IndicadorKPIResponseDTO dto = reporteService.toDTO(kpiVentas);

        assertEquals(1L, dto.getId());
        assertEquals("VENTAS_TOTALES", dto.getTipo());
        assertEquals(50000.0, dto.getValor());
        assertEquals("Ventas totales del mes", dto.getDescripcion());
        assertNotNull(dto.getFechaCalculo());
    }

    @Test
    void listarPorTipo_retornaListaFiltrada() {
        when(reporteRepository.findByTipo(TipoReporte.VENTAS)).thenReturn(List.of(reporteVentas));

        List<Reporte> resultado = reporteService.listarPorTipo(TipoReporte.VENTAS);

        assertEquals(1, resultado.size());
        assertEquals(TipoReporte.VENTAS, resultado.get(0).getTipo());
        verify(reporteRepository).findByTipo(TipoReporte.VENTAS);
    }

    @Test
    void listarPorTienda_retornaListaFiltrada() {
        when(reporteRepository.findByIdTienda(1L)).thenReturn(List.of(reporteVentas));

        List<Reporte> resultado = reporteService.listarPorTienda(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdTienda());
        verify(reporteRepository).findByIdTienda(1L);
    }

    @Test
    void generarReporteRendimiento_valido_retornaDTO() {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(1L);
        filtro.setFechaInicio(LocalDate.of(2026, 6, 1));
        filtro.setFechaFin(LocalDate.of(2026, 6, 30));

        IndicadorKPI kpiPedido = new IndicadorKPI();
        kpiPedido.setTipo(TipoKPI.PEDIDOS_ENTREGADOS);
        kpiPedido.setValor(47.0);

        IndicadorKPI kpiRendimiento = new IndicadorKPI();
        kpiRendimiento.setTipo(TipoKPI.RENDIMIENTO_TIENDA);
        kpiRendimiento.setValor(0.85);

        when(indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES)).thenReturn(List.of(kpiVentas));
        when(indicadorKPIRepository.findByTipo(TipoKPI.PEDIDOS_ENTREGADOS)).thenReturn(List.of(kpiPedido));
        when(indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO)).thenReturn(List.of(kpiBajoStock));
        when(indicadorKPIRepository.findByTipo(TipoKPI.RENDIMIENTO_TIENDA)).thenReturn(List.of(kpiRendimiento));

        ReporteRendimientoDTO resultado = reporteService.generarReporteRendimiento(filtro);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdTienda());
        assertEquals(50000.0, resultado.getVentasPorTienda());
        assertEquals(1, resultado.getPedidosEntregados());
        assertEquals(1, resultado.getStockBajo());
        assertEquals(0.85, resultado.getRendimientoOperativo());
        verify(reporteRepository).save(any(Reporte.class));
    }

    @Test
    void generarReporteRendimiento_fechasNulas_lanzaReporteException() {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(1L);
        filtro.setFechaInicio(null);
        filtro.setFechaFin(null);

        assertThrows(ReporteException.class, () -> reporteService.generarReporteRendimiento(filtro));
    }

    @Test
    void obtenerKPIPorId_existente_retornaKPI() {
        when(indicadorKPIRepository.findById(1L)).thenReturn(Optional.of(kpiVentas));

        IndicadorKPI resultado = reporteService.obtenerKPIPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(TipoKPI.VENTAS_TOTALES, resultado.getTipo());
    }

    @Test
    void obtenerKPIPorId_inexistente_lanzaReporteNotFoundException() {
        when(indicadorKPIRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ReporteNotFoundException.class, () -> reporteService.obtenerKPIPorId(999L));
    }

    @Test
    void eliminarKPI_existente_llamaDeleteById() {
        when(indicadorKPIRepository.existsById(1L)).thenReturn(true);

        reporteService.eliminarKPI(1L);

        verify(indicadorKPIRepository).deleteById(1L);
    }

    @Test
    void listarKPIsPorTipo_retornaListaFiltrada() {
        when(indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO)).thenReturn(List.of(kpiBajoStock));

        List<IndicadorKPI> resultado = reporteService.listarKPIsPorTipo(TipoKPI.STOCK_BAJO);

        assertEquals(1, resultado.size());
        assertEquals(TipoKPI.STOCK_BAJO, resultado.get(0).getTipo());
        verify(indicadorKPIRepository).findByTipo(TipoKPI.STOCK_BAJO);
    }

    @Test
    void generarReporteVentas_conFechaFinNula_lanzaReporteException() {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(1L);
        filtro.setFechaInicio(LocalDate.of(2026, 6, 1));
        filtro.setFechaFin(null);

        assertThrows(ReporteException.class, () -> reporteService.generarReporteVentas(filtro));
        verify(reporteRepository, never()).save(any());
    }
}
