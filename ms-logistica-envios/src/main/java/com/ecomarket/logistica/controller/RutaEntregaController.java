package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.CambioEstadoRutaRequestDTO;
import com.ecomarket.logistica.dto.RutaEntregaDTO;
import com.ecomarket.logistica.model.RutaEntrega;
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
@RequestMapping("/api/rutas")
public class RutaEntregaController {

    private final LogisticaService logisticaService;

    public RutaEntregaController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    private EntityModel<RutaEntrega> ensamblar(RutaEntrega ruta) {
        return EntityModel.of(ruta,
                linkTo(methodOn(RutaEntregaController.class).obtenerPorId(ruta.getId())).withSelfRel(),
                linkTo(methodOn(RutaEntregaController.class).obtenerTodas()).withRel("rutas")
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<RutaEntrega>> crear(@Valid @RequestBody RutaEntregaDTO dto) {
        RutaEntrega creada = logisticaService.crearRuta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ensamblar(creada));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<RutaEntrega>>> obtenerTodas() {
        List<EntityModel<RutaEntrega>> rutas = logisticaService.obtenerRutas().stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(rutas, linkTo(methodOn(RutaEntregaController.class).obtenerTodas()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RutaEntrega>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ensamblar(logisticaService.obtenerRutaPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<RutaEntrega>> actualizar(@PathVariable Long id, @RequestBody RutaEntregaDTO dto) {
        return ResponseEntity.ok(ensamblar(logisticaService.actualizarRuta(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logisticaService.eliminarRuta(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EntityModel<RutaEntrega>> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRutaRequestDTO request) {
        return ResponseEntity.ok(
                ensamblar(logisticaService.cambiarEstadoRuta(id, request.getEstado()))
        );
    }
}
