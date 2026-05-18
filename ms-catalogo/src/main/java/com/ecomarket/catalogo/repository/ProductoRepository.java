package com.ecomarket.catalogo.repository;

import com.ecomarket.catalogo.model.EstadoProducto;
import com.ecomarket.catalogo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(EstadoProducto estado);

    List<Producto> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrDescripcionEcologicaContainingIgnoreCase(
            String nombre,
            String descripcion,
            String descripcionEcologica);

    List<Producto> findByCategoriaIdCategoria(Long idCategoria);

    List<Producto> findByPrecioBetween(Double precioMinimo, Double precioMaximo);

    List<Producto> findByDescripcionEcologicaContainingIgnoreCase(String atributoEcologico);
}
