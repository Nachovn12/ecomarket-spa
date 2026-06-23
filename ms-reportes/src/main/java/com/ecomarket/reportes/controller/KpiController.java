package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.dto.IndicadorKPIResponseDTO;
import com.ecomarket.reportes.model.IndicadorKPI;
import com.ecomarket.reportes.model.TipoKPI;
import com.ecomarket.reportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kpis")
@Tag(name = "KPIs", description = "Indicadores clave de desempeno (calculados o registrados)")
public class KpiController {

    private final ReporteService reporteService;

    public KpiController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "Listar todos los KPIs")
    @ApiResponse(responseCode = "200", description = "Listado de KPIs",
            content = @Content(schema = @Schema(implementation = IndicadorKPIResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<IndicadorKPIResponseDTO>> listarKPIs() {
        List<IndicadorKPIResponseDTO> kpis = reporteService.listarKPIs().stream()
                .map(reporteService::toDTO)
                .toList();
        return ResponseEntity.ok(kpis);
    }

    @Operation(summary = "Obtener un KPI por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "KPI encontrado",
                    content = @Content(schema = @Schema(implementation = IndicadorKPIResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "KPI no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<IndicadorKPIResponseDTO> obtenerKPIPorId(
            @Parameter(description = "ID del KPI", example = "1", required = true) @PathVariable Long id) {
        IndicadorKPI kpi = reporteService.obtenerKPIPorId(id);
        return ResponseEntity.ok(reporteService.toDTO(kpi));
    }

    @Operation(summary = "Registrar un nuevo KPI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "KPI registrado",
                    content = @Content(schema = @Schema(implementation = IndicadorKPIResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<IndicadorKPIResponseDTO> crearKPI(@Valid @RequestBody IndicadorKPI kpi) {
        IndicadorKPI creado = reporteService.crearKPI(kpi);
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.toDTO(creado));
    }

    @Operation(summary = "Eliminar un KPI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "KPI eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "KPI no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarKPI(
            @Parameter(description = "ID del KPI", example = "1", required = true) @PathVariable Long id) {
        reporteService.eliminarKPI(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar KPIs por tipo",
            description = "Filtra los KPIs por su tipo (ej: VENTAS, INVENTARIO, RENDIMIENTO).")
    @ApiResponse(responseCode = "200", description = "Listado filtrado por tipo",
            content = @Content(schema = @Schema(implementation = IndicadorKPIResponseDTO.class)))
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<IndicadorKPIResponseDTO>> listarPorTipo(
            @Parameter(description = "Tipo de KPI", example = "VENTAS", required = true) @PathVariable TipoKPI tipo) {
        List<IndicadorKPIResponseDTO> kpis = reporteService.listarKPIsPorTipo(tipo).stream()
                .map(reporteService::toDTO)
                .toList();
        return ResponseEntity.ok(kpis);
    }
}
