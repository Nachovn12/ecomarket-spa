package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.dto.IndicadorKPIResponseDTO;
import com.ecomarket.reportes.entity.IndicadorKPI;
import com.ecomarket.reportes.entity.TipoKPI;
import com.ecomarket.reportes.service.ReporteService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/kpis")
public class KpiController {

    private final ReporteService reporteService;

    public KpiController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<IndicadorKPIResponseDTO>>> listarKPIs() {
        List<EntityModel<IndicadorKPIResponseDTO>> kpis = reporteService.listarKPIs().stream()
                .map(k -> {
                    IndicadorKPIResponseDTO dto = reporteService.toDTO(k);
                    return EntityModel.of(dto,
                            linkTo(methodOn(KpiController.class).obtenerKPIPorId(dto.getId())).withSelfRel(),
                            linkTo(methodOn(KpiController.class).listarKPIs()).withRel("kpis"));
                })
                .toList();
        CollectionModel<EntityModel<IndicadorKPIResponseDTO>> collection = CollectionModel.of(kpis,
                linkTo(methodOn(KpiController.class).listarKPIs()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<IndicadorKPIResponseDTO>> obtenerKPIPorId(@PathVariable Long id) {
        IndicadorKPI kpi = reporteService.obtenerKPIPorId(id);
        IndicadorKPIResponseDTO dto = reporteService.toDTO(kpi);
        EntityModel<IndicadorKPIResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(KpiController.class).obtenerKPIPorId(id)).withSelfRel(),
                linkTo(methodOn(KpiController.class).listarKPIs()).withRel("kpis"),
                linkTo(methodOn(KpiController.class).listarPorTipo(kpi.getTipo())).withRel("por-tipo"));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<EntityModel<IndicadorKPIResponseDTO>> crearKPI(@Valid @RequestBody IndicadorKPI kpi) {
        IndicadorKPI creado = reporteService.crearKPI(kpi);
        IndicadorKPIResponseDTO dto = reporteService.toDTO(creado);
        EntityModel<IndicadorKPIResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(KpiController.class).obtenerKPIPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(KpiController.class).listarKPIs()).withRel("kpis"));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarKPI(@PathVariable Long id) {
        reporteService.eliminarKPI(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<CollectionModel<EntityModel<IndicadorKPIResponseDTO>>> listarPorTipo(
            @PathVariable TipoKPI tipo) {
        List<EntityModel<IndicadorKPIResponseDTO>> kpis = reporteService.listarKPIsPorTipo(tipo).stream()
                .map(k -> {
                    IndicadorKPIResponseDTO dto = reporteService.toDTO(k);
                    return EntityModel.of(dto,
                            linkTo(methodOn(KpiController.class).obtenerKPIPorId(dto.getId())).withSelfRel());
                })
                .toList();
        CollectionModel<EntityModel<IndicadorKPIResponseDTO>> collection = CollectionModel.of(kpis,
                linkTo(methodOn(KpiController.class).listarPorTipo(tipo)).withSelfRel(),
                linkTo(methodOn(KpiController.class).listarKPIs()).withRel("kpis"));
        return ResponseEntity.ok(collection);
    }
}
