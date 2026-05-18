package com.ecomarket.catalogo.service;

import com.ecomarket.catalogo.model.Categoria;
import com.ecomarket.catalogo.model.Producto;
import com.ecomarket.catalogo.repository.CategoriaRepository;
import com.ecomarket.catalogo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // --- LÓGICA DE PRODUCTOS (HU-49) ---
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerTodosProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    // --- LÓGICA DE BÚSQUEDAS (HU-6) ---
    public List<Producto> buscarPorPalabraClave(String palabra) {
        return productoRepository
                .findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrDescripcionEcologicaContainingIgnoreCase(
                        palabra, palabra, palabra);
    }

    public List<Producto> buscarPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoriaIdCategoria(idCategoria);
    }

    public List<Producto> buscarPorPrecio(Double min, Double max) {
        return productoRepository.findByPrecioBetween(min, max);
    }

    public List<Producto> buscarEcologicos(String atributo) {
        return productoRepository.findByDescripcionEcologicaContainingIgnoreCase(atributo);
    }

    // --- LÓGICA DE CATEGORÍAS ---
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> obtenerTodasCategorias() {
        return categoriaRepository.findAll();
    }
}
