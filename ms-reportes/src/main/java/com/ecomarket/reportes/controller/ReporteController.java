package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.dto.ReporteFiltroRequestDTO;
import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.dto.ReporteRendimientoDTO;
import com.ecomarket.reportes.dto.ReporteVentasDTO;
import com.ecomarket.reportes.model.Reporte;
import com.ecomarket.reportes.model.TipoReporte;
import com.ecomarket.reportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes", description = "Generacion y consulta de reportes de ventas, inventario y rendimiento")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "Listar todos los reportes registrados")
    @ApiResponse(responseCode = "200", description = "Listado de reportes",
            content = @Content(schema = @Schema(implementation = Reporte.class)))
    @GetMapping
    public ResponseEntity<List<Reporte>> listarReportes() {
        return ResponseEntity.ok(reporteService.listarReportes());
    }

    @Operation(summary = "Obtener un reporte por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte encontrado",
                    content = @Content(schema = @Schema(implementation = Reporte.class))),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerReportePorId(
            @Parameter(description = "ID del reporte", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(reporteService.obtenerReportePorId(id));
    }

    @Operation(summary = "Registrar un reporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte registrado",
                    content = @Content(schema = @Schema(implementation = Reporte.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Reporte> crearReporte(@Valid @RequestBody Reporte reporte) {
        Reporte creado = reporteService.crearReporte(reporte);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Eliminar un reporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reporte eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(
            @Parameter(description = "ID del reporte", example = "1", required = true) @PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar reportes por tipo")
    @ApiResponse(responseCode = "200", description = "Reportes del tipo indicado",
            content = @Content(schema = @Schema(implementation = Reporte.class)))
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reporte>> listarPorTipo(
            @Parameter(description = "Tipo de reporte", example = "VENTAS", required = true) @PathVariable TipoReporte tipo) {
        return ResponseEntity.ok(reporteService.listarPorTipo(tipo));
    }

    @Operation(summary = "Listar reportes por tienda")
    @ApiResponse(responseCode = "200", description = "Reportes de la tienda",
            content = @Content(schema = @Schema(implementation = Reporte.class)))
    @GetMapping("/tienda/{idTienda}")
    public ResponseEntity<List<Reporte>> listarPorTienda(
            @Parameter(description = "ID de la tienda", example = "1", required = true) @PathVariable Long idTienda) {
        return ResponseEntity.ok(reporteService.listarPorTienda(idTienda));
    }

    @Operation(summary = "Generar reporte de ventas",
            description = "Genera un reporte de ventas con el filtro indicado (rango de fechas, tienda, etc.).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte de ventas generado",
                    content = @Content(schema = @Schema(implementation = ReporteVentasDTO.class))),
            @ApiResponse(responseCode = "400", description = "Filtro invalido", content = @Content)
    })
    @PostMapping("/ventas")
    public ResponseEntity<ReporteVentasDTO> generarReporteVentas(
            @Valid @RequestBody ReporteFiltroRequestDTO filtro) {
        ReporteVentasDTO dto = reporteService.generarReporteVentas(filtro);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Generar reporte de inventario para una tienda",
            description = "Genera un reporte con el estado actual del inventario de la tienda indicada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte de inventario generado",
                    content = @Content(schema = @Schema(implementation = ReporteInventarioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada", content = @Content)
    })
    @PostMapping("/inventario/{idTienda}")
    public ResponseEntity<ReporteInventarioDTO> generarReporteInventario(
            @Parameter(description = "ID de la tienda", example = "1", required = true) @PathVariable Long idTienda) {
        ReporteInventarioDTO dto = reporteService.generarReporteInventario(idTienda);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Generar reporte de rendimiento de tienda",
            description = "Calcula indicadores de rendimiento (ventas, conversion, desempeno) segun el filtro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte de rendimiento generado",
                    content = @Content(schema = @Schema(implementation = ReporteRendimientoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Filtro invalido", content = @Content)
    })
    @PostMapping("/rendimiento")
    public ResponseEntity<ReporteRendimientoDTO> generarReporteRendimiento(
            @Valid @RequestBody ReporteFiltroRequestDTO filtro) {
        ReporteRendimientoDTO dto = reporteService.generarReporteRendimiento(filtro);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
