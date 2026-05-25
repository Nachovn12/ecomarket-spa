# Evidencia Postman — EP2 EcoMarket SPA

## Proyecto

**EcoMarket SPA**
**Asignatura:** Desarrollo Full Stack I — DSY1103
**Sección:** 003D
**Entrega:** EP2 | Entrega de Encargo grupal Parte 1
**Arquitectura:** Microservicios independientes con API Gateway
**Tipo de evidencia:** Pruebas de endpoints REST mediante Postman

---

## 1. Objetivo

Este documento consolida la evidencia de pruebas REST realizadas sobre los microservicios del backend de **EcoMarket SPA**.

Las pruebas documentadas permiten validar que los endpoints principales responden correctamente, utilizan métodos HTTP adecuados, devuelven estructuras JSON coherentes y reflejan las reglas de negocio implementadas en cada microservicio.

---

## 2. Alcance de la evidencia

La evidencia considera los principales endpoints de los siguientes componentes:

- MS Usuarios e Identidad.
- MS Catálogo.
- MS Inventario y Abastecimiento.
- MS Pedidos y Ventas.
- MS Logística de Envíos.
- MS Administración y Soporte.
- MS Reportes.
- API Gateway.

Las pruebas fueron diseñadas para demostrar:

- Creación de recursos.
- Consulta de recursos.
- Actualización de estados.
- Validaciones de negocio.
- Manejo de errores.
- Respuestas REST con códigos HTTP adecuados.
- Uso de HATEOAS en endpoints principales cuando corresponde.

---

## 3. Herramienta utilizada

La herramienta utilizada para la validación manual de endpoints fue **Postman**.

Elementos esperados dentro del repositorio:

```text
docs/postman/
```

Archivos sugeridos:

```text
EcoMarket-SPA.postman_collection.json
EcoMarket-SPA-local.postman_environment.json
evidencia-postman.md
```

En caso de adjuntar capturas, se recomienda almacenarlas en:

```text
docs/evidencias/
```

o en una subcarpeta específica:

```text
docs/evidencias/postman/
```

---

## 4. Variables de entorno sugeridas en Postman

Para facilitar las pruebas, se recomienda utilizar un environment local con las siguientes variables:

| Variable | Valor sugerido |
|---|---|
| `gateway_url` | `http://localhost:8081` |
| `usuarios_url` | `http://localhost:8083` |
| `catalogo_url` | `http://localhost:8084` |
| `inventario_url` | `http://localhost:8085` |
| `pedidos_url` | `http://localhost:8086` |
| `logistica_url` | `http://localhost:8087` |
| `admin_url` | `http://localhost:8088` |
| `reportes_url` | `http://localhost:8089` |

---

## 5. Códigos HTTP esperados

| Código | Significado           | Uso esperado                              |
| -----: | --------------------- | ----------------------------------------- |
|    200 | OK                    | Consulta o actualización exitosa          |
|    201 | Created               | Recurso creado correctamente              |
|    204 | No Content            | Operación exitosa sin cuerpo de respuesta |
|    400 | Bad Request           | Datos inválidos o solicitud incorrecta    |
|    404 | Not Found             | Recurso no encontrado                     |
|    409 | Conflict              | Regla de negocio incumplida               |
|    500 | Internal Server Error | Error interno no controlado               |

---

## 6. MS Usuarios e Identidad

### Endpoint 1: Registrar usuario

**Método:** `POST`
**URL directa:** `{{usuarios_url}}/api/usuarios`
**URL Gateway sugerida:** `{{gateway_url}}/usuarios`
**Descripción:** Registra un nuevo usuario en el sistema.

Body de ejemplo:

```json
{
  "nombre": "Cliente Test",
  "email": "cliente.test@ecomarket.cl",
  "password": "Password123",
  "rol": "CLIENTE"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Usuario registrado correctamente.
```

---

### Endpoint 2: Consultar usuario por ID

**Método:** `GET`
**URL directa:** `{{usuarios_url}}/api/usuarios/{id}`
**URL Gateway sugerida:** `{{gateway_url}}/usuarios/{id}`
**Descripción:** Obtiene los datos de un usuario registrado.

