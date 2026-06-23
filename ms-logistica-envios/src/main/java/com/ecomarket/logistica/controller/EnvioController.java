package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.CambioEstadoRequestDTO;
import com.ecomarket.logistica.dto.EnvioDTO;
import com.ecomarket.logistica.dto.IncidenciaRequestDTO;
import com.ecomarket.logistica.model.Envio;
import com.ecomarket.logistica.model.SeguimientoEnvio;
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
@RequestMapping("/api/envios")
@Tag(name = "Envios", description = "Gestion de envios, seguimiento, cambios de estado e incidencias")
public class EnvioController {

    private final LogisticaService logisticaService;

    public EnvioController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    @Operation(summary = "Crear un nuevo envio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Envio creado",
                    content = @Content(schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Envio> crear(@Valid @RequestBody EnvioDTO dto) {
        Envio creado = logisticaService.crearEnvio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Listar todos los envios")
    @ApiResponse(responseCode = "200", description = "Listado de envios",
            content = @Content(schema = @Schema(implementation = Envio.class)))
    @GetMapping
    public ResponseEntity<List<Envio>> obtenerTodos() {
        return ResponseEntity.ok(logisticaService.obtenerEnvios());
    }

    @Operation(summary = "Obtener un envio por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envio encontrado",
                    content = @Content(schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "Envio no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerPorId(
            @Parameter(description = "ID del envio", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(logisticaService.obtenerEnvioPorId(id));
    }

    @Operation(summary = "Actualizar un envio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envio actualizado",
                    content = @Content(schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Envio no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Envio> actualizar(
            @Parameter(description = "ID del envio", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody EnvioDTO dto) {
        return ResponseEntity.ok(logisticaService.actualizarEnvio(id, dto));
    }

    @Operation(summary = "Eliminar un envio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Envio eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Envio no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del envio", example = "1", required = true) @PathVariable Long id) {
        logisticaService.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener envios por pedido")
    @ApiResponse(responseCode = "200", description = "Envios del pedido",
            content = @Content(schema = @Schema(implementation = Envio.class)))
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<Envio>> obtenerPorPedido(
            @Parameter(description = "ID del pedido", example = "1", required = true) @PathVariable Long idPedido) {
        return ResponseEntity.ok(logisticaService.obtenerEnviosPorPedido(idPedido));
    }

    @Operation(summary = "Cambiar el estado de un envio",
            description = "Valida la transicion legal entre estados (PENDIENTE, EN_TRANSITO, ENTREGADO, DEVUELTO, CANCELADO).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado",
                    content = @Content(schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "400", description = "Estado invalido o transicion no permitida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Envio no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto con el estado actual", content = @Content)
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Envio> cambiarEstado(
            @Parameter(description = "ID del envio", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequestDTO dto) {
        return ResponseEntity.ok(logisticaService.cambiarEstadoEnvio(id, dto));
    }

    @Operation(summary = "Registrar una incidencia en un envio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidencia registrada",
                    content = @Content(schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "Envio no encontrado", content = @Content)
    })
    @PatchMapping("/{id}/incidencia")
    public ResponseEntity<Envio> registrarIncidencia(
            @Parameter(description = "ID del envio", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody IncidenciaRequestDTO dto) {
        return ResponseEntity.ok(logisticaService.registrarIncidencia(id, dto));
    }

    @Operation(summary = "Obtener el seguimiento de un envio")
    @ApiResponse(responseCode = "200", description = "Trazabilidad del envio",
            content = @Content(schema = @Schema(implementation = SeguimientoEnvio.class)))
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<List<SeguimientoEnvio>> obtenerSeguimiento(
            @Parameter(description = "ID del envio", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(logisticaService.obtenerSeguimiento(id));
    }
}
