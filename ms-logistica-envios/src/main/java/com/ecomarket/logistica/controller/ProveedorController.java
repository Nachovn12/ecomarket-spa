package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.ProveedorDTO;
import com.ecomarket.logistica.model.Proveedor;
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
@RequestMapping("/api/envios/proveedores")
@Tag(name = "Proveedores Logisticos", description = "Administracion de proveedores de transporte y logistica")
public class ProveedorController {

    private final LogisticaService logisticaService;

    public ProveedorController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    @Operation(summary = "Crear un proveedor logistico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado",
                    content = @Content(schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto al crear el proveedor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Proveedor> crear(@Valid @RequestBody ProveedorDTO dto) {
        Proveedor creado = logisticaService.crearProveedor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Listar todos los proveedores logisticos")
    @ApiResponse(responseCode = "200", description = "Listado de proveedores",
            content = @Content(schema = @Schema(implementation = Proveedor.class)))
    @GetMapping
    public ResponseEntity<List<Proveedor>> obtenerTodos() {
        return ResponseEntity.ok(logisticaService.obtenerProveedores());
    }

    @Operation(summary = "Obtener un proveedor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado",
                    content = @Content(schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerPorId(
            @Parameter(description = "ID del proveedor", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(logisticaService.obtenerProveedorPorId(id));
    }

    @Operation(summary = "Actualizar un proveedor logistico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado",
                    content = @Content(schema = @Schema(implementation = Proveedor.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(
            @Parameter(description = "ID del proveedor", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody ProveedorDTO dto) {
        return ResponseEntity.ok(logisticaService.actualizarProveedor(id, dto));
    }

    @Operation(summary = "Activar un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor activado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(
            @Parameter(description = "ID del proveedor", example = "1", required = true) @PathVariable Long id) {
        logisticaService.activarProveedor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desactivar un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor desactivado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado", content = @Content)
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del proveedor", example = "1", required = true) @PathVariable Long id) {
        logisticaService.desactivarProveedor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar proveedores activos")
    @ApiResponse(responseCode = "200", description = "Proveedores activos",
            content = @Content(schema = @Schema(implementation = Proveedor.class)))
    @GetMapping("/activos")
    public ResponseEntity<List<Proveedor>> obtenerActivos() {
        return ResponseEntity.ok(logisticaService.obtenerProveedoresActivos());
    }

    @Operation(summary = "Buscar proveedores por tipo y cobertura")
    @ApiResponse(responseCode = "200", description = "Resultados de la busqueda",
            content = @Content(schema = @Schema(implementation = Proveedor.class)))
    @GetMapping("/buscar")
    public ResponseEntity<List<Proveedor>> buscar(
            @Parameter(description = "Tipo de proveedor", example = "TRANSPORTE", required = true)
            @RequestParam String tipoProveedor,
            @Parameter(description = "Cobertura geografica", example = "REGIONAL", required = true)
            @RequestParam String cobertura) {
        return ResponseEntity.ok(logisticaService.buscarProveedores(tipoProveedor, cobertura));
    }
}