Resultado esperado:

```text
Código esperado: 200
Resultado: Usuario encontrado correctamente.
```

---

### Endpoint 3: Error por usuario inexistente

**Método:** `GET`
**URL directa:** `{{usuarios_url}}/api/usuarios/999999`
**URL Gateway sugerida:** `{{gateway_url}}/usuarios/999999`
**Descripción:** Valida el manejo de error cuando el usuario no existe.

Resultado esperado:

```text
Código esperado: 404
Resultado: Usuario no encontrado.
```

---

## 7. MS Catálogo

### Endpoint 1: Crear producto

**Método:** `POST`
**URL directa:** `{{catalogo_url}}/api/productos`
**URL Gateway sugerida:** `{{gateway_url}}/productos`
**Descripción:** Registra un producto ecológico en el catálogo.

Body de ejemplo:

```json
{
  "sku": "ECO-001",
  "nombre": "Detergente ecológico",
  "precio": 4990,
  "descripcion": "Detergente biodegradable para uso doméstico",
  "descripcionEcologica": "Producto elaborado con componentes biodegradables",
  "idCategoria": 1
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Producto creado correctamente.
```

---

### Endpoint 2: Listar productos

**Método:** `GET`
**URL directa:** `{{catalogo_url}}/api/productos`
**URL Gateway sugerida:** `{{gateway_url}}/productos`
**Descripción:** Lista productos disponibles en el catálogo.

Resultado esperado:

```text
Código esperado: 200
Resultado: Lista de productos obtenida correctamente.
```

---

### Endpoint 3: Crear categoría

**Método:** `POST`
**URL directa:** `{{catalogo_url}}/api/categorias`
**URL Gateway sugerida:** `{{gateway_url}}/categorias`
**Descripción:** Registra una categoría de productos.

Body de ejemplo:

```json
{
  "nombre": "Limpieza ecológica",
  "descripcion": "Productos de limpieza sustentables"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Categoría creada correctamente.
```

---

## 8. MS Inventario y Abastecimiento

### Endpoint 1: Crear producto de inventario

**Método:** `POST`
**URL directa:** `{{inventario_url}}/api/productos`
**URL Gateway sugerida:** `{{gateway_url}}/inventario/productos`
**Descripción:** Registra un producto dentro del inventario.

Body de ejemplo:

```json
{
  "sku": "ECO-INV-001",
  "nombre": "Detergente ecológico",
  "categoria": "Limpieza",
  "precio": 4990,
  "stock": 30,
  "sucursal": "Lastarria"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Producto de inventario creado correctamente.
```

---

### Endpoint 2: Consultar stock por SKU

**Método:** `GET`
**URL directa:** `{{inventario_url}}/api/productos/sku/ECO-INV-001`
**URL Gateway sugerida:** `{{gateway_url}}/stock/ECO-INV-001`
**Descripción:** Consulta disponibilidad de stock por SKU.

Resultado esperado:

```text
Código esperado: 200
Resultado: Stock consultado correctamente.
```

---

### Endpoint 3: Ajustar stock

**Método:** `PATCH`
**URL directa:** `{{inventario_url}}/api/productos/{id}/stock`
**URL Gateway sugerida:** `{{gateway_url}}/inventario/productos/{id}/stock`
**Descripción:** Ajusta stock de un producto, registrando el movimiento correspondiente.

Body de ejemplo:

```json
{
  "cantidadNueva": 40,
  "motivo": "Ajuste por recepción de mercadería",
  "usuarioResponsable": "gerente.tienda"
}
```

Resultado esperado:

```text
Código esperado: 200
Resultado: Stock actualizado correctamente.
```

---

## 9. MS Pedidos y Ventas

### Endpoint 1: Crear pedido desde carrito

**Método:** `POST`
**URL directa:** `{{pedidos_url}}/api/pedidos`
**URL Gateway sugerida:** `{{gateway_url}}/pedidos`
**Descripción:** Crea un pedido desde un carrito de compra.

Body de ejemplo:

```json
{
  "metodoPago": "EFECTIVO",
  "direccionEntrega": "Calle Test 123, Santiago"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Pedido creado correctamente.
```

---

