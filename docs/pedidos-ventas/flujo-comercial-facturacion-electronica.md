# Flujo comercial y facturación electrónica — MS Pedidos y Ventas

## EcoMarket SPA — Sprint 3 y Sprint 4

> Microservicio: `ms-pedidos-ventas`  
> Puerto: `8086`  
> Base de datos: MySQL/XAMPP — `bd_ventas`

---

## 1. Objetivo del microservicio

Gestionar el ciclo comercial completo de EcoMarket SPA: carrito de compras, cupones, pedidos online, ventas presenciales, facturación electrónica académica, pagos, devoluciones y reclamaciones.

Este documento centraliza la evidencia técnica del flujo comercial y reemplaza la documentación interna previa del microservicio.

---

## 2. Flujo de carrito

1. Cliente crea un carrito: `POST /api/pedidos/carritos`.
2. Agrega ítems: `POST /api/pedidos/carritos/{id}/items`.
3. El sistema valida stock disponible.
4. Se generan registros `ItemCarrito`.
5. Se recalcula subtotal y total automáticamente.

---

## 3. Flujo de cupones

1. Administrador crea cupón: `POST /api/pedidos/cupones`.
2. Cliente aplica cupón al carrito: `POST /api/pedidos/carritos/{id}/cupon`.
3. El sistema valida vigencia, monto mínimo y tipo de descuento.
4. Si el cupón es válido, el descuento se refleja en el total del carrito.
5. Si el cupón está vencido o no existe, la operación se rechaza.

---

## 4. Flujo de pedido online

1. Cliente convierte carrito en pedido: `POST /api/pedidos/desde-carrito/{idCarrito}`.
2. El carrito pasa a estado `CONVERTIDO`.
3. Se crea un `Pedido`.
4. Se registran los detalles del pedido.
5. El pedido inicia en estado `PENDIENTE`.
6. Se registra historial automáticamente.
7. Cliente consulta estado: `GET /api/pedidos/{id}/estado`.
8. Cliente puede cancelar si el pedido está `PENDIENTE`: `PATCH /api/pedidos/{id}/cancelar`.
9. Se registra historial de cancelación.

---

## 5. Flujo de venta presencial

1. Vendedor registra venta: `POST /api/ventas/presencial`.
2. El body incluye lista de ítems con cantidad y precio unitario.
3. Se crea una `Venta`.
4. Se asocia el pago correspondiente.
5. El sistema calcula subtotal, descuento y total automáticamente.
6. Responde `201 Created` con la venta creada y enlaces HATEOAS.

---

## 6. Flujo de pago

- La entidad `Pago` registra método de pago: `TARJETA`, `TRANSFERENCIA` o `EFECTIVO`.
- El pago puede asociarse a `idPedido` o `idVenta`.
- El estado inicial queda registrado como `APROBADO`.
- Las consultas se realizan mediante `PagoRepository.findByIdPedido` y `PagoRepository.findByIdVenta`.

---

## 7. Flujo de facturación electrónica académica

1. Vendedor genera factura: `POST /api/ventas/{idVenta}/factura`.
2. El sistema valida que la venta no tenga factura previa.
3. Si ya existe factura, responde `409 Conflict`.
4. Si no existe, crea factura con folio autoincremental.
5. Consulta de factura: `GET /api/ventas/facturas/{idFactura}`.

La entidad `Factura` representa el concepto de facturación dentro del sistema EcoMarket SPA. En el contexto académico del proyecto, no existe integración real con SII, generación de PDF oficial ni envío automático por correo.

---

## 8. Flujo de devolución

1. Cliente solicita devolución por venta: `POST /api/ventas/{idVenta}/devoluciones`.
2. También puede utilizarse la ruta general: `POST /api/ventas/devoluciones`.
3. La solicitud requiere `idCliente` y motivo.
4. El sistema registra estado y seguimiento.
5. Responde `201 Created` con enlaces HATEOAS.

---

## 9. Flujo de reclamación

1. Cliente crea reclamación por pedido: `POST /api/pedidos/{idPedido}/reclamaciones`.
2. También puede utilizarse la ruta general: `POST /api/ventas/reclamaciones`.
3. La reclamación se asocia a cliente y opcionalmente a pedido o venta.
4. Consulta de reclamaciones del pedido: `GET /api/pedidos/{idPedido}/reclamaciones`.

---

