package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.CambioEstadoRutaRequestDTO;
import com.ecomarket.logistica.dto.RutaEntregaDTO;
import com.ecomarket.logistica.model.RutaEntrega;
import com.ecomarket.logistica.service.LogisticaService;
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
@RequestMapping("/api/rutas")
@Tag(name = "Rutas de Entrega", description = "Planificacion y trazabilidad de rutas de entrega")
public class RutaEntregaController {

    private final LogisticaService logisticaService;

    public RutaEntregaController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    @Operation(summary = "Crear una nueva ruta de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ruta creada",
                    content = @Content(schema = @Schema(implementation = RutaEntrega.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RutaEntrega> crear(@Valid @RequestBody RutaEntregaDTO dto) {
        RutaEntrega creada = logisticaService.crearRuta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Listar todas las rutas de entrega")
    @ApiResponse(responseCode = "200", description = "Listado de rutas",
            content = @Content(schema = @Schema(implementation = RutaEntrega.class)))
    @GetMapping
    public ResponseEntity<List<RutaEntrega>> obtenerTodas() {
        return ResponseEntity.ok(logisticaService.obtenerRutas());
    }

    @Operation(summary = "Obtener una ruta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta encontrada",
                    content = @Content(schema = @Schema(implementation = RutaEntrega.class))),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RutaEntrega> obtenerPorId(
            @Parameter(description = "ID de la ruta", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(logisticaService.obtenerRutaPorId(id));
    }

    @Operation(summary = "Actualizar una ruta de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ruta actualizada",
                    content = @Content(schema = @Schema(implementation = RutaEntrega.class))),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RutaEntrega> actualizar(
            @Parameter(description = "ID de la ruta", example = "1", required = true) @PathVariable Long id,
            @RequestBody RutaEntregaDTO dto) {
        return ResponseEntity.ok(logisticaService.actualizarRuta(id, dto));
    }

    @Operation(summary = "Eliminar una ruta de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ruta eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la ruta", example = "1", required = true) @PathVariable Long id) {
        logisticaService.eliminarRuta(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar el estado de una ruta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado",
                    content = @Content(schema = @Schema(implementation = RutaEntrega.class))),
            @ApiResponse(responseCode = "400", description = "Estado invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ruta no encontrada", content = @Content)
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<RutaEntrega> cambiarEstado(
            @Parameter(description = "ID de la ruta", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRutaRequestDTO request) {
        return ResponseEntity.ok(logisticaService.cambiarEstadoRuta(id, request.getEstado()));
    }
}
