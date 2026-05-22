package com.ecomarket.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "metricas_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricaSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMetrica;

    @Column(nullable = false, length = 80)
    private String microservicio;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(nullable = false)
    private Long tiempoRespuestaMs;

    @Column(nullable = false)
    private Integer erroresDetectados;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;
}
