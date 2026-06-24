package com.ecomarket.catalogo.service;

import com.ecomarket.catalogo.dto.*;
import com.ecomarket.catalogo.exception.ConflictException;
import com.ecomarket.catalogo.exception.ResourceNotFoundException;
import com.ecomarket.catalogo.model.*;
import com.ecomarket.catalogo.repository.CategoriaRepository;
import com.ecomarket.catalogo.repository.ProductoRepository;
import com.ecomarket.catalogo.repository.ResenaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio principal del catálogo.
 * Contiene toda la lógica de negocio para productos, categorías y reseñas.
 * Patrón CSR: Controller → service → repository.
 */
@Service
public class CatalogoService {

    private static final Logger log = LoggerFactory.getLogger(CatalogoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private PedidosClientService pedidosClientService;

    // Productos

    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        log.info("Creando producto con SKU: {}", dto.getSku());

        if (productoRepository.existsBySku(dto.getSku())) {
            log.warn("SKU duplicado al intentar crear producto: {}", dto.getSku());
            throw new ConflictException("Ya existe un producto con el SKU: " + dto.getSku());
        }

        Producto producto = new Producto();
        producto.setSku(dto.getSku().trim());
        producto.setNombre(dto.getNombre().trim());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setDescripcionEcologica(dto.getDescripcionEcologica());
        producto.setEstado(resolverEstadoProducto(dto.getEstado()));

        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría no encontrada con ID: " + dto.getIdCategoria()));
            producto.setCategoria(categoria);
        }

        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado correctamente. idProducto={}, sku={}", guardado.getIdProducto(), guardado.getSku());
        return mapearProducto(guardado);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerTodosProductos() {
        log.info("Listando todos los productos del catálogo");
        return productoRepository.findAll().stream()
                .map(this::mapearProducto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        log.info("Consultando producto por ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return mapearProducto(producto);
    }

    @Transactional
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto) {
        log.info("Actualizando producto ID: {}", id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        // Verificar duplicado de SKU solo si cambió
        if (!producto.getSku().equals(dto.getSku()) && productoRepository.existsBySku(dto.getSku())) {
            log.warn("SKU duplicado al actualizar producto. idProducto={}, sku={}", id, dto.getSku());
            throw new ConflictException("Ya existe un producto con el SKU: " + dto.getSku());
        }

        producto.setSku(dto.getSku().trim());
        producto.setNombre(dto.getNombre().trim());
        producto.setPrecio(dto.getPrecio());
        producto.setDescripcion(dto.getDescripcion());
        producto.setDescripcionEcologica(dto.getDescripcionEcologica());
        producto.setEstado(resolverEstadoProducto(dto.getEstado()));

        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría no encontrada con ID: " + dto.getIdCategoria()));
            producto.setCategoria(categoria);
        } else {
            producto.setCategoria(null);
        }

        Producto actualizado = productoRepository.save(producto);
        log.info("Producto actualizado correctamente. idProducto={}", id);
        return mapearProducto(actualizado);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        log.info("Eliminando producto ID: {}", id);
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
        log.info("Producto eliminado correctamente. idProducto={}", id);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarPorPalabraClave(String palabra) {
        log.info("Buscando productos por palabra clave: {}", palabra);
        return productoRepository
                .findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrDescripcionEcologicaContainingIgnoreCase(
                        palabra, palabra, palabra)
                .stream().map(this::mapearProducto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarPorCategoria(Long idCategoria) {
        log.info("Buscando productos por categoría ID: {}", idCategoria);
        return productoRepository.findByCategoriaIdCategoria(idCategoria)
                .stream().map(this::mapearProducto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarPorPrecio(Double min, Double max) {
        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("Rango de precios inválido: min=" + min + " max=" + max);
        }
        return productoRepository.findByPrecioBetween(min, max)
                .stream().map(this::mapearProducto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarEcologicos(String atributo) {
        log.info("Buscando productos ecológicos con atributo: {}", atributo);
        return productoRepository.findByDescripcionEcologicaContainingIgnoreCase(atributo)
                .stream().map(this::mapearProducto).collect(Collectors.toList());
    }

    // CATEGORÍAS

    @Transactional
    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO dto) {
        log.info("Creando categoría: {}", dto.getNombre());
        if (categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            log.warn("Categoría duplicada al intentar crear: {}", dto.getNombre());
            throw new ConflictException("Ya existe una categoría con el nombre: " + dto.getNombre());
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre().trim());
        categoria.setDescripcion(dto.getDescripcion());
        categoria.setEstado(resolverEstadoCategoria(dto.getEstado()));
        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoría creada correctamente. idCategoria={}", guardada.getIdCategoria());
        return mapearCategoria(guardada);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> obtenerTodasCategorias() {
        log.info("Listando todas las categorías");
        return categoriaRepository.findAll().stream()
                .map(this::mapearCategoria)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerCategoriaPorId(Long id) {
        log.info("Consultando categoría por ID: {}", id);
        return mapearCategoria(categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id)));
    }

    @Transactional
    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO dto) {
        log.info("Actualizando categoría ID: {}", id);
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        if (!existente.getNombre().equalsIgnoreCase(dto.getNombre())
                && categoriaRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + dto.getNombre());
        }

        existente.setNombre(dto.getNombre().trim());
        existente.setDescripcion(dto.getDescripcion());
        existente.setEstado(resolverEstadoCategoria(dto.getEstado()));
        Categoria actualizada = categoriaRepository.save(existente);
        log.info("Categoría actualizada correctamente. idCategoria={}", id);
        return mapearCategoria(actualizada);
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        log.info("Eliminando categoría ID: {}", id);
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
        }
        // Verificar que no tenga productos asociados antes de eliminar
        List<Producto> productosAsociados = productoRepository.findByCategoriaIdCategoria(id);
        if (!productosAsociados.isEmpty()) {
            throw new ConflictException(
                    "No se puede eliminar la categoría porque tiene " + productosAsociados.size()
                            + " producto(s) asociado(s)");
        }
        categoriaRepository.deleteById(id);
        log.info("Categoría eliminada correctamente. idCategoria={}", id);
    }

    // RESEÑAS

    @Transactional
    public ResenaResponseDTO crearResena(ResenaRequestDTO dto) {
        log.info("Creando reseña para producto ID: {} por cliente ID: {}", dto.getIdProducto(), dto.getIdCliente());

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + dto.getIdProducto()));

        // Regla de negocio: Solo clientes que compraron el producto pueden dejar reseña
        if (!pedidosClientService.verificarCompra(dto.getIdCliente(), dto.getIdProducto())) {
            log.warn("El cliente {} intentó reseñar el producto {} sin haberlo comprado.", dto.getIdCliente(), dto.getIdProducto());
            throw new ConflictException("No puedes reseñar un producto que no has comprado.");
        }

        Resena resena = new Resena();
        resena.setIdCliente(dto.getIdCliente());
        resena.setProducto(producto);
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        resena.setEstado(EstadoResena.PUBLICADA);

        Resena guardada = resenaRepository.save(resena);
        log.info("Reseña creada correctamente. idResena={}", guardada.getIdResena());
        return mapearResena(guardada);
    }

    @Transactional(readOnly = true)
    public List<ResenaResponseDTO> obtenerTodasResenas() {
        log.info("Listando todas las reseñas");
        return resenaRepository.findAll().stream()
                .map(this::mapearResena)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResenaResponseDTO obtenerResenaPorId(Long id) {
        log.info("Consultando reseña por ID: {}", id);
        return mapearResena(resenaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada con ID: " + id)));
    }

    @Transactional
    public void eliminarResena(Long id) {
        log.info("Eliminando reseña ID: {}", id);
        if (!resenaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reseña no encontrada con ID: " + id);
        }
        resenaRepository.deleteById(id);
        log.info("Reseña eliminada correctamente. idResena={}", id);
    }

    @Transactional(readOnly = true)
    public Double calcularPromedioCalificaciones(Long idProducto) {
        log.info("Calculando promedio de calificaciones para el producto ID: {}", idProducto);
        List<Resena> resenas = resenaRepository.findByProductoIdProducto(idProducto);
        if (resenas.isEmpty()) {
            return 0.0;
        }
        double suma = resenas.stream().mapToInt(Resena::getCalificacion).sum();
        double promedio = suma / resenas.size();
        return new java.math.BigDecimal(promedio).setScale(1, java.math.RoundingMode.HALF_UP).doubleValue();
    }

    // Métodos privados de mapeo entity → DTO

    private ProductoResponseDTO mapearProducto(Producto p) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setIdProducto(p.getIdProducto());
        dto.setSku(p.getSku());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setDescripcion(p.getDescripcion());
        dto.setDescripcionEcologica(p.getDescripcionEcologica());
        dto.setEstado(p.getEstado() != null ? p.getEstado().name() : null);
        dto.setFechaCreacion(p.getFechaCreacion());
        dto.setFechaActualizacion(p.getFechaActualizacion());
        if (p.getCategoria() != null) {
            dto.setIdCategoria(p.getCategoria().getIdCategoria());
            dto.setNombreCategoria(p.getCategoria().getNombre());
        }
        return dto;
    }

    private CategoriaResponseDTO mapearCategoria(Categoria c) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setIdCategoria(c.getIdCategoria());
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        dto.setEstado(c.getEstado() != null ? c.getEstado().name() : null);
        return dto;
    }

    private ResenaResponseDTO mapearResena(Resena r) {
        ResenaResponseDTO dto = new ResenaResponseDTO();
        dto.setIdResena(r.getIdResena());
        dto.setIdCliente(r.getIdCliente());
        dto.setCalificacion(r.getCalificacion());
        dto.setComentario(r.getComentario());
        dto.setEstado(r.getEstado() != null ? r.getEstado().name() : null);
        dto.setFechaCreacion(r.getFechaCreacion());
        if (r.getProducto() != null) {
            dto.setIdProducto(r.getProducto().getIdProducto());
            dto.setNombreProducto(r.getProducto().getNombre());
        }
        return dto;
    }

    private EstadoProducto resolverEstadoProducto(String estado) {
        if (estado == null || estado.isBlank()) return EstadoProducto.PUBLICADO;
        try {
            return EstadoProducto.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de producto inválido: " + estado);
        }
    }

    private EstadoCategoria resolverEstadoCategoria(String estado) {
        if (estado == null || estado.isBlank()) return EstadoCategoria.ACTIVA;
        try {
            return EstadoCategoria.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de categoría inválido: " + estado);
        }
    }
}
