package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.model.Resena;
import com.ecomarket.catalogo.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
//control
@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private CatalogoService catalogoService;

    @PostMapping
    public ResponseEntity<EntityModel<Resena>> crearResena(@Valid @RequestBody Resena resena) {
        Resena nueva = catalogoService.guardarResena(resena);
        EntityModel<Resena> resource = EntityModel.of(nueva,
                linkTo(methodOn(ResenaController.class).crearResena(null)).withSelfRel());
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
}