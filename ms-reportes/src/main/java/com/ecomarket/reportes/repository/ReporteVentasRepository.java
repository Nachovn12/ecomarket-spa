package com.ecomarket.reportes.repository;

import com.ecomarket.reportes.entity.ReporteVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteVentasRepository extends JpaRepository<ReporteVentas, Long> {
}