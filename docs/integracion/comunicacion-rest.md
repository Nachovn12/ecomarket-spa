# EcoMarket SPA - Comunicación REST entre Microservicios

## HU-50 — TT-06 Validar comunicación REST entre microservicios

## Objetivo

Validar y documentar la comunicación REST principal entre los microservicios de EcoMarket SPA, considerando la arquitectura definida para el proyecto:

- API Gateway como punto central de entrada.
- Microservicios independientes.
- Base de datos PostgreSQL por microservicio.
- Comunicación REST entre servicios cuando el flujo de negocio lo requiere.
- Validación mediante Postman.

---

## Arquitectura general de comunicación

EcoMarket SPA utiliza una arquitectura basada en microservicios, donde cada dominio funcional se encuentra separado en un servicio independiente.

Los microservicios principales son:

| Microservicio | Responsabilidad | Base de datos |
|---|---|---|
| MS Usuarios e Identidad | Login, registro, roles y permisos | BD Usuarios |
| MS Catálogo | Productos, categorías, reseñas y búsqueda | BD Catálogo |
| MS Inventario y Abastecimiento | Stock, reservas y movimientos | BD Inventario |
| MS Pedidos y Ventas | Carrito, pedidos, ventas, pagos y facturas | BD Ventas |
| MS Logística de Envíos | Envíos, rutas, proveedores y reabastecimiento | BD Logística |
| MS Administración y Soporte | Tiendas, tickets, alertas y respaldos | BD Admin |
| MS Reportes | Estadísticas, KPIs y reportes ejecutivos | BD Reportes |

---

## Rol del API Gateway

El API Gateway actúa como punto único de entrada para los clientes REST.

Esto permite:

- Centralizar rutas.
- Evitar que el cliente consuma directamente cada microservicio.
- Mantener orden en la exposición de endpoints.
- Facilitar validación con Postman.
- Preparar futuras reglas de seguridad y autenticación.

Ejemplo de consumo mediante Gateway:

```txt
http://localhost:8080/productos
http://localhost:8080/inventario
http://localhost:8080/pedidos
http://localhost:8080/envios
http://localhost:8080/reportes
```

---

## Comunicaciones REST principales identificadas

| Origen | Destino | Propósito |
|---|---|---|
| MS Pedidos y Ventas | MS Inventario y Abastecimiento | Consultar stock y reservar productos |
| MS Pedidos y Ventas | MS Logística de Envíos | Solicitar despacho de pedidos |
| MS Reportes | MS Pedidos y Ventas | Obtener datos de ventas y pedidos |
| MS Reportes | MS Inventario y Abastecimiento | Obtener datos de stock e inventario |
| MS Reportes | MS Administración y Soporte | Obtener datos de tiendas, soporte y métricas |

---

# 1. Comunicación: Pedidos y Ventas → Inventario y Abastecimiento

## Objetivo

Cuando un cliente realiza un pedido, el MS Pedidos y Ventas debe consultar disponibilidad de stock en el MS Inventario y Abastecimiento.

## Flujo esperado

```txt
Cliente crea pedido
↓
MS Pedidos y Ventas recibe solicitud
↓
MS Pedidos y Ventas consulta stock en MS Inventario
↓
MS Inventario responde disponibilidad
↓
MS Pedidos confirma o rechaza el pedido
```

## Endpoint sugerido

```http
GET /stock?idProducto=1&idTienda=1
```

## Request mediante API Gateway

```http
GET http://localhost:8080/stock?idProducto=1&idTienda=1
```

## Response esperado

```json
{
  "idProducto": 1,
  "idTienda": 1,
  "stockDisponible": 50,
  "stockReservado": 0,
  "stockMinimo": 10
}
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Stock consultado correctamente |
| 404 Not Found | No existe inventario para producto o tienda |
| 500 Internal Server Error | Error interno del microservicio |

---

# 2. Comunicación: Pedidos y Ventas → Logística de Envíos

## Objetivo

Cuando un pedido es confirmado, el MS Pedidos y Ventas debe solicitar la creación de un envío al MS Logística de Envíos.

## Flujo esperado

```txt
Pedido confirmado
↓
MS Pedidos y Ventas solicita despacho
↓
MS Logística crea envío
↓
MS Logística retorna estado inicial del envío
```

## Endpoint sugerido

```http
POST /envios
```

## Request mediante API Gateway

```http
POST http://localhost:8080/envios
Content-Type: application/json
```

## Body ejemplo

```json
{
  "idPedido": 1,
  "direccionDestino": "Barrio Lastarria, Santiago",
  "idCliente": 1
}
```

## Response esperado

```json
{
  "idEnvio": 1,
  "idPedido": 1,
  "estado": "PREPARADO",
  "direccionDestino": "Barrio Lastarria, Santiago"
}
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 201 Created | Envío creado correctamente |
| 400 Bad Request | Datos inválidos |
| 500 Internal Server Error | Error interno del microservicio |

