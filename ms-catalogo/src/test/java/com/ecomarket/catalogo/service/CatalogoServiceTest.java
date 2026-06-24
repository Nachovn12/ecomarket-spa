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
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de CatalogoService.
 * MS Catálogo EcoMarket SPA: marketplace de productos ecológicos.
 * Cubre CRUD de productos, categorías, reseñas y regla de negocio
 * "solo clientes que compraron el producto pueden dejar reseña".
 */
@ExtendWith(MockitoExtension.class)
public class CatalogoServiceTest {

    // ─── Mocks de repositorios ──────────────────────────────────────────────────
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

    // ═══════════════════════════════════════════════════════════════════════════
    // PRODUCTOS — CRUD
    // ═══════════════════════════════════════════════════════════════════════════

    // ─── Crear producto exitoso con categoría ──────────────────────────────────
    @Test
    void crearProducto_ConCategoria_Exito() {
        // Escenario: el administrador publica una bolsa biodegradable en la categoría
        // "Productos Biodegradables" del catálogo EcoMarket.
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-001");
        req.setNombre("Bolsa biodegradable mediana");
        req.setPrecio(1990.0);
        req.setDescripcion("Bolsa reutilizable hecha de almidón de maíz");
        req.setDescripcionEcologica("100% biodegradable en 180 días, libre de plástico");
        req.setEstado("PUBLICADO");
        req.setIdCategoria(1L);

        Categoria catBiodegradables = new Categoria();
        catBiodegradables.setIdCategoria(1L);
        catBiodegradables.setNombre("Productos Biodegradables");
        catBiodegradables.setEstado(EstadoCategoria.ACTIVA);

        when(productoRepository.existsBySku("ECO-001")).thenReturn(false);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(catBiodegradables));

