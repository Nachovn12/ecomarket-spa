package com.ecomarket.admin.repository;

import com.ecomarket.admin.entity.RespaldoDatos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespaldoDatosRepository extends JpaRepository<RespaldoDatos, Long> {

    List<RespaldoDatos> findByEstadoIgnoreCase(String estado);

    List<RespaldoDatos> findByOrigenDatosIgnoreCase(String origenDatos);
}
