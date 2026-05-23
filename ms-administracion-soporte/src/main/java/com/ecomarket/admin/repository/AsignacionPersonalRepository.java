package com.ecomarket.admin.repository;

import com.ecomarket.admin.entity.AsignacionPersonal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionPersonalRepository extends JpaRepository<AsignacionPersonal, Long> {

    List<AsignacionPersonal> findByIdTiendaAndActivaTrue(Long idTienda);

    List<AsignacionPersonal> findByIdUsuarioInternoAndActivaTrue(Long idUsuarioInterno);
}
