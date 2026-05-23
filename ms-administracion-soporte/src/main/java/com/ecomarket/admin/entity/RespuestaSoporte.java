package com.ecomarket.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respuestas_soporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuesta;

    @Column(nullable = false)
    private Long idTicket;

    @Column(nullable = false, length = 1000)
    private String mensaje;

    @Column(nullable = false, length = 120)
    private String respondidoPor;

    @Column(nullable = false)
    private LocalDateTime fechaRespuesta;
}