### Endpoint 2: Consultar estado de pedido

**Método:** `GET`
**URL directa:** `{{pedidos_url}}/api/pedidos/{idPedido}/estado`
**URL Gateway sugerida:** `{{gateway_url}}/pedidos/{idPedido}/estado`
**Descripción:** Consulta el estado actual de un pedido.

Resultado esperado:

```text
Código esperado: 200
Resultado: Estado del pedido obtenido correctamente.
```

---

### Endpoint 3: Consultar historial de cliente

**Método:** `GET`
**URL directa:** `{{pedidos_url}}/api/pedidos/clientes/{idCliente}/historial`
**URL Gateway sugerida:** `{{gateway_url}}/pedidos/clientes/{idCliente}/historial`
**Descripción:** Obtiene el historial de pedidos de un cliente.

Resultado esperado:

```text
Código esperado: 200
Resultado: Historial de pedidos obtenido correctamente.
```

---

### Endpoint 4: Registrar venta presencial

**Método:** `POST`
**URL directa:** `{{pedidos_url}}/api/ventas/presencial`
**URL Gateway sugerida:** `{{gateway_url}}/ventas/presencial`
**Descripción:** Registra una venta presencial realizada por un empleado de ventas.

Body de ejemplo:

```json
{
  "idCliente": 2,
  "metodoPago": "EFECTIVO",
  "items": [
    {
      "idProducto": 10,
      "nombreProducto": "Producto Venta",
      "cantidad": 3,
      "precioUnitario": 500
    }
  ]
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Venta registrada correctamente.
```

---

### Endpoint 5: Generar factura

**Método:** `POST`
**URL directa:** `{{pedidos_url}}/api/ventas/{idVenta}/facturas`
**URL Gateway sugerida:** `{{gateway_url}}/ventas/{idVenta}/facturas`
**Descripción:** Genera una factura asociada a una venta.

Body de ejemplo:

```json
{
  "rutCliente": "12345678-9",
  "razonSocial": "Empresa Test SpA"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Factura generada correctamente.
```

---

### Endpoint 6: Registrar devolución por venta

**Método:** `POST`
**URL directa:** `{{pedidos_url}}/api/ventas/{idVenta}/devoluciones`
**URL Gateway sugerida:** `{{gateway_url}}/ventas/{idVenta}/devoluciones`
**Descripción:** Registra una devolución asociada a una venta.

Body de ejemplo:

```json
{
  "idCliente": 1,
  "motivo": "Producto defectuoso"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Devolución registrada correctamente.
```

---

### Endpoint 7: Actualizar estado de devolución

**Método:** `PATCH`
**URL directa:** `{{pedidos_url}}/api/devoluciones/{id}/estado`
**URL Gateway sugerida:** `{{gateway_url}}/devoluciones/{id}/estado`
**Descripción:** Actualiza el estado de una devolución.

Body de ejemplo:

```json
{
  "estado": "APROBADA"
}
```

Resultado esperado:

```text
Código esperado: 200
Resultado: Estado de devolución actualizado correctamente.
```

---

### Endpoint 8: Registrar reclamación

**Método:** `POST`
**URL directa:** `{{pedidos_url}}/api/reclamaciones`
**URL Gateway sugerida:** `{{gateway_url}}/reclamaciones`
**Descripción:** Registra una reclamación asociada a un cliente y eventualmente a un pedido.

Body de ejemplo:

```json
{
  "idCliente": 1,
  "idPedido": 1,
  "motivo": "Pedido incompleto",
  "descripcion": "Faltaron 2 productos"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Reclamación registrada correctamente.
```

---

### Endpoint 9: Actualizar estado de reclamación

**Método:** `PATCH`
**URL directa:** `{{pedidos_url}}/api/reclamaciones/{id}/estado`
**URL Gateway sugerida:** `{{gateway_url}}/reclamaciones/{id}/estado`
**Descripción:** Actualiza el estado de una reclamación.

Body de ejemplo:

```json
{
  "estado": "CERRADA"
}
```

Resultado esperado:

```text
Código esperado: 200
Resultado: Estado de reclamación actualizado correctamente.
```

---

