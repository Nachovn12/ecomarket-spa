package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.dto.ReporteVentasDTO;
import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.entity.ReporteRendimiento;
import com.ecomarket.reportes.service.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    @Autowired
    private ReportesService reportesService;

    @GetMapping("/ventas")
    public ResponseEntity<List<ReporteVentasDTO>> obtenerReporteVentas() {
        return ResponseEntity.ok(reportesService.obtenerReporteVentas());
    }

    @GetMapping("/inventario")
    public ResponseEntity<List<ReporteInventarioDTO>> obtenerReporteInventario() {
        return ResponseEntity.ok(reportesService.obtenerReporteInventario());
    }

    @GetMapping("/rendimiento")
    public ResponseEntity<List<ReporteRendimiento>> obtenerReporteRendimiento() {
        return ResponseEntity.ok(reportesService.obtenerReporteRendimiento());
    }

    @GetMapping("/tienda/{id}")
    public ResponseEntity<List<ReporteRendimiento>> obtenerReportePorTienda(@PathVariable Long id) {
        return ResponseEntity.ok(reportesService.obtenerReportePorTienda(id));
    }
}