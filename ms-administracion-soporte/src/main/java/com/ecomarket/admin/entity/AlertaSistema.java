package com.ecomarket.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlerta;

    @Column(nullable = false, length = 80)
    private String microservicio;

    @Column(nullable = false, length = 80)
    private String tipoAlerta;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Boolean resuelta;

    @Column(nullable = false)
    private LocalDateTime fechaGeneracion;

    private LocalDateTime fechaResolucion;
}
