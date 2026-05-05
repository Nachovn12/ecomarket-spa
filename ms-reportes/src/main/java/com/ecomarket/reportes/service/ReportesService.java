package com.ecomarket.reportes.service;

import com.ecomarket.reportes.dto.ReporteVentasDTO;
import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.entity.ReporteVentas;
import com.ecomarket.reportes.entity.ReporteInventario;
import com.ecomarket.reportes.entity.ReporteRendimiento;
import com.ecomarket.reportes.repository.ReporteVentasRepository;
import com.ecomarket.reportes.repository.ReporteInventarioRepository;
import com.ecomarket.reportes.repository.ReporteRendimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportesService {

    @Autowired
    private ReporteVentasRepository reporteVentasRepository;

    @Autowired
    private ReporteInventarioRepository reporteInventarioRepository;

    @Autowired
    private ReporteRendimientoRepository reporteRendimientoRepository;

    public List<ReporteVentasDTO> obtenerReporteVentas() {
        return reporteVentasRepository.findAll().stream()
                .map(this::mapVentasToDTO)
                .collect(Collectors.toList());
    }

    public List<ReporteInventarioDTO> obtenerReporteInventario() {
        return reporteInventarioRepository.findAll().stream()
                .map(this::mapInventarioToDTO)
                .collect(Collectors.toList());
    }

    public List<ReporteRendimiento> obtenerReporteRendimiento() {
        return reporteRendimientoRepository.findAll();
    }

    public List<ReporteRendimiento> obtenerReportePorTienda(Long tiendaId) {
        return reporteRendimientoRepository.findAll().stream()
                .filter(r -> r.getTiendaId().equals(tiendaId))
                .collect(Collectors.toList());
    }

    private ReporteVentasDTO mapVentasToDTO(ReporteVentas reporte) {
        ReporteVentasDTO dto = new ReporteVentasDTO();
        dto.setId(reporte.getId());
        dto.setTotalVentas(reporte.getTotalVentas());
        dto.setCantidadPedidos(reporte.getCantidadPedidos());
        dto.setFechaReporte(reporte.getFechaReporte());
        dto.setPeriodo(reporte.getPeriodo());
        return dto;
    }

    private ReporteInventarioDTO mapInventarioToDTO(ReporteInventario reporte) {
        ReporteInventarioDTO dto = new ReporteInventarioDTO();
        dto.setId(reporte.getId());
        dto.setTotalProductos(reporte.getTotalProductos());
        dto.setProductosBajoStock(reporte.getProductosBajoStock());
        dto.setValorTotalInventario(reporte.getValorTotalInventario());
        dto.setFechaReporte(reporte.getFechaReporte());
        return dto;
    }
}