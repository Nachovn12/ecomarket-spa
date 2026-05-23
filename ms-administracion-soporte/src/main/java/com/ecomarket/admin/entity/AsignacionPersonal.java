package com.ecomarket.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones_personal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignacionPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;

    @Column(nullable = false)
    private Long idUsuarioInterno;

    @Column(nullable = false)
    private Long idTienda;

    @Column(nullable = false, length = 80)
    private String cargo;

    @Column(nullable = false)
    private Boolean activa;

    @Column(nullable = false)
    private LocalDateTime fechaAsignacion;
}
