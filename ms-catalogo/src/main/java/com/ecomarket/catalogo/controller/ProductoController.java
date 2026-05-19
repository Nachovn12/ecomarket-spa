package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.model.Producto;
import com.ecomarket.catalogo.service.CatalogoService;
import com.ecomarket.catalogo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private CatalogoService catalogoService;

    @PostMapping
    public ResponseEntity<EntityModel<Producto>> crear(@Valid @RequestBody Producto producto) {
        Producto guardado = catalogoService.guardarProducto(producto);
        return new ResponseEntity<>(ensamblarResource(guardado), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listarTodos() {
        List<Producto> productos = catalogoService.obtenerTodosProductos();
        return ResponseEntity.ok(ensamblarCollection(productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> buscarPorId(@PathVariable Long id) {
        Producto producto = catalogoService.obtenerProductoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return ResponseEntity.ok(ensamblarResource(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizar(@PathVariable Long id,
            @Valid @RequestBody Producto producto) {
        catalogoService.obtenerProductoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        producto.setIdProducto(id);
        Producto actualizado = catalogoService.guardarProducto(producto);
        return ResponseEntity.ok(ensamblarResource(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        catalogoService.obtenerProductoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        catalogoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> buscar(
            @RequestParam(required = false) String palabraClave,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Double precioMinimo,
            @RequestParam(required = false) Double precioMaximo) {

        List<Producto> resultados;

        if (palabraClave != null) {
            resultados = catalogoService.buscarPorPalabraClave(palabraClave);
        } else if (idCategoria != null) {
            resultados = catalogoService.buscarPorCategoria(idCategoria);
        } else if (precioMinimo != null && precioMaximo != null) {
            resultados = catalogoService.buscarPorPrecio(precioMinimo, precioMaximo);
        } else {
            resultados = catalogoService.obtenerTodosProductos();
        }

        return ResponseEntity.ok(ensamblarCollection(resultados));
    }

    @GetMapping("/ecologicos")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> buscarEcologicos(
            @RequestParam(defaultValue = "biodegradable") String atributoEcologico) {
        List<Producto> resultados = catalogoService.buscarEcologicos(atributoEcologico);
        return ResponseEntity.ok(ensamblarCollection(resultados));
    }

    private EntityModel<Producto> ensamblarResource(Producto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).buscarPorId(producto.getIdProducto())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarTodos()).withRel("productos"));
    }

    private CollectionModel<EntityModel<Producto>> ensamblarCollection(List<Producto> productos) {
        List<EntityModel<Producto>> productosResource = productos.stream()
                .map(this::ensamblarResource)
                .collect(Collectors.toList());
        return CollectionModel.of(productosResource,
                linkTo(methodOn(ProductoController.class).listarTodos()).withSelfRel());
    }
}