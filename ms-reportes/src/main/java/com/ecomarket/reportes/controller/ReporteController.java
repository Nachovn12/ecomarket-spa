package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.dto.ReporteFiltroRequestDTO;
import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.dto.ReporteRendimientoDTO;
import com.ecomarket.reportes.dto.ReporteVentasDTO;
import com.ecomarket.reportes.entity.Reporte;
import com.ecomarket.reportes.entity.TipoReporte;
import com.ecomarket.reportes.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Reporte>>> listarReportes() {
        List<EntityModel<Reporte>> reportes = reporteService.listarReportes().stream()
                .map(r -> EntityModel.of(r,
                        linkTo(methodOn(ReporteController.class).obtenerReportePorId(r.getId())).withSelfRel(),
                        linkTo(methodOn(ReporteController.class).listarReportes()).withRel("reportes")))
                .toList();
        CollectionModel<EntityModel<Reporte>> collection = CollectionModel.of(reportes,
                linkTo(methodOn(ReporteController.class).listarReportes()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Reporte>> obtenerReportePorId(@PathVariable Long id) {
        Reporte reporte = reporteService.obtenerReportePorId(id);
        EntityModel<Reporte> model = EntityModel.of(reporte,
                linkTo(methodOn(ReporteController.class).obtenerReportePorId(id)).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarReportes()).withRel("reportes"),
                linkTo(methodOn(ReporteController.class).listarPorTipo(reporte.getTipo())).withRel("por-tipo"));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Reporte>> crearReporte(@Valid @RequestBody Reporte reporte) {
        Reporte creado = reporteService.crearReporte(reporte);
        EntityModel<Reporte> model = EntityModel.of(creado,
                linkTo(methodOn(ReporteController.class).obtenerReportePorId(creado.getId())).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarReportes()).withRel("reportes"));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<CollectionModel<EntityModel<Reporte>>> listarPorTipo(@PathVariable TipoReporte tipo) {
        List<EntityModel<Reporte>> reportes = reporteService.listarPorTipo(tipo).stream()
                .map(r -> EntityModel.of(r,
                        linkTo(methodOn(ReporteController.class).obtenerReportePorId(r.getId())).withSelfRel()))
                .toList();
        CollectionModel<EntityModel<Reporte>> collection = CollectionModel.of(reportes,
                linkTo(methodOn(ReporteController.class).listarPorTipo(tipo)).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarReportes()).withRel("reportes"));
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/tienda/{idTienda}")
    public ResponseEntity<CollectionModel<EntityModel<Reporte>>> listarPorTienda(@PathVariable Long idTienda) {
        List<EntityModel<Reporte>> reportes = reporteService.listarPorTienda(idTienda).stream()
                .map(r -> EntityModel.of(r,
                        linkTo(methodOn(ReporteController.class).obtenerReportePorId(r.getId())).withSelfRel()))
                .toList();
        CollectionModel<EntityModel<Reporte>> collection = CollectionModel.of(reportes,
                linkTo(methodOn(ReporteController.class).listarPorTienda(idTienda)).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarReportes()).withRel("reportes"));
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/ventas")
    public ResponseEntity<EntityModel<ReporteVentasDTO>> generarReporteVentas(
            @Valid @RequestBody ReporteFiltroRequestDTO filtro) {
        ReporteVentasDTO dto = reporteService.generarReporteVentas(filtro);
        EntityModel<ReporteVentasDTO> model = EntityModel.of(dto,
                linkTo(methodOn(ReporteController.class).generarReporteVentas(filtro)).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarReportes()).withRel("reportes"));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PostMapping("/inventario/{idTienda}")
    public ResponseEntity<EntityModel<ReporteInventarioDTO>> generarReporteInventario(
            @PathVariable Long idTienda) {
        ReporteInventarioDTO dto = reporteService.generarReporteInventario(idTienda);
        EntityModel<ReporteInventarioDTO> model = EntityModel.of(dto,
                linkTo(methodOn(ReporteController.class).generarReporteInventario(idTienda)).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarReportes()).withRel("reportes"));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PostMapping("/rendimiento")
    public ResponseEntity<EntityModel<ReporteRendimientoDTO>> generarReporteRendimiento(
            @Valid @RequestBody ReporteFiltroRequestDTO filtro) {
        ReporteRendimientoDTO dto = reporteService.generarReporteRendimiento(filtro);
        EntityModel<ReporteRendimientoDTO> model = EntityModel.of(dto,
                linkTo(methodOn(ReporteController.class).generarReporteRendimiento(filtro)).withSelfRel(),
                linkTo(methodOn(ReporteController.class).listarReportes()).withRel("reportes"));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }
}
