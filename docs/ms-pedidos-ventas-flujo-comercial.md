# Flujo Comercial - MS Pedidos y Ventas
## EcoMarket SPA - Sprint 3

> Microservicio: ms-pedidos-ventas
> Puerto: 8084
> Base de datos: MySQL/XAMPP - bd_ventas
> Rama: feature/sprint3-pedidos-ventas-benjamin

---

## 1. Objetivo del Microservicio

Gestionar el ciclo comercial completo de EcoMarket SPA:
carrito de compras, cupones, pedidos online, ventas presenciales,
facturacion electronica, pagos, devoluciones y reclamaciones.

---

## 2. Flujo de Carrito

1. Cliente crea un carrito: POST /api/pedidos/carritos
2. Agrega items: POST /api/pedidos/carritos/{id}/items
3. El sistema valida stock disponible (400 si insuficiente).
4. Se recalcula subtotal y total automaticamente.

---

## 3. Flujo de Cupones

1. Administrador crea cupon: POST /api/pedidos/cupones
2. Cliente aplica cupon al carrito: POST /api/pedidos/carritos/{id}/cupon
3. El sistema valida vigencia, monto minimo y tipo (PORCENTAJE o MONTO_FIJO).
4. El descuento se refleja en el total del carrito.

---

## 4. Flujo de Pedido Online

1. Cliente convierte carrito en pedido: POST /api/pedidos/desde-carrito/{idCarrito}
2. El carrito pasa a estado CONVERTIDO.
3. El pedido inicia en estado PENDIENTE.
4. Se registra historial automaticamente.
5. Cliente consulta estado: GET /api/pedidos/{id}/estado
6. Cliente puede cancelar si esta PENDIENTE: PATCH /api/pedidos/{id}/cancelar
7. Se registra historial de cancelacion.

---

## 5. Flujo de Venta Presencial

1. Vendedor registra venta: POST /api/ventas/presencial
2. El body incluye lista de items con cantidad y precioUnitario.
3. El sistema calcula subtotal, descuento y total automaticamente.
4. Responde 201 con la venta creada y _links.

---

## 6. Flujo de Pago

- Entidad Pago registra metodo (TARJETA, TRANSFERENCIA, EFECTIVO).
- Asociado a idPedido o idVenta.
- Estado por defecto: APROBADO.
- Consulta: PagoRepository.findByIdPedido / findByIdVenta.

---

## 7. Flujo de Factura Electronica

1. Vendedor genera factura: POST /api/ventas/{idVenta}/factura
2. El sistema valida que la venta no tenga factura previa.
3. Si ya existe: responde 409 Conflict.
4. Si no existe: crea factura con folio autoincremental.
5. Consulta: GET /api/ventas/facturas/{idFactura}

---

## 8. Flujo de Devolucion

1. Cliente solicita devolucion por venta: POST /api/ventas/{idVenta}/devoluciones
2. O por ruta general: POST /api/ventas/devoluciones
3. Requiere idCliente y motivo.
4. Responde 201 con _links.

---

## 9. Flujo de Reclamacion

1. Cliente crea reclamacion por pedido: POST /api/pedidos/{idPedido}/reclamaciones
2. O por ruta general: POST /api/ventas/reclamaciones
3. Consulta reclamaciones del pedido: GET /api/pedidos/{idPedido}/reclamaciones

---

## 10. Endpoints Principales

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| POST | /api/pedidos/carritos | Crear carrito |
| POST | /api/pedidos/carritos/{id}/items | Agregar item |
| POST | /api/pedidos/cupones | Crear cupon |
| POST | /api/pedidos/carritos/{id}/cupon | Aplicar cupon |
| POST | /api/pedidos/desde-carrito/{id} | Crear pedido |
| GET | /api/pedidos | Listar pedidos |
| GET | /api/pedidos/{id} | Obtener pedido |
| PUT | /api/pedidos/{id} | Actualizar pedido |
| DELETE | /api/pedidos/{id} | Eliminar pedido |
| GET | /api/pedidos/{id}/estado | Consultar estado |
| PATCH | /api/pedidos/{id}/cancelar | Cancelar pedido |
| GET | /api/pedidos/{id}/historial | Historial de cambios |
| GET | /api/pedidos/clientes/{id}/historial | Historial del cliente |
| POST | /api/pedidos/{id}/reclamaciones | Crear reclamacion |
| GET | /api/pedidos/{id}/reclamaciones | Listar reclamaciones |
| POST | /api/ventas/presencial | Registrar venta |
| GET | /api/ventas | Listar ventas |
| GET | /api/ventas/{id} | Obtener venta |
| PUT | /api/ventas/{id} | Actualizar venta |
| DELETE | /api/ventas/{id} | Eliminar venta |
| POST | /api/ventas/{id}/factura | Generar factura |
| GET | /api/ventas/facturas/{id} | Obtener factura |
| POST | /api/ventas/{id}/devoluciones | Crear devolucion |

---

## 11. Codigos HTTP Usados

| Codigo | Significado |
|--------|-------------|
| 200 | OK |
| 201 | Created |
| 204 | No Content (DELETE) |
| 400 | Bad Request (validacion) |
| 404 | Not Found |
| 409 | Conflict (factura duplicada) |
| 500 | Internal Server Error |

---

## 12. Validaciones Postman

Flujo validado en Postman:

1. POST /api/pedidos/carritos -> 201
2. POST /api/pedidos/carritos/{id}/items -> 200
3. POST /api/pedidos/cupones -> 201
4. POST /api/pedidos/carritos/{id}/cupon -> 200
5. POST /api/pedidos/desde-carrito/{id} -> 201
6. GET /api/pedidos -> 200
7. GET /api/pedidos/{id} -> 200
8. GET /api/pedidos/{id}/estado -> 200
9. PATCH /api/pedidos/{id}/cancelar -> 200
10. GET /api/pedidos/clientes/{id}/historial -> 200
11. POST /api/ventas/presencial (con items) -> 201
12. GET /api/ventas -> 200
13. GET /api/ventas/{id} -> 200
14. POST /api/ventas/{id}/factura -> 201
15. POST /api/ventas/{id}/factura (duplicado) -> 409
16. POST /api/ventas/{id}/devoluciones -> 201
17. POST /api/pedidos/{id}/reclamaciones -> 201
18. GET /api/pedidos/{id}/reclamaciones -> 200
19. Todas las respuestas incluyen _links (HATEOAS)

---

## 13. Evidencia JUnit

Tests en MsPedidosVentasApplicationTests.java:

- contextLoads: contexto Spring carga correctamente
- crearPedidoDesdeCarrito: pedido creado con estado PENDIENTE
- consultarEstadoPedido: retorna PENDIENTE
- cancelarPedidoPendiente: retorna CANCELADO
- registrarVentaPresencial: total calculado desde items
- generarFactura: factura creada correctamente
- evitarFacturaDuplicada: lanza IllegalStateException (409)
- crearDevolucion: devolucion creada correctamente
- crearReclamacion: reclamacion creada y listada

Resultado: Tests run: 9, Failures: 0, Errors: 0 - BUILD SUCCESS
