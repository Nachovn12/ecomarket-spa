package com.ecomarket.reportes.service;

import com.ecomarket.reportes.dto.IndicadorKPIResponseDTO;
import com.ecomarket.reportes.dto.ReporteFiltroRequestDTO;
import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.dto.ReporteRendimientoDTO;
import com.ecomarket.reportes.dto.ReporteVentasDTO;
import com.ecomarket.reportes.entity.IndicadorKPI;
import com.ecomarket.reportes.entity.Reporte;
import com.ecomarket.reportes.entity.TipoKPI;
import com.ecomarket.reportes.entity.TipoReporte;
import com.ecomarket.reportes.exception.ReporteException;
import com.ecomarket.reportes.repository.IndicadorKPIRepository;
import com.ecomarket.reportes.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final IndicadorKPIRepository indicadorKPIRepository;

    public ReporteService(ReporteRepository reporteRepository,
                          IndicadorKPIRepository indicadorKPIRepository) {
        this.reporteRepository = reporteRepository;
        this.indicadorKPIRepository = indicadorKPIRepository;
    }

    private void validarRangoFechas(ReporteFiltroRequestDTO filtro) {
        if (filtro.getFechaInicio() == null || filtro.getFechaFin() == null) {
            throw new ReporteException("Las fechas de inicio y fin son obligatorias");
        }
        if (filtro.getFechaInicio().isAfter(filtro.getFechaFin())) {
            throw new ReporteException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }

    @Transactional(readOnly = true)
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reporte obtenerReportePorId(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() -> new ReporteException("Reporte no encontrado con id: " + id));
    }

    public Reporte crearReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public void eliminarReporte(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new ReporteException("Reporte no encontrado con id: " + id);
        }
        reporteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Reporte> listarPorTipo(TipoReporte tipo) {
        return reporteRepository.findByTipo(tipo);
    }

    @Transactional(readOnly = true)
    public List<Reporte> listarPorTienda(Long idTienda) {
        return reporteRepository.findByIdTienda(idTienda);
    }

    public ReporteVentasDTO generarReporteVentas(ReporteFiltroRequestDTO filtro) {
        validarRangoFechas(filtro);

        Reporte reporte = new Reporte();
        reporte.setTipo(TipoReporte.VENTAS);
        reporte.setIdTienda(filtro.getIdTienda());
        reporte.setFechaInicio(filtro.getFechaInicio());
        reporte.setFechaFin(filtro.getFechaFin());
        reporteRepository.save(reporte);

        // Nota académica Sprint 4:
        // Los cálculos se basan en datos internos de IndicadorKPI.
        // La integración real con MS Ventas, MS Inventario y MS Administración
        // queda representada de forma simulada para esta entrega.
        List<IndicadorKPI> kpisVentas = indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES);
        double totalVentas = kpisVentas.stream().mapToDouble(IndicadorKPI::getValor).sum();

        ReporteVentasDTO dto = new ReporteVentasDTO();
        dto.setIdTienda(filtro.getIdTienda());
        dto.setFechaInicio(filtro.getFechaInicio());
        dto.setFechaFin(filtro.getFechaFin());
        dto.setVentasTotales(totalVentas);
        dto.setTotalTransacciones(kpisVentas.size());
        dto.setProductosVendidos(kpisVentas.size() * 5);
        return dto;
    }

    public ReporteInventarioDTO generarReporteInventario(Long idTienda) {
        Reporte reporte = new Reporte();
        reporte.setTipo(TipoReporte.INVENTARIO);
        reporte.setIdTienda(idTienda);
        reporteRepository.save(reporte);

        // Nota académica Sprint 4:
        // Los cálculos se basan en datos internos de IndicadorKPI.
        // La integración real con MS Inventario queda simulada para esta entrega.
        List<IndicadorKPI> kpisBajoStock = indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO);
        List<IndicadorKPI> kpisRotacion = indicadorKPIRepository.findByTipo(TipoKPI.ROTACION_INVENTARIO);

        ReporteInventarioDTO dto = new ReporteInventarioDTO();
        dto.setIdTienda(idTienda);
        dto.setProductosDisponibles(kpisRotacion.size() * 10);
        dto.setProductosBajoStock(kpisBajoStock.size());
        dto.setProductosSinStock(0);
        return dto;
    }

    public ReporteRendimientoDTO generarReporteRendimiento(ReporteFiltroRequestDTO filtro) {
        validarRangoFechas(filtro);

        Reporte reporte = new Reporte();
        reporte.setTipo(TipoReporte.RENDIMIENTO_TIENDA);
        reporte.setIdTienda(filtro.getIdTienda());
        reporte.setFechaInicio(filtro.getFechaInicio());
        reporte.setFechaFin(filtro.getFechaFin());
        reporteRepository.save(reporte);

        // Nota académica Sprint 4:
        // Los cálculos se basan en datos internos de IndicadorKPI.
        // La integración real con MS Administración queda simulada para esta entrega.
        List<IndicadorKPI> kpisVentas = indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES);
        List<IndicadorKPI> kpisPedidos = indicadorKPIRepository.findByTipo(TipoKPI.PEDIDOS_ENTREGADOS);
        List<IndicadorKPI> kpisBajoStock = indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO);
        List<IndicadorKPI> kpisRendimiento = indicadorKPIRepository.findByTipo(TipoKPI.RENDIMIENTO_TIENDA);

        double ventas = kpisVentas.stream().mapToDouble(IndicadorKPI::getValor).sum();
        double rendimiento = kpisRendimiento.stream().mapToDouble(IndicadorKPI::getValor).average().orElse(0.0);

        ReporteRendimientoDTO dto = new ReporteRendimientoDTO();
        dto.setIdTienda(filtro.getIdTienda());
        dto.setFechaInicio(filtro.getFechaInicio());
        dto.setFechaFin(filtro.getFechaFin());
        dto.setVentasPorTienda(ventas);
        dto.setPedidosEntregados(kpisPedidos.size());
        dto.setStockBajo(kpisBajoStock.size());
        dto.setRendimientoOperativo(rendimiento);
        return dto;
    }

    @Transactional(readOnly = true)
    public List<IndicadorKPI> listarKPIs() {
        return indicadorKPIRepository.findAll();
    }

    @Transactional(readOnly = true)
    public IndicadorKPI obtenerKPIPorId(Long id) {
        return indicadorKPIRepository.findById(id)
                .orElseThrow(() -> new ReporteException("IndicadorKPI no encontrado con id: " + id));
    }

    public IndicadorKPI crearKPI(IndicadorKPI kpi) {
        return indicadorKPIRepository.save(kpi);
    }

    public void eliminarKPI(Long id) {
        if (!indicadorKPIRepository.existsById(id)) {
            throw new ReporteException("IndicadorKPI no encontrado con id: " + id);
        }
        indicadorKPIRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<IndicadorKPI> listarKPIsPorTipo(TipoKPI tipo) {
        return indicadorKPIRepository.findByTipo(tipo);
    }

    public IndicadorKPIResponseDTO toDTO(IndicadorKPI kpi) {
        IndicadorKPIResponseDTO dto = new IndicadorKPIResponseDTO();
        dto.setId(kpi.getId());
        dto.setTipo(kpi.getTipo().name());
        dto.setValor(kpi.getValor());
        dto.setDescripcion(kpi.getDescripcion());
        dto.setFechaCalculo(kpi.getFechaCalculo());
        return dto;
    }
}