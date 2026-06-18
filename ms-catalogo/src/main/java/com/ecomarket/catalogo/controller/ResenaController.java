package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.ResenaRequestDTO;
import com.ecomarket.catalogo.dto.ResenaResponseDTO;
import com.ecomarket.catalogo.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller de resenas del catalogo.
 * CRUD completo: GET /resenas, GET /resenas/{id}, POST /resenas, DELETE /resenas/{id}.
 * Patron CSR: delega toda la logica al service.
 */
@RestController
@RequestMapping("/api/resenas")
@Tag(name = "Resenas", description = "Operaciones sobre resenas y calificaciones de productos")
public class ResenaController {

    @Autowired
    private CatalogoService catalogoService;

    @Operation(
            summary = "Crear una nueva resena",
            description = "Registra una resena de un producto por parte de un cliente. Calificacion entre 1 y 5."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resena creada",
                    content = @Content(schema = @Schema(implementation = ResenaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o calificacion fuera de rango",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ResenaResponseDTO> crearResena(@Valid @RequestBody ResenaRequestDTO dto) {
        ResenaResponseDTO creada = catalogoService.crearResena(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(
            summary = "Listar todas las resenas",
            description = "Retorna la coleccion de resenas registradas."
    )
    @ApiResponse(responseCode = "200", description = "Listado de resenas",
            content = @Content(schema = @Schema(implementation = ResenaResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<ResenaResponseDTO>> listarResenas() {
        List<ResenaResponseDTO> resenas = catalogoService.obtenerTodasResenas();
        return ResponseEntity.ok(resenas);
    }

    @Operation(
            summary = "Obtener una resena por ID",
            description = "Busca una resena especifica por su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resena encontrada",
                    content = @Content(schema = @Schema(implementation = ResenaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Resena no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResenaResponseDTO> obtenerResenaPorId(
            @Parameter(description = "ID de la resena", example = "1", required = true)
            @PathVariable Long id) {
        ResenaResponseDTO resena = catalogoService.obtenerResenaPorId(id);
        return ResponseEntity.ok(resena);
    }

    @Operation(
            summary = "Eliminar una resena",
            description = "Borra la resena identificada por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resena eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Resena no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(
            @Parameter(description = "ID de la resena", example = "1", required = true)
            @PathVariable Long id) {
        catalogoService.eliminarResena(id);
        return ResponseEntity.noContent().build();
    }
}