## 10. Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/pedidos/carritos` | Crear carrito |
| POST | `/api/pedidos/carritos/{id}/items` | Agregar ítem |
| POST | `/api/pedidos/cupones` | Crear cupón |
| POST | `/api/pedidos/carritos/{id}/cupon` | Aplicar cupón |
| POST | `/api/pedidos/desde-carrito/{id}` | Crear pedido desde carrito |
| GET | `/api/pedidos` | Listar pedidos |
| GET | `/api/pedidos/{id}` | Obtener pedido |
| PUT | `/api/pedidos/{id}` | Actualizar pedido |
| DELETE | `/api/pedidos/{id}` | Eliminar pedido |
| GET | `/api/pedidos/{id}/estado` | Consultar estado |
| PATCH | `/api/pedidos/{id}/cancelar` | Cancelar pedido |
| GET | `/api/pedidos/{id}/historial` | Historial de cambios |
| GET | `/api/pedidos/clientes/{id}/historial` | Historial del cliente |
| POST | `/api/pedidos/{id}/reclamaciones` | Crear reclamación |
| GET | `/api/pedidos/{id}/reclamaciones` | Listar reclamaciones |
| POST | `/api/ventas/presencial` | Registrar venta presencial |
| GET | `/api/ventas` | Listar ventas |
| GET | `/api/ventas/{id}` | Obtener venta |
| PUT | `/api/ventas/{id}` | Actualizar venta |
| DELETE | `/api/ventas/{id}` | Eliminar venta |
| POST | `/api/ventas/{id}/factura` | Generar factura |
| GET | `/api/ventas/facturas/{id}` | Obtener factura |
| POST | `/api/ventas/{id}/devoluciones` | Crear devolución |

---

## 11. Códigos HTTP usados

| Código | Significado |
|---|---|
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 404 | Not Found |
| 409 | Conflict |
| 500 | Internal Server Error |

---

## 12. Validaciones Postman

Flujo validado en Postman:

1. `POST /api/pedidos/carritos` → `201`
2. `POST /api/pedidos/carritos/{id}/items` → `200`
3. `POST /api/pedidos/cupones` → `201`
4. `POST /api/pedidos/carritos/{id}/cupon` → `200`
5. `POST /api/pedidos/desde-carrito/{id}` → `201`
6. `GET /api/pedidos` → `200`
7. `GET /api/pedidos/{id}` → `200`
8. `GET /api/pedidos/{id}/estado` → `200`
9. `PATCH /api/pedidos/{id}/cancelar` → `200`
10. `GET /api/pedidos/clientes/{id}/historial` → `200`
11. `POST /api/ventas/presencial` → `201`
12. `GET /api/ventas` → `200`
13. `GET /api/ventas/{id}` → `200`
14. `POST /api/ventas/{id}/factura` → `201`
15. `POST /api/ventas/{id}/factura` duplicado → `409`
16. `POST /api/ventas/{id}/devoluciones` → `201`
17. `POST /api/pedidos/{id}/reclamaciones` → `201`
18. `GET /api/pedidos/{id}/reclamaciones` → `200`
19. Respuestas principales con enlaces `_links` cuando corresponde.

---

## 13. Evidencia JUnit

Tests registrados en `MsPedidosVentasApplicationTests.java`:

- `contextLoads`: contexto Spring carga correctamente.
- `crearPedidoDesdeCarrito`: pedido creado con estado `PENDIENTE`.
- `consultarEstadoPedido`: retorna `PENDIENTE`.
- `cancelarPedidoPendiente`: retorna `CANCELADO`.
- `registrarVentaPresencial`: total calculado desde ítems.
- `generarFactura`: factura creada correctamente.
- `evitarFacturaDuplicada`: lanza excepción para factura duplicada.
- `crearDevolucion`: devolución creada correctamente.
- `crearReclamacion`: reclamación creada y listada.

Resultado documentado: `BUILD SUCCESS`.

---

## 14. Relación con HU

- HU-7 — Añadir productos al carrito.
- HU-8 — Aplicación de cupones de descuento.
- HU-10 — Revisión de historial de compras.
- HU-11 — Realización de pedido online.
- HU-12 — Consulta de estado del pedido.
- HU-13 — Cancelación de pedido pendiente.
- HU-14 — Registro de venta presencial.
- HU-15 — Atención de devoluciones y reclamaciones.
- HU-36 — Generación de factura electrónica.
- HU-45 — Implementar CRUD del MS Pedidos y Ventas.
- HU-46 — Validar flujo de compra/venta en Postman.
- HU-57 — Documentar facturación electrónica y flujo comercial.

---

## 15. Alcance académico

Este documento es de carácter académico y consolida evidencia técnica del flujo comercial del MS Pedidos y Ventas. La facturación electrónica se representa como entidad interna del sistema EcoMarket SPA y no integra servicios externos tributarios.
