package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.ProveedorDTO;
import com.ecomarket.logistica.model.Proveedor;
import com.ecomarket.logistica.service.LogisticaService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/envios/proveedores")
public class ProveedorController {

    private final LogisticaService logisticaService;

    public ProveedorController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    private EntityModel<Proveedor> ensamblar(Proveedor prov) {
        return EntityModel.of(prov,
                linkTo(methodOn(ProveedorController.class).obtenerPorId(prov.getId())).withSelfRel(),
                linkTo(methodOn(ProveedorController.class).obtenerTodos()).withRel("proveedores")
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Proveedor>> crear(@Valid @RequestBody ProveedorDTO dto) {
        Proveedor creado = logisticaService.crearProveedor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ensamblar(creado));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Proveedor>>> obtenerTodos() {
        List<EntityModel<Proveedor>> proveedores = logisticaService.obtenerProveedores().stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(proveedores, linkTo(methodOn(ProveedorController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Proveedor>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ensamblar(logisticaService.obtenerProveedorPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Proveedor>> actualizar(@PathVariable Long id, @RequestBody ProveedorDTO dto) {
        return ResponseEntity.ok(ensamblar(logisticaService.actualizarProveedor(id, dto)));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        logisticaService.activarProveedor(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        logisticaService.desactivarProveedor(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<CollectionModel<EntityModel<Proveedor>>> obtenerActivos() {
        List<EntityModel<Proveedor>> activos = logisticaService.obtenerProveedoresActivos().stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(activos));
    }

    @GetMapping("/buscar")
    public ResponseEntity<CollectionModel<EntityModel<Proveedor>>> buscar(
            @RequestParam String tipoProveedor, @RequestParam String cobertura) {
        List<EntityModel<Proveedor>> resultados = logisticaService.buscarProveedores(tipoProveedor, cobertura).stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(resultados));
    }
}