        Producto guardado = new Producto();
        guardado.setIdProducto(1L);
        guardado.setSku("ECO-001");
        guardado.setNombre("Bolsa biodegradable mediana");
        guardado.setPrecio(1990.0);
        guardado.setCategoria(catBiodegradables);
        guardado.setEstado(EstadoProducto.PUBLICADO);
        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        ProductoResponseDTO resp = catalogoService.crearProducto(req);
        assertNotNull(resp);
        assertEquals(1L, resp.getIdProducto());
        assertEquals("ECO-001", resp.getSku());
        assertEquals(1L, resp.getIdCategoria());
        assertEquals("Productos Biodegradables", resp.getNombreCategoria());
    }

    // ─── Crear producto sin categoría (estado null → default PUBLICADO) ────────
    @Test
    void crearProducto_SinCategoria_EstadoNull_DefaultPublicado() {
        // Escenario: se agrega una botella de agua reutilizable al catálogo
        // sin asignarle aún una categoría, el estado debe defaulf a PUBLICADO.
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-002");
        req.setNombre("Botella de agua reutilizable 750ml");
        req.setPrecio(9990.0);
        req.setDescripcion("Botella de acero inoxidable libre de BPA");
        req.setDescripcionEcologica("Fabricada con acero 316L reciclado");
        req.setEstado(null);
        req.setIdCategoria(null);

        when(productoRepository.existsBySku("ECO-002")).thenReturn(false);

        Producto guardado = new Producto();
        guardado.setIdProducto(2L);
        guardado.setSku("ECO-002");
        guardado.setNombre("Botella de agua reutilizable 750ml");
        guardado.setPrecio(9990.0);
        guardado.setEstado(EstadoProducto.PUBLICADO);
        guardado.setCategoria(null);
        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        ProductoResponseDTO resp = catalogoService.crearProducto(req);
        assertNotNull(resp);
        assertNull(resp.getIdCategoria());
        assertEquals("PUBLICADO", resp.getEstado());
    }

    // ─── Crear producto con categoría inexistente ──────────────────────────────
    @Test
    void crearProducto_CategoriaNoExiste_LanzaExcepcion() {
        // Escenario: se intenta asignar el shampoo sólido a la categoría 99
        // que no existe en el catálogo → debe lanzar ResourceNotFoundException.
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-010");
        req.setNombre("Shampoo sólido de bambú");
        req.setPrecio(4990.0);
        req.setIdCategoria(99L);

        when(productoRepository.existsBySku("ECO-010")).thenReturn(false);
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> catalogoService.crearProducto(req));
    }

    // ─── Crear producto con estado inválido ────────────────────────────────────
    @Test
    void crearProducto_EstadoInvalido_LanzaExcepcion() {
        // Escenario: se envía un estado "VENDIDO" que no existe en EstadoProducto.
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-011");
        req.setNombre("Cepillo de dientes de bambú");
        req.setPrecio(2490.0);
        req.setEstado("VENDIDO"); // no es un valor válido del enum

        when(productoRepository.existsBySku("ECO-011")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> catalogoService.crearProducto(req));
    }

    // ─── Crear producto con SKU duplicado ──────────────────────────────────────
    @Test
    void crearProducto_SkuDuplicado_LanzaConflict() {
        // Escenario: ya existe un producto con SKU "ECO-001" y se intenta crear otro.
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-001");
        req.setNombre("Bolsa biodegradable grande");

        when(productoRepository.existsBySku("ECO-001")).thenReturn(true);

        assertThrows(ConflictException.class, () -> catalogoService.crearProducto(req));
    }

    // ─── Listar todos los productos ────────────────────────────────────────────
    @Test
    void obtenerTodosProductos_Exito() {
        // Escenario: el catálogo tiene 2 productos activos publicados.
        Producto p1 = new Producto();
        p1.setIdProducto(1L);
        p1.setSku("ECO-001");
        p1.setNombre("Bolsa biodegradable mediana");
        p1.setEstado(EstadoProducto.PUBLICADO);

        Producto p2 = new Producto();
        p2.setIdProducto(2L);
        p2.setSku("ECO-002");
        p2.setNombre("Botella de agua reutilizable 750ml");
        p2.setEstado(EstadoProducto.PUBLICADO);

        when(productoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProductoResponseDTO> resp = catalogoService.obtenerTodosProductos();
        assertEquals(2, resp.size());
        assertEquals("ECO-001", resp.get(0).getSku());
        assertEquals("ECO-002", resp.get(1).getSku());
    }

    // ─── Obtener producto por ID existente ─────────────────────────────────────
    @Test
    void obtenerProductoPorId_Exito() {
        // Escenario: el cliente busca el detalle del set de cubiertos de bambú (id=3).
        Producto p = new Producto();
        p.setIdProducto(3L);
        p.setSku("ECO-003");
        p.setNombre("Set cubiertos de bambú x6");
        p.setPrecio(6990.0);
        p.setEstado(EstadoProducto.PUBLICADO);
        when(productoRepository.findById(3L)).thenReturn(Optional.of(p));

        ProductoResponseDTO resp = catalogoService.obtenerProductoPorId(3L);
        assertNotNull(resp);
        assertEquals(3L, resp.getIdProducto());
        assertEquals("ECO-003", resp.getSku());
    }

    // ─── Obtener producto por ID inexistente ───────────────────────────────────
    @Test
    void obtenerProductoPorId_NoExiste_LanzaExcepcion() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.obtenerProductoPorId(999L));
    }

    // ─── Actualizar producto con nuevo SKU y nueva categoría ──────────────────
    @Test
    void actualizarProducto_NuevoSkuYCategoria_Exito() {
        // Escenario: el gerente actualiza el jabón de avena (SKU viejo "ECO-OLD-05")
        // asignándole el nuevo SKU "ECO-005" y la categoría "Higiene Natural".
        Producto existente = new Producto();
        existente.setIdProducto(5L);
        existente.setSku("ECO-OLD-05");
        existente.setNombre("Jabón de avena artesanal");

        Categoria catHigiene = new Categoria();
        catHigiene.setIdCategoria(2L);
        catHigiene.setNombre("Higiene Natural");

        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-005");
        req.setNombre("Jabón de avena y miel artesanal");
        req.setPrecio(3490.0);
        req.setDescripcionEcologica("Elaborado con avena orgánica certificada");
        req.setEstado("PUBLICADO");
        req.setIdCategoria(2L);

        when(productoRepository.findById(5L)).thenReturn(Optional.of(existente));
        when(productoRepository.existsBySku("ECO-005")).thenReturn(false);
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(catHigiene));
        when(productoRepository.save(any(Producto.class))).thenReturn(existente);

        ProductoResponseDTO resp = catalogoService.actualizarProducto(5L, req);
        assertNotNull(resp);
    }

    // ─── Actualizar producto sin cambiar SKU y sin categoría ──────────────────
    @Test
    void actualizarProducto_MismoSkuSinCategoria_Exito() {
        // Escenario: el gerente actualiza solo el precio de las semillas de chía
        // sin cambiar su SKU ni su categoría.
        Producto existente = new Producto();
        existente.setIdProducto(6L);
        existente.setSku("ECO-006");
        existente.setNombre("Semillas de chía orgánica 500g");

        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-006"); // mismo SKU → no verifica duplicado
        req.setNombre("Semillas de chía orgánica 500g");
        req.setPrecio(2990.0);
        req.setIdCategoria(null); // sin categoría → se setea null

        when(productoRepository.findById(6L)).thenReturn(Optional.of(existente));
        when(productoRepository.save(any(Producto.class))).thenReturn(existente);

        ProductoResponseDTO resp = catalogoService.actualizarProducto(6L, req);
        assertNotNull(resp);
    }

    // ─── Actualizar con SKU duplicado ─────────────────────────────────────────
    @Test
    void actualizarProducto_SkuDuplicado_LanzaConflict() {
        // Escenario: el gerente intenta cambiar el SKU del aceite de coco
        // a "ECO-001" que ya pertenece a otro producto.
        Producto existente = new Producto();
        existente.setIdProducto(7L);
        existente.setSku("ECO-007");
        existente.setNombre("Aceite de coco orgánico 500ml");

        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-001"); // ya existe en otro producto

        when(productoRepository.findById(7L)).thenReturn(Optional.of(existente));
        when(productoRepository.existsBySku("ECO-001")).thenReturn(true);

        assertThrows(ConflictException.class, () -> catalogoService.actualizarProducto(7L, req));
    }

    // ─── Eliminar producto existente ───────────────────────────────────────────
    @Test
    void eliminarProducto_Exito() {
        // Escenario: el administrador descontinúa el desodorante de piedra (id=8)
        // y lo elimina del catálogo.
        when(productoRepository.existsById(8L)).thenReturn(true);
        catalogoService.eliminarProducto(8L);
        verify(productoRepository, times(1)).deleteById(8L);
    }

    // ─── Eliminar producto inexistente ─────────────────────────────────────────
    @Test
    void eliminarProducto_NoExiste_LanzaExcepcion() {
        when(productoRepository.existsById(999L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.eliminarProducto(999L));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PRODUCTOS — BÚSQUEDAS (AC-4 HU-63)
    // ═══════════════════════════════════════════════════════════════════════════

    // ─── Buscar por palabra clave "bambú" ──────────────────────────────────────
    @Test
    void buscarPorPalabraClave_Bambu_RetornaProductosEcologicos() {
        // AC-4: el cliente busca "bambú" en la barra de búsqueda del marketplace.
        // Debe retornar el cepillo y los cubiertos de bambú.
        Producto cepillo = new Producto();
        cepillo.setIdProducto(9L);
        cepillo.setSku("ECO-009");
        cepillo.setNombre("Cepillo de dientes de bambú adulto");
        cepillo.setDescripcionEcologica("Mango de bambú certificado FSC, cerdas de nylon");

        Producto cubiertos = new Producto();
        cubiertos.setIdProducto(3L);
        cubiertos.setSku("ECO-003");
        cubiertos.setNombre("Set cubiertos de bambú x6");

        when(productoRepository
                .findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrDescripcionEcologicaContainingIgnoreCase(
                        "bambú", "bambú", "bambú"))
                .thenReturn(List.of(cepillo, cubiertos));

        List<ProductoResponseDTO> resp = catalogoService.buscarPorPalabraClave("bambú");
        assertFalse(resp.isEmpty());
        assertEquals(2, resp.size());
        assertTrue(resp.stream().anyMatch(r -> r.getSku().equals("ECO-009")));
    }

    // ─── Buscar por categoría ──────────────────────────────────────────────────
    @Test
    void buscarPorCategoria_ProductosBiodegradables_Exito() {
        // Escenario: el cliente filtra productos de la categoría "Productos Biodegradables" (id=1).
        Producto bolsa = new Producto();
        bolsa.setIdProducto(1L);
        bolsa.setSku("ECO-001");
        bolsa.setNombre("Bolsa biodegradable mediana");

        when(productoRepository.findByCategoriaIdCategoria(1L)).thenReturn(List.of(bolsa));

        List<ProductoResponseDTO> resp = catalogoService.buscarPorCategoria(1L);
        assertFalse(resp.isEmpty());
        assertEquals("ECO-001", resp.get(0).getSku());
    }

    // ─── Buscar por rango de precio válido ────────────────────────────────────
    @Test
    void buscarPorPrecio_RangoValido_Exito() {
        // Escenario: el cliente filtra productos entre $1.000 y $5.000 CLP.
        Producto bolsa = new Producto();
        bolsa.setIdProducto(1L);
        bolsa.setSku("ECO-001");
        bolsa.setPrecio(1990.0);

        when(productoRepository.findByPrecioBetween(1000.0, 5000.0)).thenReturn(List.of(bolsa));

        List<ProductoResponseDTO> resp = catalogoService.buscarPorPrecio(1000.0, 5000.0);
        assertFalse(resp.isEmpty());
        assertEquals(1, resp.size());
    }

    // ─── Buscar por precio con rango inválido ─────────────────────────────────
    @Test
    void buscarPorPrecio_RangoInvalido_LanzaExcepcion() {
        // Escenario: se envía precio mínimo negativo, máximo negativo o min > max.
        assertThrows(IllegalArgumentException.class, () -> catalogoService.buscarPorPrecio(-100.0, 5000.0));
        assertThrows(IllegalArgumentException.class, () -> catalogoService.buscarPorPrecio(1000.0, -100.0));
        assertThrows(IllegalArgumentException.class, () -> catalogoService.buscarPorPrecio(5000.0, 1000.0));
    }

    // ─── Buscar productos ecológicos por atributo ──────────────────────────────
    @Test
    void buscarEcologicos_Biodegradable_RetornaProductos() {
        // AC-3: el cliente usa el filtro de eco-certificación buscando "biodegradable".
        Producto bolsa = new Producto();
        bolsa.setIdProducto(1L);
        bolsa.setSku("ECO-001");
        bolsa.setDescripcionEcologica("100% biodegradable en 180 días");

        when(productoRepository.findByDescripcionEcologicaContainingIgnoreCase("biodegradable"))
                .thenReturn(List.of(bolsa));

        List<ProductoResponseDTO> resp = catalogoService.buscarEcologicos("biodegradable");
        assertFalse(resp.isEmpty());
        assertEquals("ECO-001", resp.get(0).getSku());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CATEGORÍAS — CRUD
    // ═══════════════════════════════════════════════════════════════════════════

    // ─── Crear categoría exitosa ───────────────────────────────────────────────
    @Test
    void crearCategoria_Exito() {
        // Escenario: el administrador crea la categoría "Productos Biodegradables" en el catálogo.
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Productos Biodegradables");
        req.setDescripcion("Productos fabricados con materiales que se descomponen naturalmente");
        req.setEstado("ACTIVA");

        when(categoriaRepository.existsByNombreIgnoreCase("Productos Biodegradables")).thenReturn(false);

        Categoria guardada = new Categoria();
        guardada.setIdCategoria(1L);
        guardada.setNombre("Productos Biodegradables");
        guardada.setEstado(EstadoCategoria.ACTIVA);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(guardada);

        CategoriaResponseDTO resp = catalogoService.crearCategoria(req);
        assertEquals(1L, resp.getIdCategoria());
        assertEquals("Productos Biodegradables", resp.getNombre());
        assertEquals("ACTIVA", resp.getEstado());
    }

    // ─── Crear categoría con estado inválido ───────────────────────────────────
    @Test
    void crearCategoria_EstadoInvalido_LanzaExcepcion() {
        // Escenario: se envía estado "BORRADA" que no existe en EstadoCategoria.
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Higiene Natural");
        req.setEstado("BORRADA");
        when(categoriaRepository.existsByNombreIgnoreCase("Higiene Natural")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> catalogoService.crearCategoria(req));
    }

    // ─── Crear categoría con nombre duplicado ──────────────────────────────────
    @Test
    void crearCategoria_NombreDuplicado_LanzaConflict() {
        // Escenario: ya existe "Hogar Sustentable" y se intenta crear otra con el mismo nombre.
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Hogar Sustentable");
        when(categoriaRepository.existsByNombreIgnoreCase("Hogar Sustentable")).thenReturn(true);
        assertThrows(ConflictException.class, () -> catalogoService.crearCategoria(req));
    }

    // ─── Listar todas las categorías ──────────────────────────────────────────
    @Test
    void obtenerTodasCategorias_Exito() {
        // Escenario: el catálogo tiene 3 categorías activas disponibles para los clientes.
        Categoria c1 = new Categoria();
        c1.setIdCategoria(1L);
        c1.setNombre("Productos Biodegradables");
        c1.setEstado(EstadoCategoria.ACTIVA);

        Categoria c2 = new Categoria();
        c2.setIdCategoria(2L);
        c2.setNombre("Higiene Natural");
        c2.setEstado(EstadoCategoria.ACTIVA);

        when(categoriaRepository.findAll()).thenReturn(List.of(c1, c2));
        List<CategoriaResponseDTO> resp = catalogoService.obtenerTodasCategorias();
        assertEquals(2, resp.size());
        assertEquals("Productos Biodegradables", resp.get(0).getNombre());
    }

    // ─── Obtener categoría por ID ──────────────────────────────────────────────
    @Test
    void obtenerCategoriaPorId_Exito() {
        // Escenario: el cliente consulta el detalle de la categoría "Hogar Sustentable" (id=3).
        Categoria c = new Categoria();
        c.setIdCategoria(3L);
        c.setNombre("Hogar Sustentable");
        c.setEstado(EstadoCategoria.ACTIVA);
        when(categoriaRepository.findById(3L)).thenReturn(Optional.of(c));

        CategoriaResponseDTO resp = catalogoService.obtenerCategoriaPorId(3L);
        assertNotNull(resp);
        assertEquals("Hogar Sustentable", resp.getNombre());
    }

    // ─── Actualizar categoría exitosa ──────────────────────────────────────────
    @Test
    void actualizarCategoria_Exito() {
        // Escenario: el administrador actualiza la descripción de la categoría "Higiene Natural".
        Categoria existente = new Categoria();
        existente.setIdCategoria(2L);
        existente.setNombre("Higiene Natural");

        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Higiene y Cuidado Natural");
        req.setDescripcion("Productos de higiene personal libres de químicos");
        req.setEstado("ACTIVA");

        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.existsByNombreIgnoreCase("Higiene y Cuidado Natural")).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(existente);

        CategoriaResponseDTO resp = catalogoService.actualizarCategoria(2L, req);
        assertNotNull(resp);
    }

    // ─── Actualizar categoría con nombre duplicado ────────────────────────────
    @Test
    void actualizarCategoria_NombreDuplicado_LanzaConflict() {
        // Escenario: se intenta renombrar "Hogar Sustentable" a "Higiene Natural"
        // que ya existe como otra categoría.
        Categoria existente = new Categoria();
        existente.setIdCategoria(3L);
        existente.setNombre("Hogar Sustentable");

        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Higiene Natural"); // nombre que ya existe

        when(categoriaRepository.findById(3L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.existsByNombreIgnoreCase("Higiene Natural")).thenReturn(true);

        assertThrows(ConflictException.class, () -> catalogoService.actualizarCategoria(3L, req));
    }

    // ─── Eliminar categoría sin productos ─────────────────────────────────────
    @Test
    void eliminarCategoria_SinProductos_Exito() {
        // Escenario: se elimina la categoría "Semillas y Superalimentos" (id=4)
        // que no tiene productos asociados.
        when(categoriaRepository.existsById(4L)).thenReturn(true);
        when(productoRepository.findByCategoriaIdCategoria(4L)).thenReturn(Collections.emptyList());

        catalogoService.eliminarCategoria(4L);
        verify(categoriaRepository, times(1)).deleteById(4L);
    }

    // ─── Eliminar categoría inexistente ───────────────────────────────────────
    @Test
    void eliminarCategoria_NoExiste_LanzaExcepcion() {
        when(categoriaRepository.existsById(999L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.eliminarCategoria(999L));
    }

    // ─── Eliminar categoría con productos asociados ────────────────────────────
    @Test
    void eliminarCategoria_ConProductosAsociados_LanzaConflict() {
        // Escenario: se intenta eliminar "Productos Biodegradables" (id=1)
        // que aún tiene la bolsa biodegradable asociada → debe bloquearse.
        Producto bolsa = new Producto();
        bolsa.setIdProducto(1L);
        bolsa.setSku("ECO-001");
        bolsa.setNombre("Bolsa biodegradable mediana");

        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.findByCategoriaIdCategoria(1L)).thenReturn(List.of(bolsa));

        assertThrows(ConflictException.class, () -> catalogoService.eliminarCategoria(1L));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // RESEÑAS — CRUD + REGLA DE NEGOCIO (AC-5 y AC-6)
    // ═══════════════════════════════════════════════════════════════════════════

    // ─── AC-5: Cliente que SÍ compró puede dejar reseña ──────────────────────
    @Test
    void crearResena_ClienteQueCompro_Exito() {
        // AC-5: la cliente María Fernández (idCliente=12) compró el aceite esencial
        // de lavanda (idProducto=15) y deja una reseña de 5 estrellas.
        ResenaRequestDTO req = new ResenaRequestDTO();
        req.setIdProducto(15L);
        req.setIdCliente(12L);
        req.setCalificacion(5);
        req.setComentario("Excelente calidad, el aroma es increíble y duró todo el día");

        Producto lavanda = new Producto();
        lavanda.setIdProducto(15L);
        lavanda.setSku("ECO-015");
        lavanda.setNombre("Aceite esencial de lavanda orgánico 30ml");

        when(productoRepository.findById(15L)).thenReturn(Optional.of(lavanda));
        when(pedidosClientService.verificarCompra(12L, 15L)).thenReturn(true);

        Resena guardada = new Resena();
        guardada.setIdResena(1L);
        guardada.setIdCliente(12L);
        guardada.setProducto(lavanda);
        guardada.setCalificacion(5);
        guardada.setComentario("Excelente calidad, el aroma es increíble y duró todo el día");
        guardada.setEstado(EstadoResena.PUBLICADA);
        when(resenaRepository.save(any(Resena.class))).thenReturn(guardada);

        ResenaResponseDTO resp = catalogoService.crearResena(req);
        assertEquals(1L, resp.getIdResena());
        assertEquals(5, resp.getCalificacion());
        assertEquals("PUBLICADA", resp.getEstado());
        assertEquals(15L, resp.getIdProducto());
    }

    // ─── AC-5: Cliente que NO compró no puede dejar reseña ────────────────────
    @Test
    void crearResena_ClienteQueNoCompro_LanzaConflict() {
        // AC-5 (Regla crítica): el cliente Carlos Vega (idCliente=20) intenta reseñar
        // la bolsa biodegradable (idProducto=1) que nunca compró → debe ser rechazado.
        ResenaRequestDTO req = new ResenaRequestDTO();
        req.setIdProducto(1L);
        req.setIdCliente(20L);
        req.setCalificacion(3);
        req.setComentario("Se ve bien en la foto");

        Producto bolsa = new Producto();
        bolsa.setIdProducto(1L);
        bolsa.setSku("ECO-001");
        bolsa.setNombre("Bolsa biodegradable mediana");

        when(productoRepository.findById(1L)).thenReturn(Optional.of(bolsa));
        when(pedidosClientService.verificarCompra(20L, 1L)).thenReturn(false);

        assertThrows(ConflictException.class, () -> catalogoService.crearResena(req));
    }

    // ─── Listar todas las reseñas ──────────────────────────────────────────────
    @Test
    void obtenerTodasResenas_Exito() {
        // Escenario: el administrador consulta el panel de reseñas del catálogo.
        Resena r = new Resena();
        r.setIdResena(1L);
        r.setIdCliente(12L);
        r.setCalificacion(5);
        r.setEstado(EstadoResena.PUBLICADA);
        when(resenaRepository.findAll()).thenReturn(List.of(r));

        List<ResenaResponseDTO> resp = catalogoService.obtenerTodasResenas();
        assertFalse(resp.isEmpty());
        assertEquals(1, resp.size());
    }

    // ─── Obtener reseña por ID ─────────────────────────────────────────────────
    @Test
    void obtenerResenaPorId_Exito() {
        // Escenario: se consulta la reseña de 5 estrellas dejada por María Fernández.
        Resena r = new Resena();
        r.setIdResena(1L);
        r.setIdCliente(12L);
        r.setCalificacion(5);
        r.setEstado(EstadoResena.PUBLICADA);
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(r));

        ResenaResponseDTO resp = catalogoService.obtenerResenaPorId(1L);
        assertNotNull(resp);
        assertEquals(1L, resp.getIdResena());
        assertEquals(5, resp.getCalificacion());
    }

    // ─── Eliminar reseña existente ─────────────────────────────────────────────
    @Test
    void eliminarResena_Exito() {
        // Escenario: el moderador elimina una reseña reportada como inapropiada.
        when(resenaRepository.existsById(1L)).thenReturn(true);
        catalogoService.eliminarResena(1L);
        verify(resenaRepository, times(1)).deleteById(1L);
    }

    // ─── Eliminar reseña inexistente ───────────────────────────────────────────
    @Test
    void eliminarResena_NoExiste_LanzaExcepcion() {
        when(resenaRepository.existsById(999L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.eliminarResena(999L));
    }

    // ─── AC-6: Promedio de calificaciones con HALF_UP a 1 decimal ────────────
    @Test
    void calcularPromedio_TresResenas_ResultadoCuatroPointCero() {
        // AC-6: el set de cubiertos de bambú tiene 3 reseñas: 4, 5 y 3 estrellas.
        // Promedio: (4+5+3)/3 = 4.0 → con HALF_UP a 1 decimal = 4.0
        Resena r1 = new Resena(); r1.setCalificacion(4); // "Muy buenos cubiertos"
        Resena r2 = new Resena(); r2.setCalificacion(5); // "Los mejores que he comprado"
        Resena r3 = new Resena(); r3.setCalificacion(3); // "Bien, pero algo frágiles"
        when(resenaRepository.findByProductoIdProducto(3L)).thenReturn(List.of(r1, r2, r3));

        Double promedio = catalogoService.calcularPromedioCalificaciones(3L);
        assertEquals(4.0, promedio);
    }

    // ─── Promedio sin reseñas → retorna 0.0 ──────────────────────────────────
    @Test
    void calcularPromedio_SinResenas_RetornaCero() {
        // Escenario: la botella de agua reutilizable es nueva y aún no tiene reseñas.
        when(resenaRepository.findByProductoIdProducto(2L)).thenReturn(Collections.emptyList());
        Double promedio = catalogoService.calcularPromedioCalificaciones(2L);
        assertEquals(0.0, promedio);
    }

    // ─── resolverEstadoProducto: estado null/blank → default PUBLICADO ─────────
    @Test
    void crearProducto_EstadoBlank_DefaultPublicado() {
        // Escenario: se crea el filtro de agua (ECO-020) con estado en blanco.
        // El sistema debe asignar automáticamente PUBLICADO como estado por defecto.
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-020");
        req.setNombre("Filtro de agua de carbón activado");
        req.setPrecio(24990.0);
        req.setEstado(""); // blank → default PUBLICADO

        when(productoRepository.existsBySku("ECO-020")).thenReturn(false);

        Producto guardado = new Producto();
        guardado.setIdProducto(20L);
        guardado.setSku("ECO-020");
        guardado.setEstado(EstadoProducto.PUBLICADO);
        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        ProductoResponseDTO resp = catalogoService.crearProducto(req);
        assertEquals("PUBLICADO", resp.getEstado());
    }

    // ─── resolverEstadoCategoria: estado null/blank → default ACTIVA ──────────
    @Test
    void crearCategoria_EstadoNull_DefaultActiva() {
        // Escenario: se crea la categoría "Alimentos Orgánicos" sin especificar estado.
        // El sistema debe asignarle ACTIVA por defecto.
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Alimentos Orgánicos");
        req.setEstado(null); // null → default ACTIVA

        when(categoriaRepository.existsByNombreIgnoreCase("Alimentos Orgánicos")).thenReturn(false);

        Categoria guardada = new Categoria();
        guardada.setIdCategoria(5L);
        guardada.setNombre("Alimentos Orgánicos");
        guardada.setEstado(EstadoCategoria.ACTIVA);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(guardada);

        CategoriaResponseDTO resp = catalogoService.crearCategoria(req);
        assertEquals("ACTIVA", resp.getEstado());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TESTS ADICIONALES PARA ALCANZAR 100% COBERTURA
    // ═══════════════════════════════════════════════════════════════════════════

    @Test
    void actualizarProducto_NoExiste_LanzaExcepcion() {
        ProductoRequestDTO req = new ProductoRequestDTO();
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.actualizarProducto(999L, req));
    }

    @Test
    void actualizarProducto_CategoriaNoExiste_LanzaExcepcion() {
        Producto p = new Producto();
        p.setIdProducto(1L);
        p.setSku("ECO-001");
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-001");
        req.setNombre("Nombre válido"); // Avoid NPE in req.getNombre().trim()
        req.setIdCategoria(999L);
        
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.actualizarProducto(1L, req));
    }

    @Test
    void obtenerCategoriaPorId_NoExiste_LanzaExcepcion() {
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.obtenerCategoriaPorId(999L));
    }

    @Test
    void actualizarCategoria_NoExiste_LanzaExcepcion() {
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.actualizarCategoria(999L, req));
    }

    @Test
    void crearResena_ProductoNoExiste_LanzaExcepcion() {
        ResenaRequestDTO req = new ResenaRequestDTO();
        req.setIdProducto(999L);
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.crearResena(req));
    }

    @Test
    void obtenerResenaPorId_NoExiste_LanzaExcepcion() {
        when(resenaRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> catalogoService.obtenerResenaPorId(999L));
    }

    @Test
    void mapearProducto_NullEstado_NullCategoria() {
        // Escenario para cubrir ramas null en mapeo
        Producto p = new Producto();
        p.setIdProducto(100L);
        p.setEstado(null);
        p.setCategoria(null);
        when(productoRepository.findById(100L)).thenReturn(Optional.of(p));
        ProductoResponseDTO resp = catalogoService.obtenerProductoPorId(100L);
        assertNull(resp.getEstado());
        assertNull(resp.getIdCategoria());
    }

    @Test
    void mapearCategoria_NullEstado() {
        Categoria c = new Categoria();
        c.setIdCategoria(100L);
        c.setEstado(null);
        when(categoriaRepository.findById(100L)).thenReturn(Optional.of(c));
        CategoriaResponseDTO resp = catalogoService.obtenerCategoriaPorId(100L);
        assertNull(resp.getEstado());
    }

    @Test
    void mapearResena_NullEstado_NullProducto() {
        Resena r = new Resena();
        r.setIdResena(100L);
        r.setEstado(null);
        r.setProducto(null);
        when(resenaRepository.findById(100L)).thenReturn(Optional.of(r));
        ResenaResponseDTO resp = catalogoService.obtenerResenaPorId(100L);
        assertNull(resp.getEstado());
        assertNull(resp.getIdProducto());
    }
}