---

# 3. Comunicación: Reportes → Pedidos y Ventas

## Objetivo

El MS Reportes consume datos de ventas y pedidos para generar reportes ejecutivos.

## Flujo esperado

```txt
Administrador solicita reporte de ventas
↓
MS Reportes consulta datos de ventas
↓
MS Pedidos y Ventas responde información comercial
↓
MS Reportes genera reporte
```

## Endpoint sugerido

```http
GET /ventas
GET /pedidos
```

## Request mediante API Gateway

```http
GET http://localhost:8080/ventas
GET http://localhost:8080/pedidos
```

## Response esperado

```json
[
  {
    "idVenta": 1,
    "idPedido": 1,
    "total": 24990,
    "metodoPago": "TARJETA",
    "fechaVenta": "2026-05-14T09:30:00"
  }
]
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Datos obtenidos correctamente |
| 204 No Content | No existen datos para reportar |
| 500 Internal Server Error | Error interno del microservicio |

---

# 4. Comunicación: Reportes → Inventario y Abastecimiento

## Objetivo

El MS Reportes consume información de stock para generar reportes de inventario y alertas de stock bajo.

## Flujo esperado

```txt
Administrador solicita reporte de inventario
↓
MS Reportes consulta datos de stock
↓
MS Inventario responde información de productos y tiendas
↓
MS Reportes genera indicadores de inventario
```

## Endpoint sugerido

```http
GET /inventario
GET /stock
```

## Request mediante API Gateway

```http
GET http://localhost:8080/inventario
GET http://localhost:8080/stock
```

## Response esperado

```json
[
  {
    "idProducto": 1,
    "idTienda": 1,
    "stockDisponible": 8,
    "stockMinimo": 10,
    "estado": "STOCK_BAJO"
  }
]
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Datos de inventario obtenidos correctamente |
| 204 No Content | No existen registros de inventario |
| 500 Internal Server Error | Error interno del microservicio |

---

# 5. Comunicación: Reportes → Administración y Soporte

## Objetivo

El MS Reportes consume datos administrativos para generar indicadores de rendimiento de tienda, métricas de soporte y alertas del sistema.

## Flujo esperado

```txt
Administrador solicita KPI o reporte ejecutivo
↓
MS Reportes consulta tiendas, tickets o métricas
↓
MS Administración y Soporte responde datos administrativos
↓
MS Reportes genera indicadores ejecutivos
```

## Endpoint sugerido

```http
GET /admin/tiendas
GET /soporte/tickets
```

## Request mediante API Gateway

```http
GET http://localhost:8080/admin/tiendas
GET http://localhost:8080/soporte/tickets
```

## Response esperado

