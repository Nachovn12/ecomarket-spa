package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.model.Proveedor;
import com.ecomarket.logistica.service.LogisticaService;
import com.ecomarket.logistica.exception.ResourceNotFoundException;
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
@RequestMapping("/api/envios/proveedores")
public class ProveedorController {
    @Autowired
    private LogisticaService service;

    @PostMapping
    public ResponseEntity<EntityModel<Proveedor>> crear(@Valid @RequestBody Proveedor proveedor) {
        Proveedor guardado = service.guardarProveedor(proveedor);
        return new ResponseEntity<>(ensamblar(guardado), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Proveedor>>> listarTodos() {
        List<EntityModel<Proveedor>> proveedores = service.obtenerProveedores().stream()
            .map(this::ensamblar)
            .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(proveedores, linkTo(methodOn(ProveedorController.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Proveedor>> buscarPorId(@PathVariable Long id) {
        Proveedor proveedor = service.obtenerProveedorPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        return ResponseEntity.ok(ensamblar(proveedor));
    }

    private EntityModel<Proveedor> ensamblar(Proveedor proveedor) {
        return EntityModel.of(proveedor,
            linkTo(methodOn(ProveedorController.class).buscarPorId(proveedor.getIdProveedor())).withSelfRel(),
            linkTo(methodOn(ProveedorController.class).listarTodos()).withRel("proveedores"));
    }
}
