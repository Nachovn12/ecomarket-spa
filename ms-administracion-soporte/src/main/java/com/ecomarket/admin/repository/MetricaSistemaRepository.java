package com.ecomarket.admin.repository;

import com.ecomarket.admin.entity.MetricaSistema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetricaSistemaRepository extends JpaRepository<MetricaSistema, Long> {

    List<MetricaSistema> findByMicroservicioIgnoreCase(String microservicio);

    List<MetricaSistema> findByDisponibleFalse();
}
