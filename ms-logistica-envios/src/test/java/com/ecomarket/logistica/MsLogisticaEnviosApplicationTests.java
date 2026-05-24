package com.ecomarket.logistica;

import com.ecomarket.logistica.dto.CambioEstadoRequestDTO;
import com.ecomarket.logistica.dto.EnvioDTO;
import com.ecomarket.logistica.dto.IncidenciaRequestDTO;
import com.ecomarket.logistica.dto.ProveedorDTO;
import com.ecomarket.logistica.dto.RutaEntregaDTO;
import com.ecomarket.logistica.exception.ConflictoNegocioException;
import com.ecomarket.logistica.exception.ResourceNotFoundException;
import com.ecomarket.logistica.model.Envio;
import com.ecomarket.logistica.model.Proveedor;
import com.ecomarket.logistica.model.RutaEntrega;
import com.ecomarket.logistica.model.enums.EstadoEnvio;
import com.ecomarket.logistica.model.enums.EstadoRuta;
import com.ecomarket.logistica.repository.EnvioRepository;
import com.ecomarket.logistica.repository.ProveedorRepository;
import com.ecomarket.logistica.repository.RutaEntregaRepository;
import com.ecomarket.logistica.service.LogisticaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MsLogisticaEnviosApplicationTests {

    @Autowired
    private LogisticaService logisticaService;

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private RutaEntregaRepository rutaEntregaRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void crearProveedorValido() {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setRazonSocial("Proveedor Eco Test");
        dto.setRut("11111111-1");
        dto.setContacto("Contacto Test");
        dto.setEmail("proveedor@test.cl");
        dto.setTelefono("+56912345678");
        dto.setTipoProveedor("TRANSPORTE");
        dto.setCobertura("SANTIAGO");

        Proveedor proveedor = logisticaService.crearProveedor(dto);

        assertNotNull(proveedor.getId());
        assertEquals("Proveedor Eco Test", proveedor.getRazonSocial());
        assertEquals("11111111-1", proveedor.getRut());
        assertTrue(proveedor.getActivo());
    }

    @Test
    void crearEnvioValido() {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdPedido(1L);
        dto.setOrigen("Bodega Central Santiago");
        dto.setDestino("Tienda Lastarria");
        dto.setFechaEstimadaEntrega(LocalDateTime.now().plusDays(2));

        Envio creado = logisticaService.crearEnvio(dto);

        assertNotNull(creado.getId());
        assertEquals(EstadoEnvio.PREPARADO, creado.getEstado());
        assertEquals(1L, creado.getIdPedido());
        assertEquals("Bodega Central Santiago", creado.getOrigen());
        assertEquals("Tienda Lastarria", creado.getDestino());
    }

    @Test
    void crearEnvioSinIdPedidoDebeFallar() {
        EnvioDTO dto = new EnvioDTO();
        dto.setOrigen("Bodega Central Santiago");
        dto.setDestino("Tienda Lastarria");
        dto.setFechaEstimadaEntrega(LocalDateTime.now().plusDays(2));

        assertThrows(IllegalArgumentException.class, () ->
                logisticaService.crearEnvio(dto)
        );
    }

    @Test
    void crearEnvioConProveedorInactivoDebeFallar() {
        Proveedor proveedor = new Proveedor();
        proveedor.setRazonSocial("Proveedor Inactivo Test");
        proveedor.setRut("22222222-2");
        proveedor.setTipoProveedor("TRANSPORTE");
        proveedor.setCobertura("VALDIVIA");
        proveedor.setActivo(false);

        Proveedor guardado = proveedorRepository.save(proveedor);

        EnvioDTO dto = new EnvioDTO();
        dto.setIdPedido(2L);
        dto.setOrigen("Bodega Central");
        dto.setDestino("Tienda Valdivia");
        dto.setFechaEstimadaEntrega(LocalDateTime.now().plusDays(3));
        dto.setProveedorId(guardado.getId());

        assertThrows(ConflictoNegocioException.class, () ->
                logisticaService.crearEnvio(dto)
        );
    }

    @Test
    void noPermitirIncidenciaEnEnvioEntregado() {
        Envio envio = new Envio();
        envio.setIdPedido(10L);
        envio.setOrigen("Bodega Central");
        envio.setDestino("Cliente Web");
        envio.setFechaEstimadaEntrega(LocalDateTime.now().plusDays(1));
        envio.setEstado(EstadoEnvio.ENTREGADO);

        Envio guardado = envioRepository.save(envio);

        IncidenciaRequestDTO incidencia = new IncidenciaRequestDTO();
        incidencia.setMotivoIncidencia("Cliente no encontrado");
        incidencia.setActualizadoPor("tester");
        incidencia.setObservacion("No deberia permitir incidencia");

        assertThrows(ConflictoNegocioException.class, () ->
                logisticaService.registrarIncidencia(guardado.getId(), incidencia)
        );
    }

    @Test
    void noPermitirCambiarEstadoEnvioEntregado() {
        Envio envio = new Envio();
        envio.setIdPedido(20L);
        envio.setOrigen("Bodega Central");
        envio.setDestino("Cliente Web");
        envio.setFechaEstimadaEntrega(LocalDateTime.now().plusDays(1));
        envio.setEstado(EstadoEnvio.ENTREGADO);

        Envio guardado = envioRepository.save(envio);

        CambioEstadoRequestDTO cambio = new CambioEstadoRequestDTO();
        cambio.setEstado(EstadoEnvio.EN_CAMINO);
        cambio.setActualizadoPor("tester");

        assertThrows(ConflictoNegocioException.class, () ->
                logisticaService.cambiarEstadoEnvio(guardado.getId(), cambio)
        );
    }

    @Test
    void obtenerEnvioInexistenteDebeFallar() {
        assertThrows(ResourceNotFoundException.class, () ->
                logisticaService.obtenerEnvioPorId(999999L)
        );
    }

    @Test
    void crearRutaConEstadoPorDefecto() {
        RutaEntregaDTO dto = new RutaEntregaDTO();

        RutaEntrega ruta = logisticaService.crearRuta(dto);

        assertNotNull(ruta.getId());
        assertEquals(EstadoRuta.PLANIFICADA, ruta.getEstado());
    }

    @Test
    void crearRutaConEstadoValido() {
        RutaEntregaDTO dto = new RutaEntregaDTO();
        dto.setEstado(EstadoRuta.OPTIMIZADA);

        RutaEntrega ruta = logisticaService.crearRuta(dto);

        assertNotNull(ruta.getId());
        assertEquals(EstadoRuta.OPTIMIZADA, ruta.getEstado());
    }

    @Test
    void cambiarEstadoRutaValido() {
        RutaEntrega ruta = new RutaEntrega();
        ruta.setEstado(EstadoRuta.PLANIFICADA);
        RutaEntrega guardada = rutaEntregaRepository.save(ruta);

        RutaEntrega actualizada = logisticaService.cambiarEstadoRuta(
                guardada.getId(),
                EstadoRuta.EN_CURSO
        );

        assertEquals(EstadoRuta.EN_CURSO, actualizada.getEstado());
    }

    @Test
    void noPermitirCambiarEstadoRutaFinalizada() {
        RutaEntrega ruta = new RutaEntrega();
        ruta.setEstado(EstadoRuta.FINALIZADA);
        RutaEntrega guardada = rutaEntregaRepository.save(ruta);

        assertThrows(ConflictoNegocioException.class, () ->
                logisticaService.cambiarEstadoRuta(guardada.getId(), EstadoRuta.EN_CURSO)
        );
    }
}
