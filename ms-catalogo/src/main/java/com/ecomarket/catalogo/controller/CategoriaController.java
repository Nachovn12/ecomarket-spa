package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.CategoriaRequestDTO;
import com.ecomarket.catalogo.dto.CategoriaResponseDTO;
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
 * Controller de categorias del catalogo.
 * Patron CSR: recibe peticion HTTP, delega al service, retorna ResponseEntity con DTO.
 */
@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Operaciones CRUD sobre las categorias del catalogo de productos ecologicos")
public class CategoriaController {

    @Autowired
    private CatalogoService catalogoService;

    @Operation(
            summary = "Crear una nueva categoria",
            description = "Registra una categoria en el catalogo. Valida nombre unico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria creada exitosamente",
                    content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto: nombre de categoria duplicado",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO creada = catalogoService.crearCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(
            summary = "Listar todas las categorias",
            description = "Retorna la coleccion de categorias registradas."
    )
    @ApiResponse(responseCode = "200", description = "Listado de categorias",
            content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        List<CategoriaResponseDTO> categorias = catalogoService.obtenerTodasCategorias();
        return ResponseEntity.ok(categorias);
    }

    @Operation(
            summary = "Obtener una categoria por ID",
            description = "Busca una categoria especifica por su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada",
                    content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerCategoriaPorId(
            @Parameter(description = "ID de la categoria a buscar", example = "1", required = true)
            @PathVariable Long id) {
        CategoriaResponseDTO categoria = catalogoService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @Operation(
            summary = "Actualizar una categoria existente",
            description = "Modifica los datos de una categoria identificada por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria actualizada",
                    content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto con el nuevo nombre", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(
            @Parameter(description = "ID de la categoria a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO actualizada = catalogoService.actualizarCategoria(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(
            summary = "Eliminar una categoria",
            description = "Borra la categoria identificada por su ID. Falla si tiene productos asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID de la categoria a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        catalogoService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}