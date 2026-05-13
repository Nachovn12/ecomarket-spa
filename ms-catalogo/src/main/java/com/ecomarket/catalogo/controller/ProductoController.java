package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.model.Producto;
import com.ecomarket.catalogo.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private CatalogoService catalogoService;

    // Crear un producto (HU-49)
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return catalogoService.guardar(producto);
    }

    // Listar todos los productos (HU-49 - Read)
    @GetMapping
    public List<Producto> listarTodos() {
        return catalogoService.obtenerTodos();
    }

    // Buscar un producto por ID (HU-49 - Read)
    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable Long id) {
        return catalogoService.obtenerPorId(id);
    }

    // Actualizar un producto (HU-49 - Update)
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return catalogoService.actualizar(id, producto);
    }

    // Eliminar un producto (HU-49 - Delete)
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        catalogoService.eliminar(id);
    }

    // Buscar productos ecológicos con HATEOAS (HU-6)
    @GetMapping("/ecologicos")
    public CollectionModel<EntityModel<Producto>> buscarEcologicos() {
        List<EntityModel<Producto>> productos = catalogoService.buscarProductosEcologicos().stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).buscarEcologicos()).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).buscarEcologicos()).withSelfRel());
    }
}