```json
[
  {
    "idTienda": 1,
    "nombre": "EcoMarket Lastarria",
    "ciudad": "Santiago",
    "estado": "ACTIVA"
  }
]
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Datos administrativos obtenidos correctamente |
| 204 No Content | No existen registros disponibles |
| 500 Internal Server Error | Error interno del microservicio |

---

## Validación mediante Postman

Para validar la HU-50 se recomienda usar la colección principal del proyecto:

```txt
postman/EcoMarket-SPA.postman_collection.json
postman/EcoMarket-SPA-local.postman_environment.json
```

El ambiente local debe considerar:

| Variable | Valor recomendado |
|---|---|
| base_url | http://localhost:8080 |
| api_gateway_port | 8080 |
| ms_usuarios_port | 8081 |
| ms_catalogo_port | 8082 |
| ms_inventario_port | 8083 |
| ms_pedidos_port | 8084 |
| ms_logistica_port | 8085 |
| ms_admin_port | 8086 |
| ms_reportes_port | 8087 |

---

## Evidencia recomendada en Postman

Para validar la HU-50 se recomienda registrar evidencia de los siguientes endpoints:

| Validación | Método | Ruta |
|---|---|---|
| Health API Gateway | GET | `/actuator/health` |
| Consultar stock | GET | `/stock?idProducto=1&idTienda=1` |
| Crear envío | POST | `/envios` |
| Consultar ventas | GET | `/ventas` |
| Consultar pedidos | GET | `/pedidos` |
| Consultar inventario | GET | `/inventario` |
| Consultar tiendas | GET | `/admin/tiendas` |
| Consultar tickets | GET | `/soporte/tickets` |
| Consultar reportes | GET | `/reportes` |
| Consultar KPI | GET | `/kpi` |

---

## Evidencias sugeridas para guardar

Las capturas o evidencias pueden guardarse en:

```txt
docs/evidencias/integracion/
```

Evidencias recomendadas:

```txt
01-health-api-gateway.png
02-consulta-stock.png
03-crear-envio.png
04-consulta-ventas.png
05-consulta-inventario.png
06-consulta-tiendas.png
07-consulta-reportes.png
```

Si no se usan imágenes, se puede dejar evidencia textual en un archivo posterior:

```txt
docs/evidencias/integracion/evidencia-postman-hu50.md
```

---

## Consideración sobre integración real

Durante el Sprint 3, algunos microservicios pueden encontrarse en estado parcial o en corrección.

Por este motivo, la validación de comunicación REST se puede realizar de dos formas:

1. **Validación real:** cuando ambos microservicios están implementados y levantados localmente.
2. **Validación documentada:** cuando un microservicio aún no está completamente disponible, pero se documentan ruta, request, response esperado y código HTTP.

Esto permite mantener trazabilidad técnica sin bloquear el avance del equipo.

---

## Flujo recomendado de prueba local

Para una validación completa, levantar primero el API Gateway:

```powershell
cd api-gateway
.\mvnw.cmd spring-boot:run
```

Luego levantar cada microservicio necesario en una terminal distinta:

```powershell
cd ms-inventario-abastecimiento
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-pedidos-ventas
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-logistica-envios
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-reportes
.\mvnw.cmd spring-boot:run
```

Después probar las rutas desde Postman usando:

```txt
http://localhost:8080
```

---

## Criterios de aceptación cubiertos

| Criterio de aceptación | Estado |
|---|---|
| Se identifican las comunicaciones REST principales | Cumplido |
| Existen pruebas que demuestran consumo entre servicios | Pendiente de evidencia Postman |
| Se documentan request, response y códigos HTTP | Cumplido |
| La integración puede explicarse en la defensa | Cumplido |

---

## Riesgos técnicos identificados

| Riesgo | Mitigación |
|---|---|
| Microservicio aún incompleto | Documentar request/response esperado |
| Endpoint no implementado todavía | Registrar como pendiente técnico |
| Puerto distinto al esperado | Revisar `application.properties` |
| Error de conexión entre servicios | Validar primero el endpoint directo y luego vía Gateway |
| Datos inexistentes en BD local | Crear datos de prueba mediante Postman |

---

## Resumen para defensa

La comunicación REST entre microservicios permite que EcoMarket SPA mantenga una arquitectura desacoplada y escalable.

Los flujos principales son:

1. **Pedidos y Ventas consulta Inventario** para validar stock.
2. **Pedidos y Ventas solicita Logística** para generar envíos.
3. **Reportes consume Ventas, Inventario y Administración** para generar KPIs y reportes ejecutivos.

El API Gateway centraliza el acceso externo y permite validar las rutas REST desde Postman usando un único punto de entrada.

---

## Conclusión

La comunicación REST entre microservicios en EcoMarket SPA permite desacoplar responsabilidades y mantener una arquitectura escalable.

Las integraciones más importantes son:

- Pedidos consulta stock en Inventario.
- Pedidos solicita despacho a Logística.
- Reportes consume datos de Ventas, Inventario y Administración.

Esta documentación respalda la HU-50 y sirve como evidencia técnica para la defensa del Sprint 3.