package com.ecomarket.catalogo.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Cliente REST para comunicacion con MS pedidos y ventas.
 * Implementa IE 2.4.1 (Comunicacion entre MS) con manejo de timeout y errores remotos.
 */
@Service
@RequiredArgsConstructor
public class PedidosClientService {

    private static final Logger log = LoggerFactory.getLogger(PedidosClientService.class);

    private final RestTemplate restTemplate;

    @Value("${ms.pedidos.url}")
    private String msPedidosUrl;

    /**
     * Verifica la conectividad con el MS pedidos y ventas.
     * GET /api/pedidos
     */
    public Map<String, Object> ping() {
        String url = msPedidosUrl + "/api/pedidos";
        log.info("Llamando a MS Pedidos y Ventas. url={}", url);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Rol-Usuario", "SISTEMA");
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
            log.info("MS Pedidos y Ventas respondio OK");
            return response;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("MS Pedidos y Ventas respondio 404");
            return null;
        } catch (ResourceAccessException e) {
            log.error("MS Pedidos y Ventas no disponible. error={}", e.getMessage());
            throw new IllegalStateException("MS Pedidos y Ventas no esta disponible. Intente nuevamente en unos momentos.");
        } catch (Exception e) {
            log.error("Error inesperado al llamar a MS Pedidos y Ventas. error={}", e.getMessage());
            throw new IllegalStateException("No se pudo comunicar con MS Pedidos y Ventas.");
        }
    }

    /**
     * Verifica si un cliente ha comprado un producto especifico.
     * Regla critica: Solo clientes que compraron el producto pueden dejar resena.
     */
    public boolean verificarCompra(Long idCliente, Long idProducto) {
        String url = msPedidosUrl + "/api/pedidos/verificar-compra?idCliente=" + idCliente + "&idProducto=" + idProducto;
        log.info("Verificando compra del cliente {} para el producto {}. url={}", idCliente, idProducto, url);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Rol-Usuario", "SISTEMA");
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            // Suponemos que MS Pedidos expone un endpoint booleano para esto
            Boolean compro = restTemplate.exchange(url, HttpMethod.GET, entity, Boolean.class).getBody();
            return Boolean.TRUE.equals(compro);
        } catch (Exception e) {
            log.error("Error al verificar compra en MS Pedidos. error={}", e.getMessage());
            return false;
        }
    }
}