### Endpoint 10: Error por estado de devolución inválido

**Método:** `PATCH`
**URL directa:** `{{pedidos_url}}/api/devoluciones/{id}/estado`
**URL Gateway sugerida:** `{{gateway_url}}/devoluciones/{id}/estado`
**Descripción:** Valida el manejo de error ante un estado de devolución no permitido.

Body de ejemplo:

```json
{
  "estado": "INVALIDO"
}
```

Resultado esperado:

```text
Código esperado: 400
Resultado: Estado de devolución inválido.
```

---

## 10. MS Logística de Envíos

### Endpoint 1: Crear proveedor

**Método:** `POST`
**URL directa:** `{{logistica_url}}/api/proveedores`
**URL Gateway sugerida:** `{{gateway_url}}/envios/proveedores`
**Descripción:** Registra un proveedor logístico.

Body de ejemplo:

```json
{
  "razonSocial": "Proveedor Eco Test",
  "rut": "11111111-1",
  "contacto": "Contacto Test",
  "email": "proveedor@test.cl",
  "telefono": "+56912345678",
  "tipoProveedor": "TRANSPORTE",
  "cobertura": "SANTIAGO"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Proveedor creado correctamente.
```

---

### Endpoint 2: Crear envío

**Método:** `POST`
**URL directa:** `{{logistica_url}}/api/envios`
**URL Gateway sugerida:** `{{gateway_url}}/envios`
**Descripción:** Crea un envío asociado a un pedido.

Body de ejemplo:

```json
{
  "idPedido": 1001,
  "origen": "Tienda Lastarria",
  "destino": "Cliente Web - Santiago",
  "fechaEstimadaEntrega": "2026-05-26T18:00:00"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Envío creado correctamente en estado PREPARADO.
```

---

### Endpoint 3: Consultar envío por ID

**Método:** `GET`
**URL directa:** `{{logistica_url}}/api/envios/{id}`
**URL Gateway sugerida:** `{{gateway_url}}/envios/{id}`
**Descripción:** Consulta los datos de un envío.

Resultado esperado:

```text
Código esperado: 200
Resultado: Envío obtenido correctamente.
```

---

### Endpoint 4: Cambiar estado de envío

**Método:** `PATCH`
**URL directa:** `{{logistica_url}}/api/envios/{id}/estado`
**URL Gateway sugerida:** `{{gateway_url}}/envios/{id}/estado`
**Descripción:** Actualiza el estado operacional de un envío.

Body de ejemplo:

```json
{
  "estado": "EN_CAMINO",
  "actualizadoPor": "personal.logistica",
  "observacion": "Envío retirado desde tienda"
}
```

Resultado esperado:

```text
Código esperado: 200
Resultado: Estado de envío actualizado correctamente.
```

---

### Endpoint 5: Obtener seguimiento de envío

**Método:** `GET`
**URL directa:** `{{logistica_url}}/api/envios/{id}/seguimiento`
**URL Gateway sugerida:** `{{gateway_url}}/envios/{id}/seguimiento`
**Descripción:** Obtiene el seguimiento del envío con respuesta HATEOAS.

Resultado esperado:

```text
Código esperado: 200
Resultado: Seguimiento obtenido correctamente con enlaces HATEOAS.
```

---

### Endpoint 6: Registrar incidencia

**Método:** `PATCH`
**URL directa:** `{{logistica_url}}/api/envios/{id}/incidencia`
**URL Gateway sugerida:** `{{gateway_url}}/envios/{id}/incidencia`
**Descripción:** Registra una incidencia sobre un envío.

Body de ejemplo:

```json
{
  "motivoIncidencia": "Cliente no encontrado",
  "actualizadoPor": "personal.logistica",
  "observacion": "Se intentará una nueva entrega"
}
```

Resultado esperado:

```text
Código esperado: 200
Resultado: Incidencia registrada correctamente.
```

---

### Endpoint 7: Crear ruta

**Método:** `POST`
**URL directa:** `{{logistica_url}}/api/rutas`
**URL Gateway sugerida:** `{{gateway_url}}/rutas`
**Descripción:** Crea una ruta de entrega.

Body de ejemplo:

