package com.ecomarket.admin.repository;

import com.ecomarket.admin.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TiendaRepository extends JpaRepository<Tienda, Long> {

    List<Tienda> findByCiudadIgnoreCase(String ciudad);

    List<Tienda> findByActivaTrue();
}
