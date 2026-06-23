package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.ProductoRequestDTO;
import com.ecomarket.catalogo.dto.ProductoResponseDTO;
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
 * Controller de productos del catalogo.
 * Patron CSR: recibe la peticion HTTP, delega al service y retorna ResponseEntity.
 */
@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones CRUD y busquedas sobre productos del catalogo ecologico")
public class ProductoController {

    @Autowired
    private CatalogoService catalogoService;

    @Operation(
            summary = "Crear un nuevo producto",
            description = "Registra un producto en el catalogo. El SKU debe ser unico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "SKU duplicado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO creado = catalogoService.crearProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(
            summary = "Listar todos los productos",
            description = "Retorna la coleccion completa de productos del catalogo."
    )
    @ApiResponse(responseCode = "200", description = "Listado de productos",
            content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        List<ProductoResponseDTO> productos = catalogoService.obtenerTodosProductos();
        return ResponseEntity.ok(productos);
    }

    @Operation(
            summary = "Obtener un producto por ID",
            description = "Busca un producto especifico por su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarPorId(
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long id) {
        ProductoResponseDTO producto = catalogoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    @Operation(
            summary = "Actualizar un producto existente",
            description = "Modifica los datos de un producto. Valida unicidad de SKU solo si cambia."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado",
                    content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto con SKU duplicado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto) {
        ProductoResponseDTO actualizado = catalogoService.actualizarProducto(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(
            summary = "Eliminar un producto",
            description = "Borra el producto identificado por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        catalogoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar productos por criterios",
            description = "Permite buscar productos por palabra clave, categoria, o rango de precios. " +
                    "Si no se envia ningun parametro retorna todos."
    )
    @ApiResponse(responseCode = "200", description = "Resultados de la busqueda",
            content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class)))
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> buscar(
            @Parameter(description = "Palabra clave a buscar en nombre o descripcion", example = "biodegradable")
            @RequestParam(required = false) String palabraClave,
            @Parameter(description = "ID de la categoria a filtrar", example = "2")
            @RequestParam(required = false) Long idCategoria,
            @Parameter(description = "Precio minimo (inclusive)", example = "1000")
            @RequestParam(required = false) Double precioMinimo,
            @Parameter(description = "Precio maximo (inclusive)", example = "5000")
            @RequestParam(required = false) Double precioMaximo) {

        List<ProductoResponseDTO> resultados;
        if (palabraClave != null) {
            resultados = catalogoService.buscarPorPalabraClave(palabraClave);
        } else if (idCategoria != null) {
            resultados = catalogoService.buscarPorCategoria(idCategoria);
        } else if (precioMinimo != null && precioMaximo != null) {
            resultados = catalogoService.buscarPorPrecio(precioMinimo, precioMaximo);
        } else {
            resultados = catalogoService.obtenerTodosProductos();
        }

        return ResponseEntity.ok(resultados);
    }

    @Operation(
            summary = "Buscar productos ecologicos",
            description = "Retorna productos que contengan el atributo ecologico indicado en su descripcion ecologica."
    )
    @ApiResponse(responseCode = "200", description = "Productos ecologicos encontrados",
            content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class)))
    @GetMapping("/ecologicos")
    public ResponseEntity<List<ProductoResponseDTO>> buscarEcologicos(
            @Parameter(description = "Atributo ecologico a buscar", example = "biodegradable")
            @RequestParam(defaultValue = "biodegradable") String atributoEcologico) {
        List<ProductoResponseDTO> resultados = catalogoService.buscarEcologicos(atributoEcologico);
        return ResponseEntity.ok(resultados);
    }
}