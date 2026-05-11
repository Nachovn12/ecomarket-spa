# EcoMarket SPA - Validación Postman

## Objetivo

Validar los endpoints REST principales de EcoMarket SPA mediante una colección general de Postman organizada por microservicio.

La colección tiene como propósito apoyar la validación técnica del proyecto, facilitar pruebas durante el desarrollo y servir como evidencia para la defensa académica del Sprint 2.

---

## Archivos generados

| Archivo                                                | Uso                                         |
| ------------------------------------------------------ | ------------------------------------------- |
| `postman/EcoMarket-SPA.postman_collection.json`        | Colección principal del proyecto            |
| `postman/EcoMarket-SPA-local.postman_environment.json` | Ambiente local con variables de ejecución   |
| `docs/postman-validacion.md`                           | Documento técnico de validación con Postman |

---

## Variables configuradas

| Variable             | Valor base              | Descripción                                           |
| -------------------- | ----------------------- | ----------------------------------------------------- |
| `base_url`           | `http://localhost:8080` | URL principal del API Gateway                         |
| `token`              | Vacío inicialmente      | Token JWT o token de sesión para endpoints protegidos |
| `api_gateway_port`   | `8080`                  | Puerto del API Gateway                                |
| `ms_usuarios_port`   | `8081`                  | Puerto propuesto para MS Usuarios e Identidad         |
| `ms_catalogo_port`   | `8082`                  | Puerto propuesto para MS Catálogo                     |
| `ms_inventario_port` | `8083`                  | Puerto propuesto para MS Inventario y Abastecimiento  |
| `ms_pedidos_port`    | `8084`                  | Puerto propuesto para MS Pedidos y Ventas             |
| `ms_logistica_port`  | `8085`                  | Puerto propuesto para MS Logística de Envíos          |
| `ms_admin_port`      | `8086`                  | Puerto propuesto para MS Administración y Soporte     |
| `ms_reportes_port`   | `8087`                  | Puerto propuesto para MS Reportes                     |

---

## Organización de la colección

La colección principal de Postman se organiza en carpetas por componente y microservicio:

1. API Gateway
2. MS Usuarios e Identidad
3. MS Catálogo
4. MS Inventario y Abastecimiento
5. MS Pedidos y Ventas
6. MS Logística de Envíos
7. MS Administración y Soporte
8. MS Reportes

Esta organización permite validar cada dominio funcional de EcoMarket SPA de forma ordenada, manteniendo una estructura clara para la presentación y para el trabajo colaborativo del equipo.

---

## Endpoints principales incluidos

| Microservicio                  | Endpoints principales                                                              |
| ------------------------------ | ---------------------------------------------------------------------------------- |
| API Gateway                    | `/actuator/health`                                                                 |
| MS Usuarios e Identidad        | `/auth/login`, `/usuarios`                                                         |
| MS Catálogo                    | `/productos`, `/categorias`, `/productos/{id}/resenas`                             |
| MS Inventario y Abastecimiento | `/inventario`, `/stock`, `/stock/reservas`                                         |
| MS Pedidos y Ventas            | `/pedidos`, `/pedidos/carritos`, `/ventas/pagos`, `/ventas/facturas`               |
| MS Logística de Envíos         | `/envios`, `/envios/{id}/seguimiento`, `/rutas`                                    |
| MS Administración y Soporte    | `/admin/tiendas`, `/soporte/tickets`                                               |
| MS Reportes                    | `/reportes/ventas`, `/reportes/inventario`, `/kpi`, `/reportes/{id}/exportaciones` |

---

## Flujo general de validación

Para validar correctamente los endpoints desde Postman, se recomienda seguir este flujo:

1. Levantar el componente `api-gateway` en el puerto `8080`.
2. Levantar el microservicio que se desea probar.
3. Importar el ambiente `EcoMarket-SPA-local.postman_environment.json`.
4. Importar la colección `EcoMarket-SPA.postman_collection.json`.
5. Seleccionar el ambiente local en Postman.
6. Ejecutar `GET {{base_url}}/actuator/health`.
7. Validar que el API Gateway responda correctamente.
8. Ejecutar los endpoints correspondientes al microservicio que se está probando.
9. Revisar request, response y códigos HTTP.
10. Registrar evidencia para la defensa.

---

## Ejemplo request: crear producto

```http
POST {{base_url}}/productos
Content-Type: application/json
Authorization: Bearer {{token}}
```

### Body

```json
{
  "sku": "ECO-SHAMPOO-001",
  "nombre": "Shampoo sólido ecológico",
  "precio": 4990,
  "descripcion": "Producto ecológico sin envase plástico",
  "descripcionEcologica": "Libre de plástico y biodegradable",
  "idCategoria": 1
}
```

---

## Ejemplo response esperado

```json
{
  "idProducto": 1,
  "sku": "ECO-SHAMPOO-001",
  "nombre": "Shampoo sólido ecológico",
  "precio": 4990,
  "estado": "PUBLICADO"
}
```

