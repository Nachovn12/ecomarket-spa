package com.ecomarket.reportes.repository;

import com.ecomarket.reportes.entity.ReporteRendimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRendimientoRepository extends JpaRepository<ReporteRendimiento, Long> {
}