package com.ecomarket.catalogo.repository;

import com.ecomarket.catalogo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Esta línea busca solo los ecológicos (HU-6)
    List<Producto> findByEsEcologicoTrue();
}