---

## Ejemplo request: consultar inventario

```http
GET {{base_url}}/inventario?idProducto=1&idTienda=1
Authorization: Bearer {{token}}
```

### Response esperado

```json
{
  "idInventario": 1,
  "idProducto": 1,
  "idTienda": 1,
  "stockDisponible": 50,
  "stockReservado": 0,
  "stockMinimo": 10,
  "estado": "ACTIVO"
}
```

---

## Ejemplo request: realizar pedido online

```http
POST {{base_url}}/pedidos
Content-Type: application/json
Authorization: Bearer {{token}}
```

### Body

```json
{
  "idCliente": 1,
  "direccionEntrega": "Barrio Lastarria, Santiago",
  "opcionEnvio": "DESPACHO"
}
```

### Response esperado

```json
{
  "idPedido": 1,
  "idCliente": 1,
  "estado": "PENDIENTE",
  "direccionEntrega": "Barrio Lastarria, Santiago",
  "total": 9980
}
```

---

## Ejemplo request: crear envío

```http
POST {{base_url}}/envios
Content-Type: application/json
Authorization: Bearer {{token}}
```

### Body

```json
{
  "idPedido": 1,
  "direccionDestino": "Barrio Lastarria, Santiago"
}
```

### Response esperado

```json
{
  "idEnvio": 1,
  "idPedido": 1,
  "estado": "PREPARADO",
  "direccionDestino": "Barrio Lastarria, Santiago"
}
```

---

## Ejemplo request: generar reporte de ventas

```http
GET {{base_url}}/reportes/ventas?idTienda=1&fechaInicio=2026-05-01&fechaFin=2026-05-31
Authorization: Bearer {{token}}
```

### Response esperado

```json
{
  "idReporte": 1,
  "tipoReporte": "VENTAS",
  "idTienda": 1,
  "fechaInicio": "2026-05-01",
  "fechaFin": "2026-05-31",
  "estado": "GENERADO"
}
```

---

## Validación mediante API Gateway

La colección utiliza como URL principal:

```txt
{{base_url}}
```

Por defecto:

```txt
http://localhost:8080
```

Esto significa que las solicitudes se realizan a través del API Gateway, no directamente a cada microservicio.

Ejemplo:

```txt
http://localhost:8080/productos
```

En lugar de:

```txt
http://localhost:8082/productos
```

Esto permite validar que el Gateway funcione como punto central de entrada REST para EcoMarket SPA.

---

## Consideraciones técnicas

El API Gateway:

- Centraliza el acceso a los microservicios.
- Permite ordenar las rutas REST del sistema.
- Evita que el cliente consuma directamente cada microservicio.
- Facilita la integración con el Frontend Web.
- Mejora el bajo acoplamiento entre cliente y backend.
- Permite preparar futuras validaciones de seguridad desde una capa común.

---

## Criterios de aceptación cubiertos

| Criterio de aceptación                               | Estado   |
| ---------------------------------------------------- | -------- |
| La colección está organizada por microservicio       | Cumplido |
| Los endpoints principales tienen request de ejemplo  | Cumplido |
| Se documentan ejemplos de response                   | Cumplido |
| Se incluyen variables `base_url`, `token` y puertos  | Cumplido |
| La colección puede ser usada durante la presentación | Cumplido |
| Se validan endpoints mediante el API Gateway         | Cumplido |

---

## Evidencias recomendadas para la defensa

Para la presentación del proyecto, se recomienda guardar evidencia de:

1. Colección importada en Postman.
2. Ambiente local seleccionado.
3. Request `GET {{base_url}}/actuator/health`.
4. Respuesta con estado `UP`.
5. Carpetas organizadas por microservicio.
6. Ejemplo de request `POST {{base_url}}/productos`.
7. Ejemplo de request `GET {{base_url}}/inventario`.
8. Ejemplo de request `POST {{base_url}}/pedidos`.
9. Ejemplo de request `POST {{base_url}}/envios`.
10. Ejemplo de request `GET {{base_url}}/reportes/ventas`.

---

## Archivos asociados a HU-44

| Archivo                                                | Descripción                         |
| ------------------------------------------------------ | ----------------------------------- |
| `postman/EcoMarket-SPA.postman_collection.json`        | Colección principal de Postman      |
| `postman/EcoMarket-SPA-local.postman_environment.json` | Ambiente local de Postman           |
| `docs/postman-validacion.md`                           | Documentación técnica de validación |

---

## Rama de trabajo

```txt
feature/postman-collection
```

---

## Commit recomendado

```txt
docs(HU-44): crear coleccion postman principal
```

---

## Pull Request

La rama debe integrarse hacia:

```txt
develop
```

---

## Relación con HU-44

Esta documentación corresponde a la tarea:

```txt
HU-44 — TT-05 Crear colección Postman principal del proyecto
```

Su objetivo es dejar preparada una colección general de Postman para validar endpoints REST de EcoMarket SPA, organizada por microservicio y lista para ser utilizada durante la presentación.
