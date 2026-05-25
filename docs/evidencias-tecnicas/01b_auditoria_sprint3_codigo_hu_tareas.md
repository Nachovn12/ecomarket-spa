# Auditoria Sprint 3 — Codigo, HU y Tareas

## EcoMarket SPA — DSY1103

Este documento registra la revision del Sprint 3 no solo desde la documentacion, sino tambien desde el codigo fuente disponible en la rama `develop` de GitHub.

Sprint revisado:

| Campo | Detalle |
|---|---|
| Sprint | S3 - Inventario y Logistica |
| Objetivo | Validar integracion entre microservicios, completar funcionalidades logisticas, implementar pruebas unitarias y documentar navegacion HATEOAS |
| Rama revisada | `develop` |
| Resultado Jira | 22 issues finalizadas |
| Criterio de revision | Cada HU/Tarea debe tener respaldo en codigo, endpoints, pruebas o evidencia tecnica |

---

## Criterios usados para validar cada HU/Tarea

Para considerar una HU/Tarea como reflejada se reviso:

1. Existencia de clases, entidades, DTO, repositorios, servicios y controladores.
2. Existencia de reglas de negocio asociadas al criterio de aceptacion.
3. Existencia de endpoints REST.
4. Uso de HATEOAS cuando corresponde.
5. Evidencia Postman o documentacion tecnica asociada.
6. Pruebas JUnit o evidencia de `mvn clean test`.

---

## Matriz Sprint 3 — Validacion por codigo y evidencia

