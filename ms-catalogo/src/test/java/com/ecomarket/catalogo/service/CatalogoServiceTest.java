package com.ecomarket.catalogo.service;

import com.ecomarket.catalogo.dto.*;
import com.ecomarket.catalogo.exception.ConflictException;
import com.ecomarket.catalogo.exception.ResourceNotFoundException;
import com.ecomarket.catalogo.model.*;
import com.ecomarket.catalogo.repository.CategoriaRepository;
import com.ecomarket.catalogo.repository.ProductoRepository;
import com.ecomarket.catalogo.repository.ResenaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private PedidosClientService pedidosClientService;

    @InjectMocks
    private CatalogoService catalogoService;

    // --- TESTS PARA PRODUCTOS ---

    @Test
    void crearProducto_Exito() {
        ProductoRequestDTO request = new ProductoRequestDTO();
        request.setSku("SKU-1");
        request.setNombre("Prod 1");
        request.setPrecio(100.0);
        request.setIdCategoria(1L);
        request.setEstado("PUBLICADO");

        Categoria cat = new Categoria();
        cat.setIdCategoria(1L);

        when(productoRepository.existsBySku("SKU-1")).thenReturn(false);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat));
        
        Producto guardado = new Producto();
        guardado.setIdProducto(100L);
        guardado.setSku("SKU-1");
        guardado.setCategoria(cat);
        guardado.setEstado(EstadoProducto.PUBLICADO);
        
        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        ProductoResponseDTO response = catalogoService.crearProducto(request);

        assertNotNull(response);
        assertEquals(100L, response.getIdProducto());
        assertEquals("SKU-1", response.getSku());
    }

    @Test
    void crearProducto_Exito_SinCategoriaYEstadoNull() {
        ProductoRequestDTO request = new ProductoRequestDTO();
        request.setSku("SKU-2");
        request.setNombre("Prod 2");
        request.setPrecio(50.0);
        request.setIdCategoria(null);
        request.setEstado(null);

        when(productoRepository.existsBySku("SKU-2")).thenReturn(false);

        Producto guardado = new Producto();
        guardado.setIdProducto(101L);
        guardado.setSku("SKU-2");
        guardado.setEstado(null);

        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        ProductoResponseDTO response = catalogoService.crearProducto(request);
        assertNotNull(response);
        assertNull(response.getIdCategoria());
    }

    @Test
    void crearProducto_CategoriaNoEncontrada() {
        ProductoRequestDTO request = new ProductoRequestDTO();
        request.setSku("SKU-1");
        request.setNombre("Prod 1");
        request.setPrecio(100.0);
        request.setIdCategoria(99L);

        when(productoRepository.existsBySku("SKU-1")).thenReturn(false);
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> catalogoService.crearProducto(request));
    }

    @Test
    void crearProducto_EstadoInvalido() {
        ProductoRequestDTO request = new ProductoRequestDTO();
        request.setSku("SKU-3");
        request.setNombre("Prod 3");
        request.setPrecio(10.0);
        request.setEstado("INVENTADO");

        when(productoRepository.existsBySku("SKU-3")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> catalogoService.crearProducto(request));
    }

    @Test
    void crearProducto_SkuDuplicado() {
        ProductoRequestDTO request = new ProductoRequestDTO();
        request.setSku("SKU-1");

        when(productoRepository.existsBySku("SKU-1")).thenReturn(true);

        assertThrows(ConflictException.class, () -> catalogoService.crearProducto(request));
    }

    @Test
    void obtenerTodosProductos_Exito() {
        Producto p = new Producto();
        p.setIdProducto(1L);
        when(productoRepository.findAll()).thenReturn(List.of(p));

        List<ProductoResponseDTO> res = catalogoService.obtenerTodosProductos();
        assertFalse(res.isEmpty());
        assertEquals(1, res.size());
    }

    @Test
    void obtenerProductoPorId_Exito() {
        Producto p = new Producto();
        p.setIdProducto(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

        ProductoResponseDTO res = catalogoService.obtenerProductoPorId(1L);
        assertNotNull(res);
        assertEquals(1L, res.getIdProducto());
    }

    @Test
    void obtenerProductoPorId_NoEncontrado() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.obtenerProductoPorId(1L));
    }

    @Test
    void actualizarProducto_Exito() {
        Producto p = new Producto();
        p.setIdProducto(1L);
        p.setSku("SKU-OLD");

        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("SKU-NEW");
        req.setNombre("Prod NEW");
        req.setPrecio(200.0);
        req.setIdCategoria(1L);

        Categoria cat = new Categoria();
        cat.setIdCategoria(1L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productoRepository.existsBySku("SKU-NEW")).thenReturn(false);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(productoRepository.save(any(Producto.class))).thenReturn(p);

        ProductoResponseDTO res = catalogoService.actualizarProducto(1L, req);
        assertNotNull(res);
    }

    @Test
    void actualizarProducto_Exito_SinCambioSkuYSinCategoria() {
        Producto p = new Producto();
        p.setIdProducto(1L);
        p.setSku("SKU-OLD");

        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("SKU-OLD");
        req.setNombre("Prod NEW");
        req.setPrecio(200.0);
        req.setIdCategoria(null);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productoRepository.save(any(Producto.class))).thenReturn(p);

        ProductoResponseDTO res = catalogoService.actualizarProducto(1L, req);
        assertNotNull(res);
    }

    @Test
    void actualizarProducto_SkuDuplicado() {
        Producto p = new Producto();
        p.setIdProducto(1L);
        p.setSku("SKU-OLD");

        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("SKU-NEW");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productoRepository.existsBySku("SKU-NEW")).thenReturn(true);

        assertThrows(ConflictException.class, () -> catalogoService.actualizarProducto(1L, req));
    }

    @Test
    void eliminarProducto_Exito() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        catalogoService.eliminarProducto(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarProducto_NoEncontrado() {
        when(productoRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.eliminarProducto(1L));
    }

    @Test
    void buscarPorPalabraClave_Exito() {
        when(productoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrDescripcionEcologicaContainingIgnoreCase(
                "bio", "bio", "bio")).thenReturn(List.of(new Producto()));
        List<ProductoResponseDTO> res = catalogoService.buscarPorPalabraClave("bio");
        assertFalse(res.isEmpty());
    }

    @Test
    void buscarPorCategoria_Exito() {
        when(productoRepository.findByCategoriaIdCategoria(1L)).thenReturn(List.of(new Producto()));
        List<ProductoResponseDTO> res = catalogoService.buscarPorCategoria(1L);
        assertFalse(res.isEmpty());
    }

    @Test
    void buscarPorPrecio_Exito() {
        when(productoRepository.findByPrecioBetween(10.0, 50.0)).thenReturn(List.of(new Producto()));
        List<ProductoResponseDTO> res = catalogoService.buscarPorPrecio(10.0, 50.0);
        assertFalse(res.isEmpty());
    }

    @Test
    void buscarPorPrecio_Invalido() {
        assertThrows(IllegalArgumentException.class, () -> catalogoService.buscarPorPrecio(-10.0, 50.0));
        assertThrows(IllegalArgumentException.class, () -> catalogoService.buscarPorPrecio(10.0, -50.0));
        assertThrows(IllegalArgumentException.class, () -> catalogoService.buscarPorPrecio(50.0, 10.0));
    }

    @Test
    void buscarEcologicos_Exito() {
        when(productoRepository.findByDescripcionEcologicaContainingIgnoreCase("biodegradable")).thenReturn(List.of(new Producto()));
        List<ProductoResponseDTO> res = catalogoService.buscarEcologicos("biodegradable");
        assertFalse(res.isEmpty());
    }

    // --- TESTS PARA CATEGORIAS ---

    @Test
    void crearCategoria_Exito() {
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Cat 1");
        req.setEstado("ACTIVA");
        when(categoriaRepository.existsByNombreIgnoreCase("Cat 1")).thenReturn(false);
        
        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        c.setEstado(EstadoCategoria.ACTIVA);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(c);

        CategoriaResponseDTO res = catalogoService.crearCategoria(req);
        assertEquals(1L, res.getIdCategoria());
    }

    @Test
    void crearCategoria_EstadoInvalido() {
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Cat 1");
        req.setEstado("INVALIDO");
        when(categoriaRepository.existsByNombreIgnoreCase("Cat 1")).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> catalogoService.crearCategoria(req));
    }

    @Test
    void crearCategoria_Conflicto() {
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Cat 1");
        when(categoriaRepository.existsByNombreIgnoreCase("Cat 1")).thenReturn(true);
        
        assertThrows(ConflictException.class, () -> catalogoService.crearCategoria(req));
    }

    @Test
    void obtenerTodasCategorias_Exito() {
        when(categoriaRepository.findAll()).thenReturn(List.of(new Categoria()));
        assertFalse(catalogoService.obtenerTodasCategorias().isEmpty());
    }

    @Test
    void obtenerCategoriaPorId_Exito() {
        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(c));
        assertNotNull(catalogoService.obtenerCategoriaPorId(1L));
    }

    @Test
    void actualizarCategoria_Exito() {
        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        c.setNombre("OLD");

        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("NEW");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(c));
        when(categoriaRepository.existsByNombreIgnoreCase("NEW")).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(c);

        assertNotNull(catalogoService.actualizarCategoria(1L, req));
    }

    @Test
    void actualizarCategoria_Conflicto() {
        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        c.setNombre("OLD");

        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("NEW");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(c));
        when(categoriaRepository.existsByNombreIgnoreCase("NEW")).thenReturn(true);

        assertThrows(ConflictException.class, () -> catalogoService.actualizarCategoria(1L, req));
    }

    @Test
    void eliminarCategoria_Exito() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.findByCategoriaIdCategoria(1L)).thenReturn(Collections.emptyList());
        
        catalogoService.eliminarCategoria(1L);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarCategoria_NoEncontrada() {
        when(categoriaRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.eliminarCategoria(1L));
    }

    @Test
    void eliminarCategoria_ConProductos() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.findByCategoriaIdCategoria(1L)).thenReturn(List.of(new Producto()));
        
        assertThrows(ConflictException.class, () -> catalogoService.eliminarCategoria(1L));
    }

    // --- TESTS PARA RESEÑAS ---

    @Test
    void crearResena_Exito_SiCompro() {
        ResenaRequestDTO req = new ResenaRequestDTO();
        req.setIdProducto(1L);
        req.setIdCliente(10L);
        req.setCalificacion(5);

        Producto p = new Producto();
        p.setIdProducto(1L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(pedidosClientService.verificarCompra(10L, 1L)).thenReturn(true);
        
        Resena r = new Resena();
        r.setIdResena(100L);
        r.setCalificacion(5);
        r.setProducto(p);
        when(resenaRepository.save(any(Resena.class))).thenReturn(r);

        ResenaResponseDTO res = catalogoService.crearResena(req);
        assertEquals(100L, res.getIdResena());
        assertEquals(5, res.getCalificacion());
    }

    @Test
    void crearResena_Fallo_NoCompro() {
        ResenaRequestDTO req = new ResenaRequestDTO();
        req.setIdProducto(1L);
        req.setIdCliente(10L);

        Producto p = new Producto();
        p.setIdProducto(1L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(pedidosClientService.verificarCompra(10L, 1L)).thenReturn(false);

        assertThrows(ConflictException.class, () -> catalogoService.crearResena(req));
    }

    @Test
    void obtenerTodasResenas_Exito() {
        when(resenaRepository.findAll()).thenReturn(List.of(new Resena()));
        assertFalse(catalogoService.obtenerTodasResenas().isEmpty());
    }

    @Test
    void obtenerResenaPorId_Exito() {
        Resena r = new Resena();
        r.setIdResena(1L);
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(r));
        assertNotNull(catalogoService.obtenerResenaPorId(1L));
    }

    @Test
    void eliminarResena_Exito() {
        when(resenaRepository.existsById(1L)).thenReturn(true);
        catalogoService.eliminarResena(1L);
        verify(resenaRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarResena_NoEncontrada() {
        when(resenaRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.eliminarResena(1L));
    }

    @Test
    void calcularPromedioCalificaciones_Exito() {
        Resena r1 = new Resena(); r1.setCalificacion(4);
        Resena r2 = new Resena(); r2.setCalificacion(5);
        when(resenaRepository.findByProductoIdProducto(1L)).thenReturn(List.of(r1, r2));

        Double promedio = catalogoService.calcularPromedioCalificaciones(1L);
        assertEquals(4.5, promedio);
    }
    
    @Test
    void calcularPromedioCalificaciones_Vacio() {
        when(resenaRepository.findByProductoIdProducto(1L)).thenReturn(Collections.emptyList());
        Double promedio = catalogoService.calcularPromedioCalificaciones(1L);
        assertEquals(0.0, promedio);
    }
}
