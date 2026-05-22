package com.ecomarket.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respaldos_datos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespaldoDatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespaldo;

    @Column(nullable = false, length = 120)
    private String origenDatos;

    @Column(nullable = false, length = 80)
    private String frecuencia;

    @Column(nullable = false, length = 120)
    private String responsable;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false, length = 500)
    private String resultado;

    @Column(nullable = false)
    private LocalDateTime fechaProgramada;

    private LocalDateTime fechaEjecucion;

    private LocalDateTime fechaRestauracion;
}
