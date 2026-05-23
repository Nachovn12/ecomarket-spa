package com.ecomarket.reportes.repository;

import com.ecomarket.reportes.entity.IndicadorKPI;
import com.ecomarket.reportes.entity.TipoKPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicadorKPIRepository extends JpaRepository<IndicadorKPI, Long> {
    List<IndicadorKPI> findByTipo(TipoKPI tipo);
}
