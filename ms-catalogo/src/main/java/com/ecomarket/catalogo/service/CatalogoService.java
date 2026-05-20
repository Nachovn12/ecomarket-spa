package com.ecomarket.catalogo.service;

import com.ecomarket.catalogo.model.Categoria;
import com.ecomarket.catalogo.model.Producto;
import com.ecomarket.catalogo.model.Resena;
import com.ecomarket.catalogo.repository.CategoriaRepository;
import com.ecomarket.catalogo.repository.ProductoRepository;
import com.ecomarket.catalogo.repository.ResenaRepository;
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

    @Autowired
    private ResenaRepository resenaRepository;

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

    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> obtenerTodasCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

    public Resena guardarResena(Resena resena) {
        return resenaRepository.save(resena);
    }
}