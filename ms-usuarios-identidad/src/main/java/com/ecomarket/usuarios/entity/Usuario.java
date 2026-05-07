package com.ecomarket.usuarios.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private Boolean eliminado;

    @Column(length = 50)
    private String nivelAcceso;

    @Column(length = 500)
    private String permisos;

    @Column(length = 120)
    private String modificadoPor;

    private LocalDateTime fechaModificacionAcceso;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;
}