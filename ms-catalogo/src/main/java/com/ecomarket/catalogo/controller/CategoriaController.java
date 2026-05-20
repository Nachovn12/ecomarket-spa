package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.model.Categoria;
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
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CatalogoService catalogoService;

    @PostMapping
    public ResponseEntity<EntityModel<Categoria>> crearCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria nueva = catalogoService.guardarCategoria(categoria);
        return new ResponseEntity<>(ensamblarResource(nueva), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Categoria>>> listarCategorias() {
        List<Categoria> categorias = catalogoService.obtenerTodasCategorias();
        return ResponseEntity.ok(ensamblarCollection(categorias));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Categoria>> obtenerCategoriaPorId(@PathVariable Long id) {
        Categoria categoria = catalogoService.obtenerCategoriaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        return ResponseEntity.ok(ensamblarResource(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Categoria>> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        Categoria existente = catalogoService.obtenerCategoriaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        
        existente.setNombre(categoria.getNombre());
        existente.setEstado(categoria.getEstado());
        
        Categoria actualizada = catalogoService.guardarCategoria(existente);
        return ResponseEntity.ok(ensamblarResource(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        catalogoService.obtenerCategoriaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        catalogoService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Categoria> ensamblarResource(Categoria categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaController.class).obtenerCategoriaPorId(categoria.getIdCategoria())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withRel("categorias"));
    }

    private CollectionModel<EntityModel<Categoria>> ensamblarCollection(List<Categoria> categorias) {
        List<EntityModel<Categoria>> categoriasResource = categorias.stream()
                .map(this::ensamblarResource)
                .collect(Collectors.toList());
        return CollectionModel.of(categoriasResource,
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withSelfRel());
    }
}