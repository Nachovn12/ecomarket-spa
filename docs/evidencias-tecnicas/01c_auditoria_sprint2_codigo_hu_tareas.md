# Auditoría Sprint 2 — Código, HU y Tareas

## EcoMarket SPA — DSY1103

Este documento registra la revisión documental y técnica del Sprint 2 desde la rama `develop`, consolidando la relación entre issues de Jira, código fuente, endpoints REST, configuración, Postman y documentación técnica.

## Sprint revisado

| Campo | Detalle |
|---|---|
| Sprint | S2 - Compra y Ventas |
| Objetivo | Implementar y preparar las bases técnicas del flujo comercial principal de EcoMarket SPA, incluyendo compra, venta, inventario, catálogo, API Gateway y validación inicial mediante Postman |
| Rama revisada | `develop` |
| Resultado Jira | 15 issues finalizadas |
| Criterio de revisión | Cada HU/Tarea debe tener respaldo en código, endpoints, configuración, evidencia Postman o documentación técnica |

---

## Criterios usados para validar cada HU/Tarea

Para considerar una HU/Tarea como reflejada se revisó:

1. Código real en `develop`.
2. Entidades, DTO, repositorios, servicios y controladores.
3. Endpoints REST.
4. HATEOAS cuando corresponde.
5. Evidencia Postman.
6. Configuración de BD.
7. Documentación técnica.

---

## Matriz Sprint 2 — Validación por código y evidencia

| Issue | Tipo | Microservicio/Componente | Validación en código develop | Evidencia/documentación | Estado técnico |
|---|---|---|---|---|---|
| HU-6 | Historia | MS Catálogo | `ProductoController`, `CategoriaController`, `ResenaController`, `CatalogoService`, `Producto`, `Categoria`, `Resena` y repositories respaldan búsqueda y gestión base de productos ecológicos | `docs/postman/evidencia-postman.md`; colección Postman principal | Reflejada |
| HU-7 | Historia | MS Pedidos y Ventas | `CarritoService`, `CarritoController`, `CarritoCompra` e `ItemCarrito` respaldan creación de carrito y gestión de productos asociados | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md`; colección Postman principal | Reflejada |
| HU-8 | Historia | MS Pedidos y Ventas | `CuponDescuentoService` y flujo de carrito/venta permiten aplicar cupones y recalcular totales del flujo comercial | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md`; colección Postman principal | Reflejada |
| HU-10 | Historia | MS Pedidos y Ventas | `PedidoService` y `PedidoController` respaldan consulta de historial y trazabilidad de pedidos por cliente | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-11 | Historia | MS Pedidos y Ventas | `PedidoService`, `PedidoController`, `Pedido`, `CarritoCompra` e `ItemCarrito` respaldan creación de pedido online desde carrito | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-13 | Historia | MS Pedidos y Ventas | `PedidoService` y `PedidoController` respaldan cancelación de pedidos pendientes y control de estado | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-14 | Historia | MS Pedidos y Ventas | `VentaService`, `VentaController` y `Venta` respaldan registro de venta presencial, cálculo de subtotal, descuentos y total | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-36 | Historia | MS Pedidos y Ventas | `VentaService` y `Factura` respaldan generación académica de factura asociada a una venta | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-43 | Tarea | API Gateway | `api-gateway/src/main/resources/application.properties` respalda configuración de rutas base hacia microservicios | `docs/api-gateway-rutas.md` | Reflejada |
| HU-44 | Tarea | Postman | La colección `EcoMarket-SPA.postman_collection.json` y el ambiente local consolidan requests y variables por microservicio | `postman/EcoMarket-SPA.postman_collection.json`; `postman/EcoMarket-SPA-local.postman_environment.json` | Reflejada |
| HU-45 | Tarea | MS Pedidos y Ventas | Servicios, controladores y entidades de carrito, cupones, pedidos, ventas y facturas respaldan CRUD y flujo comercial | `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md`; `docs/postman/evidencia-postman.md` | Reflejada |
| HU-46 | Tarea | Postman / Flujo comercial | La colección Postman y la documentación del flujo compra/venta respaldan validación manual de endpoints principales | `docs/postman/evidencia-postman.md`; `docs/pedidos-ventas/flujo-comercial-facturacion-electronica.md` | Reflejada |
| HU-47 | Tarea | MS Inventario y Abastecimiento | `InventarioController`, `AjusteStockController`, `PedidoReabastecimientoController`, `RecepcionMercanciaController` y servicios asociados respaldan CRUD y reglas base | Código fuente de `ms-inventario-abastecimiento` | Reflejada |
| HU-48 | Tarea | MS Inventario y Abastecimiento | `application.properties` del microservicio respalda configuración de base de datos local para inventario | `ms-inventario-abastecimiento/src/main/resources/application.properties` | Reflejada |
| HU-49 | Tarea | MS Catálogo | `ProductoController`, `CategoriaController`, `ResenaController`, `CatalogoService`, modelos y repositories respaldan CRUD del catálogo | Código fuente de `ms-catalogo`; `docs/postman/evidencia-postman.md` | Reflejada |

---

## Veredicto Sprint 2

Sprint 2 queda reflejado en GitHub `develop` a nivel de código fuente, endpoints REST, servicios, controladores, persistencia, API Gateway, Postman y documentación técnica.

Resultado:

```text
Sprint 2 validado técnicamente en develop.
15 issues revisadas.
15 issues con respaldo en código, documentación técnica o evidencia de pruebas.
```