```json
{
  "estado": "PLANIFICADA"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Ruta creada correctamente.
```

---

### Endpoint 8: Cambiar estado de ruta

**Método:** `PATCH`
**URL directa:** `{{logistica_url}}/api/rutas/{id}/estado`
**URL Gateway sugerida:** `{{gateway_url}}/rutas/{id}/estado`
**Descripción:** Cambia el estado de una ruta mediante DTO validado.

Body de ejemplo:

```json
{
  "estado": "EN_CURSO"
}
```

Resultado esperado:

```text
Código esperado: 200
Resultado: Estado de ruta actualizado correctamente.
```

---

## 11. MS Administración y Soporte

### Endpoint 1: Crear tienda

**Método:** `POST`
**URL directa:** `{{admin_url}}/api/tiendas`
**URL Gateway sugerida:** `{{gateway_url}}/admin/tiendas`
**Descripción:** Registra una tienda de EcoMarket SPA.

Body de ejemplo:

```json
{
  "nombre": "EcoMarket Lastarria",
  "ciudad": "Santiago",
  "direccion": "Barrio Lastarria",
  "horarioApertura": "09:00",
  "horarioCierre": "19:00"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Tienda creada correctamente.
```

---

### Endpoint 2: Crear ticket de soporte

**Método:** `POST`
**URL directa:** `{{admin_url}}/api/soporte/tickets`
**URL Gateway sugerida:** `{{gateway_url}}/soporte/tickets`
**Descripción:** Registra un ticket de soporte.

Body de ejemplo:

```json
{
  "idCliente": 1,
  "asunto": "Problema con pedido",
  "descripcion": "El cliente reporta retraso en la entrega",
  "prioridad": "MEDIA"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Ticket de soporte creado correctamente.
```

---

### Endpoint 3: Actualizar estado de ticket

**Método:** `PATCH`
**URL directa:** `{{admin_url}}/api/soporte/tickets/{id}/estado`
**URL Gateway sugerida:** `{{gateway_url}}/soporte/tickets/{id}/estado`
**Descripción:** Actualiza el estado de un ticket de soporte.

Body de ejemplo:

```json
{
  "estado": "EN_ATENCION"
}
```

Resultado esperado:

```text
Código esperado: 200
Resultado: Estado del ticket actualizado correctamente.
```

---

## 12. MS Reportes

### Endpoint 1: Crear reporte

**Método:** `POST`
**URL directa:** `{{reportes_url}}/api/reportes`
**URL Gateway sugerida:** `{{gateway_url}}/reportes`
**Descripción:** Genera un reporte de negocio.

Body de ejemplo:

```json
{
  "tipo": "VENTAS",
  "fechaInicio": "2026-05-01",
  "fechaFin": "2026-05-31",
  "idTienda": 1,
  "generadoPor": "gerente.tienda",
  "formatoExportacion": "PDF"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: Reporte creado correctamente.
```

---

### Endpoint 2: Obtener reporte por ID

**Método:** `GET`
**URL directa:** `{{reportes_url}}/api/reportes/{id}`
**URL Gateway sugerida:** `{{gateway_url}}/reportes/{id}`
**Descripción:** Consulta un reporte generado.

Resultado esperado:

```text
Código esperado: 200
Resultado: Reporte obtenido correctamente.
```

---

### Endpoint 3: Crear KPI

**Método:** `POST`
**URL directa:** `{{reportes_url}}/api/kpi`
**URL Gateway sugerida:** `{{gateway_url}}/kpi`
**Descripción:** Registra un indicador KPI.

Body de ejemplo:

```json
{
  "tipo": "VENTAS_TOTALES",
  "valor": 1500000,
  "descripcion": "Ventas acumuladas del mes"
}
```

Resultado esperado:

```text
Código esperado: 201
Resultado: KPI registrado correctamente.
```

---

### Endpoint 4: Consultar KPIs por tipo

**Método:** `GET`
**URL directa:** `{{reportes_url}}/api/kpi/tipo/VENTAS_TOTALES`
**URL Gateway sugerida:** `{{gateway_url}}/kpi/tipo/VENTAS_TOTALES`
**Descripción:** Consulta indicadores KPI por tipo.

