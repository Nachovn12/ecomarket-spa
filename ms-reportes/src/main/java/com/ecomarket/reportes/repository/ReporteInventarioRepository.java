package com.ecomarket.reportes.repository;

import com.ecomarket.reportes.entity.ReporteInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteInventarioRepository extends JpaRepository<ReporteInventario, Long> {
}