| Issue | Tipo | Microservicio | Validacion en codigo develop | Evidencia/documentacion | Estado tecnico |
|---|---|---|---|---|---|
| HU-6 | Historia | MS Catalogo | `ProductoController`, `CatalogoService`, `ProductoRepository` implementan busqueda por palabra clave, categoria, precio y atributo ecologico | `docs/postman/evidencia-logistica-catalogo-sprint3.md` | Reflejada |
| HU-7 | Historia | MS Pedidos y Ventas | `CarritoService` permite crear carrito, agregar items, validar stock, actualizar cantidad, eliminar item y recalcular totales | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-10 | Historia | MS Pedidos y Ventas | `PedidoService.historialCliente()` consulta pedidos por cliente ordenados por fecha; `listarHistorialPedido()` registra trazabilidad | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-11 | Historia | MS Pedidos y Ventas | `PedidoService.crearDesdeCarrito()` crea pedido desde carrito, cambia estado del carrito y registra historial | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-13 | Historia | MS Pedidos y Ventas | `PedidoService.cancelarPedido()` restringe cancelacion a estado `PENDIENTE` y registra historial | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-14 | Historia | MS Pedidos y Ventas | `VentaService.registrarVentaPresencial()` calcula subtotal, descuento y total desde items | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-17 | Historia | MS Inventario y Abastecimiento | `AjusteStockService`, `AjusteStockController` y `AjusteStockRequestDTO` implementan ajuste manual, motivo obligatorio e historial por producto | `MsInventarioAbastecimientoApplicationTests`, build/test evidence | Reflejada |
| HU-18 | Historia | MS Inventario y Abastecimiento | `PedidoReabastecimientoService` implementa crear, aprobar, rechazar con motivo y consultar/listar pedidos | `MsInventarioAbastecimientoApplicationTests`, build/test evidence | Reflejada |
| HU-20 | Historia | MS Logistica de Envios | `LogisticaService.crearEnvio()`, `obtenerEnvios()`, `actualizarEnvio()` y `obtenerEnviosPorPedido()` implementan gestion de envios | `docs/postman/evidencia-logistica-catalogo-sprint3.md` | Reflejada |
| HU-22 | Historia | MS Logistica de Envios | `LogisticaService.cambiarEstadoEnvio()` y `registrarIncidencia()` implementan cambio de estado e incidencias | `docs/postman/evidencia-logistica-catalogo-sprint3.md` | Reflejada |
| HU-23 | Historia | MS Logistica de Envios | `crearProveedor()`, `actualizarProveedor()`, `activarProveedor()`, `desactivarProveedor()`, `obtenerProveedoresActivos()` y `buscarProveedores()` cubren proveedores | `docs/postman/evidencia-logistica-catalogo-sprint3.md` | Reflejada |
| HU-24 | Historia | MS Inventario y Abastecimiento | `RecepcionMercanciaService.registrarRecepcion()` valida pedido aprobado, cantidades, danios, diferencias y actualiza stock aceptado | `MsInventarioAbastecimientoApplicationTests`, build/test evidence | Reflejada |
| HU-36 | Historia | MS Pedidos y Ventas | `VentaService.generarFactura()` genera factura, folio y evita duplicar factura por venta | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-45 | Tarea | MS Pedidos y Ventas | Existen entidades, repositorios, servicios y controladores para carrito, pedidos, ventas, cupones, facturas, devoluciones y reclamaciones | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-46 | Tarea | MS Pedidos y Ventas | Endpoints del flujo compra/venta implementados en `CarritoController`, `PedidoController`, `VentaController` y servicios asociados | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-49 | Tarea | MS Catalogo | Existen `Producto`, `Categoria`, `Resena`, repositories, `CatalogoService` y controllers REST | `docs/postman/evidencia-logistica-catalogo-sprint3.md` | Reflejada |
| HU-50 | Tarea | Integracion | Se documenta comunicacion REST entre Gateway y microservicios principales | `docs/evidencias/integracion/evidencia-postman-hu50.md` | Reflejada documentalmente |
| HU-51 | Tarea | Transversal | HATEOAS implementado en controllers principales y documentado conceptualmente | `docs/hateoas/documentacion-hateoas-base.md` | Reflejada |
| HU-52 | Tarea | MS Pedidos y Ventas | Tests JUnit del MS Pedidos y Ventas registrados en evidencia de build/tests | `docs/evidencias/evidencia-build-tests.md` | Reflejada |
| HU-53 | Tarea | MS Inventario y Abastecimiento | Tests JUnit del MS Inventario y Abastecimiento registrados en evidencia de build/tests | `docs/evidencias/evidencia-build-tests.md` | Reflejada |
| HU-54 | Tarea | MS Logistica de Envios | CRUD y reglas principales implementadas en `EnvioController`, `ProveedorController`, `RutaEntregaController` y `LogisticaService` | `docs/postman/evidencia-logistica-catalogo-sprint3.md` | Reflejada |
| HU-55 | Tarea | MS Catalogo / MS Logistica | Endpoints de catalogo y logistica documentados con codigos HTTP esperados | `docs/postman/evidencia-logistica-catalogo-sprint3.md` | Reflejada |

---

## Correcciones realizadas en develop

- Se corrigio el puerto del documento de flujo comercial desde `8084` a `8086`, porque `8084` corresponde a MS Catalogo y `8086` corresponde a MS Pedidos y Ventas.
- Se amplio `docs/postman/evidencia-logistica-catalogo-sprint3.md` para incluir endpoints de MS Catalogo, busqueda, categorias y resenas, no solo Logistica.
- Se agrega este documento para dejar trazabilidad de revision por codigo, HU y tareas del Sprint 3.

---

## Veredicto Sprint 3

El Sprint 3 queda reflejado en GitHub `develop` a nivel de:

- Codigo fuente Spring Boot.
- Entidades, DTO, repositorios, servicios y controladores.
- Endpoints REST.
- HATEOAS en controllers representativos.
- Evidencias Postman.
- Evidencias JUnit/Maven.
- Auditoria por HU/Tarea.

Resultado final:

```text
Sprint 3 validado tecnicamente en develop.
22 issues revisadas.
22 issues con respaldo en codigo, documentacion tecnica o evidencia de pruebas.
```

La auditoría técnica consolida el alcance validado del Sprint 3 y respalda la trazabilidad entre Jira, código fuente, evidencias Postman, pruebas y documentación.