Resultado esperado:

```text
Código esperado: 200
Resultado: KPIs obtenidos correctamente.
```

---

## 13. Pruebas mediante API Gateway

Cuando todos los microservicios estén levantados, las pruebas principales deben ejecutarse preferentemente mediante el API Gateway.

Ejemplos de rutas esperadas:

| Ruta Gateway                 | Microservicio                  |
| ---------------------------- | ------------------------------ |
| `{{gateway_url}}/usuarios`   | MS Usuarios e Identidad        |
| `{{gateway_url}}/productos`  | MS Catálogo                    |
| `{{gateway_url}}/inventario` | MS Inventario y Abastecimiento |
| `{{gateway_url}}/pedidos`    | MS Pedidos y Ventas            |
| `{{gateway_url}}/ventas`     | MS Pedidos y Ventas            |
| `{{gateway_url}}/envios`     | MS Logística de Envíos         |
| `{{gateway_url}}/rutas`      | MS Logística de Envíos         |
| `{{gateway_url}}/admin`      | MS Administración y Soporte    |
| `{{gateway_url}}/soporte`    | MS Administración y Soporte    |
| `{{gateway_url}}/reportes`   | MS Reportes                    |
| `{{gateway_url}}/kpi`        | MS Reportes                    |

---

## 14. Evidencia de casos correctos

Los casos correctos permiten validar que los recursos se crean, consultan y actualizan correctamente.

Casos mínimos recomendados:

| Microservicio                  | Caso correcto                                   |
| ------------------------------ | ----------------------------------------------- |
| MS Usuarios e Identidad        | Registrar y consultar usuario                   |
| MS Catálogo                    | Crear y listar producto                         |
| MS Inventario y Abastecimiento | Crear producto y consultar stock                |
| MS Pedidos y Ventas            | Crear pedido, registrar venta y generar factura |
| MS Logística de Envíos         | Crear envío y obtener seguimiento               |
| MS Administración y Soporte    | Crear tienda y ticket                           |
| MS Reportes                    | Crear reporte y consultar KPI                   |

---

## 15. Evidencia de casos de error

Los casos de error permiten validar reglas de negocio y manejo de excepciones.

Casos mínimos recomendados:

| Microservicio                  | Caso de error                                      |
| ------------------------------ | -------------------------------------------------- |
| MS Usuarios e Identidad        | Consultar usuario inexistente                      |
| MS Catálogo                    | Crear producto con SKU duplicado o datos inválidos |
| MS Inventario y Abastecimiento | Consultar producto inexistente                     |
| MS Pedidos y Ventas            | Actualizar devolución con estado inválido          |
| MS Logística de Envíos         | Cambiar estado de envío entregado                  |
| MS Administración y Soporte    | Actualizar ticket inexistente                      |
| MS Reportes                    | Consultar reporte inexistente                      |

---

## 16. Relación con la evaluación EP2

La evidencia Postman respalda los siguientes elementos solicitados en la evaluación:

- Desarrollo backend con microservicios independientes.
- Exposición de endpoints REST.
- Comunicación entre servicios mediante API REST.
- Persistencia de datos con repositorios.
- Validaciones y reglas de negocio.
- Manejo de excepciones.
- Uso de códigos HTTP adecuados.
- Evidencia técnica para la entrega.
- Documentación de endpoints.

---

## 17. Observaciones

Las rutas exactas pueden variar según la configuración final de cada controlador y del API Gateway.

En caso de diferencias menores entre rutas directas y rutas Gateway, se debe priorizar lo implementado en el código fuente y documentado en:

```text
docs/api-gateway-rutas.md
```

También se recomienda mantener actualizada la colección Postman principal del proyecto para que refleje los endpoints disponibles en la rama `develop`.

---

## 18. Conclusión

Las pruebas documentadas en Postman permiten evidenciar que el backend de EcoMarket SPA expone endpoints REST organizados por microservicio, utiliza métodos HTTP adecuados, responde con JSON, maneja errores controlados y permite validar los flujos principales del sistema.

Esta evidencia complementa la validación automatizada realizada con Maven y JUnit, aportando respaldo manual para la entrega EP2.

