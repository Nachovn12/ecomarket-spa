package com.ecomarket.admin.controller;

import com.ecomarket.admin.dto.*;
import com.ecomarket.admin.model.EstadoTicket;
import com.ecomarket.admin.service.AdministracionSoporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Administracion y Soporte", description = "Endpoints administrativos: tiendas, tickets, metricas, alertas y respaldos")
public class AdministracionSoporteController {

    private final AdministracionSoporteService administracionSoporteService;

    @Operation(summary = "Crear una tienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tienda creada",
                    content = @Content(schema = @Schema(implementation = TiendaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping("/api/admin/tiendas")
    public ResponseEntity<TiendaResponseDTO> crearTienda(@Valid @RequestBody TiendaRequestDTO request) {
        TiendaResponseDTO response = administracionSoporteService.crearTienda(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todas las tiendas")
    @ApiResponse(responseCode = "200", description = "Listado de tiendas",
            content = @Content(schema = @Schema(implementation = TiendaResponseDTO.class)))
    @GetMapping("/api/admin/tiendas")
    public ResponseEntity<List<TiendaResponseDTO>> listarTiendas() {
        List<TiendaResponseDTO> tiendas = administracionSoporteService.listarTiendas();
        return ResponseEntity.ok(tiendas);
    }

    @Operation(summary = "Consultar una tienda por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tienda encontrada",
                    content = @Content(schema = @Schema(implementation = TiendaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada", content = @Content)
    })
    @GetMapping("/api/admin/tiendas/{idTienda}")
    public ResponseEntity<TiendaResponseDTO> consultarTienda(
            @Parameter(description = "ID de la tienda", example = "1", required = true) @PathVariable Long idTienda) {
        return ResponseEntity.ok(administracionSoporteService.consultarTienda(idTienda));
    }

    @Operation(summary = "Actualizar una tienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tienda actualizada",
                    content = @Content(schema = @Schema(implementation = TiendaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada", content = @Content)
    })
    @PutMapping("/api/admin/tiendas/{idTienda}")
    public ResponseEntity<TiendaResponseDTO> actualizarTienda(
            @Parameter(description = "ID de la tienda", example = "1", required = true) @PathVariable Long idTienda,
            @Valid @RequestBody TiendaRequestDTO request) {
        return ResponseEntity.ok(administracionSoporteService.actualizarTienda(idTienda, request));
    }

    @Operation(summary = "Eliminar (soft) una tienda")
    @ApiResponse(responseCode = "204", description = "Tienda desactivada", content = @Content)
    @ApiResponse(responseCode = "404", description = "Tienda no encontrada", content = @Content)
    @DeleteMapping("/api/admin/tiendas/{idTienda}")
    public ResponseEntity<Void> eliminarTienda(
            @Parameter(description = "ID de la tienda", example = "1", required = true) @PathVariable Long idTienda) {
        administracionSoporteService.eliminarTienda(idTienda);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asignar personal a una tienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asignacion creada",
                    content = @Content(schema = @Schema(implementation = AsignacionPersonalResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada", content = @Content)
    })
    @PostMapping("/api/admin/tiendas/{idTienda}/personal")
    public ResponseEntity<AsignacionPersonalResponseDTO> asignarPersonal(
            @Parameter(description = "ID de la tienda", example = "1", required = true) @PathVariable Long idTienda,
            @Valid @RequestBody AsignacionPersonalRequestDTO request) {
        request.setIdTienda(idTienda);
        AsignacionPersonalResponseDTO response = administracionSoporteService.asignarPersonal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar el personal asignado a una tienda")
    @ApiResponse(responseCode = "200", description = "Listado de personal",
            content = @Content(schema = @Schema(implementation = AsignacionPersonalResponseDTO.class)))
    @GetMapping("/api/admin/tiendas/{idTienda}/personal")
    public ResponseEntity<List<AsignacionPersonalResponseDTO>> listarPersonalPorTienda(
            @Parameter(description = "ID de la tienda", example = "1", required = true) @PathVariable Long idTienda) {
        List<AsignacionPersonalResponseDTO> personal = administracionSoporteService.listarPersonalPorTienda(idTienda);
        return ResponseEntity.ok(personal);
    }

    @Operation(summary = "Crear un ticket de soporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket creado",
                    content = @Content(schema = @Schema(implementation = TicketSoporteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping("/api/soporte/tickets")
    public ResponseEntity<TicketSoporteResponseDTO> crearTicketSoporte(
            @Valid @RequestBody TicketSoporteRequestDTO request) {
        TicketSoporteResponseDTO response = administracionSoporteService.crearTicketSoporte(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los tickets de soporte")
    @ApiResponse(responseCode = "200", description = "Listado de tickets",
            content = @Content(schema = @Schema(implementation = TicketSoporteResponseDTO.class)))
    @GetMapping("/api/soporte/tickets")
    public ResponseEntity<List<TicketSoporteResponseDTO>> listarTickets() {
        List<TicketSoporteResponseDTO> tickets = administracionSoporteService.listarTickets();
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Consultar un ticket de soporte por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket encontrado",
                    content = @Content(schema = @Schema(implementation = TicketSoporteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado", content = @Content)
    })
    @GetMapping("/api/soporte/tickets/{idTicket}")
    public ResponseEntity<TicketSoporteResponseDTO> consultarTicket(
            @Parameter(description = "ID del ticket", example = "1", required = true) @PathVariable Long idTicket) {
        return ResponseEntity.ok(administracionSoporteService.consultarTicket(idTicket));
    }

    @Operation(summary = "Actualizar el estado de un ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado",
                    content = @Content(schema = @Schema(implementation = TicketSoporteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Estado invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado", content = @Content)
    })
    @PatchMapping("/api/soporte/tickets/{idTicket}/estado")
    public ResponseEntity<TicketSoporteResponseDTO> actualizarEstadoTicket(
            @Parameter(description = "ID del ticket", example = "1", required = true) @PathVariable Long idTicket,
            @Parameter(description = "Nuevo estado del ticket", example = "EN_PROCESO", required = true)
            @RequestParam EstadoTicket estado) {
        return ResponseEntity.ok(administracionSoporteService.actualizarEstadoTicket(idTicket, estado));
    }

    @Operation(summary = "Eliminar (soft) un ticket de soporte")
    @ApiResponse(responseCode = "204", description = "Ticket desactivado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Ticket no encontrado", content = @Content)
    @DeleteMapping("/api/soporte/tickets/{idTicket}")
    public ResponseEntity<Void> eliminarTicket(
            @Parameter(description = "ID del ticket", example = "1", required = true) @PathVariable Long idTicket) {
        administracionSoporteService.eliminarTicket(idTicket);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Agregar una respuesta a un ticket",
            description = "Anade un comentario/respuesta al hilo del ticket y opcionalmente lo resuelve.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Respuesta registrada",
                    content = @Content(schema = @Schema(implementation = RespuestaSoporteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado", content = @Content)
    })
    @PostMapping("/api/soporte/tickets/{idTicket}/respuestas")
    public ResponseEntity<RespuestaSoporteResponseDTO> responderTicket(
            @Parameter(description = "ID del ticket", example = "1", required = true) @PathVariable Long idTicket,
            @Valid @RequestBody RespuestaSoporteRequestDTO request) {
        RespuestaSoporteResponseDTO response = administracionSoporteService.responderTicket(idTicket, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar las respuestas de un ticket")
    @ApiResponse(responseCode = "200", description = "Listado de respuestas",
            content = @Content(schema = @Schema(implementation = RespuestaSoporteResponseDTO.class)))
    @GetMapping("/api/soporte/tickets/{idTicket}/respuestas")
    public ResponseEntity<List<RespuestaSoporteResponseDTO>> listarRespuestasTicket(
            @Parameter(description = "ID del ticket", example = "1", required = true) @PathVariable Long idTicket) {
        List<RespuestaSoporteResponseDTO> respuestas = administracionSoporteService.listarRespuestasTicket(idTicket);
        return ResponseEntity.ok(respuestas);
    }

    @Operation(summary = "Registrar una metrica de sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Metrica registrada",
                    content = @Content(schema = @Schema(implementation = MetricaSistemaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping("/api/admin/metricas")
    public ResponseEntity<MetricaSistemaResponseDTO> registrarMetrica(
            @Valid @RequestBody MetricaSistemaRequestDTO request) {
        MetricaSistemaResponseDTO response = administracionSoporteService.registrarMetrica(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar las ultimas metricas del sistema")
    @ApiResponse(responseCode = "200", description = "Listado de metricas",
            content = @Content(schema = @Schema(implementation = MetricaSistemaResponseDTO.class)))
    @GetMapping("/api/admin/metricas")
    public ResponseEntity<List<MetricaSistemaResponseDTO>> listarMetricas() {
        List<MetricaSistemaResponseDTO> metricas = administracionSoporteService.listarMetricas();
        return ResponseEntity.ok(metricas);
    }

    @Operation(summary = "Generar una alerta del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alerta generada",
                    content = @Content(schema = @Schema(implementation = AlertaSistemaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping("/api/admin/alertas")
    public ResponseEntity<AlertaSistemaResponseDTO> generarAlerta(
            @Valid @RequestBody AlertaSistemaRequestDTO request) {
        AlertaSistemaResponseDTO response = administracionSoporteService.registrarAlerta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar alertas activas del sistema")
    @ApiResponse(responseCode = "200", description = "Alertas activas",
            content = @Content(schema = @Schema(implementation = AlertaSistemaResponseDTO.class)))
    @GetMapping("/api/admin/alertas/activas")
    public ResponseEntity<List<AlertaSistemaResponseDTO>> listarAlertasActivas() {
        List<AlertaSistemaResponseDTO> alertas = administracionSoporteService.listarAlertasActivas();
        return ResponseEntity.ok(alertas);
    }

    @Operation(summary = "Resolver una alerta del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerta resuelta",
                    content = @Content(schema = @Schema(implementation = AlertaSistemaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Alerta no encontrada", content = @Content)
    })
    @PatchMapping("/api/admin/alertas/{idAlerta}/resolver")
    public ResponseEntity<AlertaSistemaResponseDTO> resolverAlerta(
            @Parameter(description = "ID de la alerta", example = "1", required = true) @PathVariable Long idAlerta) {
        return ResponseEntity.ok(administracionSoporteService.resolverAlerta(idAlerta));
    }

    @Operation(summary = "Programar un respaldo de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Respaldo programado",
                    content = @Content(schema = @Schema(implementation = RespaldoDatosResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content)
    })
    @PostMapping("/api/admin/respaldos")
    public ResponseEntity<RespaldoDatosResponseDTO> programarRespaldo(
            @Valid @RequestBody RespaldoDatosRequestDTO request) {
        RespaldoDatosResponseDTO response = administracionSoporteService.programarRespaldo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los respaldos")
    @ApiResponse(responseCode = "200", description = "Listado de respaldos",
            content = @Content(schema = @Schema(implementation = RespaldoDatosResponseDTO.class)))
    @GetMapping("/api/admin/respaldos")
    public ResponseEntity<List<RespaldoDatosResponseDTO>> listarRespaldos() {
        List<RespaldoDatosResponseDTO> respaldos = administracionSoporteService.listarRespaldos();
        return ResponseEntity.ok(respaldos);
    }

    @Operation(summary = "Ejecutar un respaldo programado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respaldo ejecutado",
                    content = @Content(schema = @Schema(implementation = RespaldoDatosResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Respaldo no encontrado", content = @Content)
    })
    @PatchMapping("/api/admin/respaldos/{idRespaldo}/ejecutar")
    public ResponseEntity<RespaldoDatosResponseDTO> ejecutarRespaldo(
            @Parameter(description = "ID del respaldo", example = "1", required = true) @PathVariable Long idRespaldo) {
        return ResponseEntity.ok(administracionSoporteService.ejecutarRespaldo(idRespaldo));
    }

    @Operation(summary = "Restaurar datos desde un respaldo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restauracion ejecutada",
                    content = @Content(schema = @Schema(implementation = RespaldoDatosResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Respaldo no encontrado", content = @Content)
    })
    @PatchMapping("/api/admin/respaldos/{idRespaldo}/restaurar")
    public ResponseEntity<RespaldoDatosResponseDTO> restaurarRespaldo(
            @Parameter(description = "ID del respaldo", example = "1", required = true) @PathVariable Long idRespaldo) {
        return ResponseEntity.ok(administracionSoporteService.restaurarRespaldo(idRespaldo));
    }

    @Operation(summary = "Eliminar un respaldo")
    @ApiResponse(responseCode = "204", description = "Respaldo eliminado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Respaldo no encontrado", content = @Content)
    @DeleteMapping("/api/admin/respaldos/{idRespaldo}")
    public ResponseEntity<Void> eliminarRespaldo(
            @Parameter(description = "ID del respaldo", example = "1", required = true) @PathVariable Long idRespaldo) {
        administracionSoporteService.eliminarRespaldo(idRespaldo);
        return ResponseEntity.noContent().build();
    }
}