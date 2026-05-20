package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.model.Ruta;
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
@RequestMapping("/api/rutas")
public class RutaController {
    @Autowired
    private LogisticaService service;

    @PostMapping
    public ResponseEntity<EntityModel<Ruta>> crear(@Valid @RequestBody Ruta ruta) {
        Ruta guardada = service.guardarRuta(ruta);
        return new ResponseEntity<>(ensamblar(guardada), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Ruta>>> listarTodas() {
        List<EntityModel<Ruta>> rutas = service.obtenerRutas().stream()
            .map(this::ensamblar)
            .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(rutas, linkTo(methodOn(RutaController.class).listarTodas()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Ruta>> buscarPorId(@PathVariable Long id) {
        Ruta ruta = service.obtenerRutaPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id));
        return ResponseEntity.ok(ensamblar(ruta));
    }

    private EntityModel<Ruta> ensamblar(Ruta ruta) {
        return EntityModel.of(ruta,
            linkTo(methodOn(RutaController.class).buscarPorId(ruta.getIdRuta())).withSelfRel(),
            linkTo(methodOn(RutaController.class).listarTodas()).withRel("rutas"));
    }
}
