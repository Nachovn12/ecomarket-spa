# EcoMarket SPA - Documentación HATEOAS Base

## HU-51 — TT-07 Implementar documentación HATEOAS base

## Objetivo

Documentar el uso de **HATEOAS** en los endpoints principales de EcoMarket SPA, explicando su propósito, beneficios, estructura de respuesta y ejemplos aplicados a los microservicios del proyecto.

Esta documentación respalda la HU-51 y permite demostrar técnicamente cómo la API REST puede entregar navegación entre recursos relacionados mediante enlaces.

---

## ¿Qué es HATEOAS?

**HATEOAS** significa:

```txt
Hypermedia As The Engine Of Application State
```

En una API REST, HATEOAS permite que las respuestas incluyan enlaces relacionados para que el cliente pueda navegar entre recursos sin conocer previamente todas las rutas del sistema.

En vez de entregar solo datos planos, la respuesta incluye una sección de enlaces, normalmente llamada:

```json
"_links"
```

Esto permite que el consumidor de la API identifique acciones disponibles como:

- Consultar el recurso actual.
- Volver a la colección principal.
- Actualizar un recurso.
- Eliminar un recurso.
- Acceder a recursos relacionados.

---

## Uso de HATEOAS en EcoMarket SPA

EcoMarket SPA utiliza una arquitectura de microservicios con API Gateway, donde cada microservicio expone endpoints REST.

HATEOAS ayuda a mejorar la navegación y comprensión de la API, especialmente en endpoints principales como:

| Microservicio | Endpoints representativos |
|---|---|
| MS Catálogo | `/productos`, `/productos/{id}`, `/categorias` |
| MS Inventario y Abastecimiento | `/inventario`, `/stock` |
| MS Pedidos y Ventas | `/pedidos`, `/pedidos/carritos`, `/ventas` |
| MS Logística de Envíos | `/envios`, `/rutas` |
| MS Administración y Soporte | `/admin/tiendas`, `/soporte/tickets` |
| MS Reportes | `/reportes`, `/kpi` |

---

## Beneficios de HATEOAS

HATEOAS aporta beneficios técnicos importantes para EcoMarket SPA:

| Beneficio | Descripción |
|---|---|
| Navegabilidad | Permite descubrir rutas relacionadas desde la respuesta |
| Bajo acoplamiento | El cliente no necesita conocer todas las URLs previamente |
| Claridad REST | Mejora la estructura de respuestas de la API |
| Escalabilidad | Facilita extender recursos y acciones futuras |
| Evidencia técnica | Permite demostrar uso avanzado de API REST con Spring Boot |

---

## Dependencia requerida

Cada microservicio que implemente HATEOAS debe incluir la dependencia correspondiente en su `pom.xml`.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

---

## Clases principales de Spring HATEOAS

Las clases más utilizadas son:

| Clase | Uso |
|---|---|
| `EntityModel<T>` | Representa un recurso individual con enlaces |
| `CollectionModel<T>` | Representa una colección de recursos con enlaces |
| `WebMvcLinkBuilder` | Permite construir enlaces hacia métodos del controller |
| `linkTo()` | Crea un enlace hacia un método o recurso |
| `methodOn()` | Referencia un método del controller |
| `withSelfRel()` | Define el enlace propio del recurso |
| `withRel()` | Define un enlace relacionado |

---

## Ejemplo base de HATEOAS en un recurso individual

Ejemplo aplicado a un producto del **MS Catálogo**.

### Endpoint

```http
GET /productos/1
```

### Response sin HATEOAS

```json
{
  "idProducto": 1,
  "sku": "ECO-SHAMPOO-001",
  "nombre": "Shampoo sólido ecológico",
  "precio": 4990,
  "descripcionEcologica": "Biodegradable y libre de plástico"
}
```

### Response con HATEOAS

```json
{
  "idProducto": 1,
  "sku": "ECO-SHAMPOO-001",
  "nombre": "Shampoo sólido ecológico",
  "precio": 4990,
  "descripcionEcologica": "Biodegradable y libre de plástico",
  "_links": {
    "self": {
      "href": "http://localhost:8081/productos/1"
    },
    "productos": {
      "href": "http://localhost:8081/productos"
    },
    "categorias": {
      "href": "http://localhost:8081/categorias"
    }
  }
}
```

---

## Ejemplo conceptual en Spring Boot

```java
EntityModel<Producto> model = EntityModel.of(producto);

model.add(
    linkTo(methodOn(ProductoController.class)
    .obtenerProducto(producto.getIdProducto()))
    .withSelfRel()
);

model.add(
    linkTo(methodOn(ProductoController.class)
    .listarProductos())
    .withRel("productos")
);
```

---

## Ejemplo de colección con HATEOAS

### Endpoint

```http
GET /productos
```

### Response esperado

```json
{
  "_embedded": {
    "productoList": [
      {
        "idProducto": 1,
        "sku": "ECO-SHAMPOO-001",
        "nombre": "Shampoo sólido ecológico",
        "precio": 4990,
        "_links": {
          "self": {
            "href": "http://localhost:8081/productos/1"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8081/productos"
    }
  }
}
```

---

## Ejemplo conceptual de colección en Spring Boot

```java
List<EntityModel<Producto>> productos = catalogoService.listarProductos()
    .stream()
    .map(producto -> EntityModel.of(producto,
        linkTo(methodOn(ProductoController.class)
        .obtenerProducto(producto.getIdProducto()))
        .withSelfRel()
    ))
    .toList();

CollectionModel<EntityModel<Producto>> collection = CollectionModel.of(productos);

collection.add(
    linkTo(methodOn(ProductoController.class)
    .listarProductos())
    .withSelfRel()
);
```

---

# HATEOAS por microservicio

## 1. MS Catálogo

### Endpoints principales

```http
GET /productos
GET /productos/{idProducto}
GET /productos/ecologicos
GET /categorias
```

### Enlaces recomendados

| Recurso | Enlaces HATEOAS |
|---|---|
| Producto | `self`, `productos`, `categorias`, `resenas` |
| Categoría | `self`, `categorias`, `productos` |
| Reseña | `self`, `producto` |

### Ejemplo esperado

```json
{
  "idProducto": 1,
  "sku": "ECO-SHAMPOO-001",
  "nombre": "Shampoo sólido ecológico",
  "_links": {
    "self": {
      "href": "http://localhost:8081/productos/1"
    },
    "productos": {
      "href": "http://localhost:8081/productos"
    },
    "resenas": {
      "href": "http://localhost:8081/productos/1/resenas"
    }
  }
}
```

---

## 2. MS Inventario y Abastecimiento

### Endpoints principales

```http
GET /inventario
GET /inventario/{idInventario}
GET /stock?idProducto=1&idTienda=1
POST /stock/reservas
```

### Enlaces recomendados

| Recurso | Enlaces HATEOAS |
|---|---|
| Inventario | `self`, `inventario`, `stock`, `reservas` |
| ReservaStock | `self`, `stock`, `inventario` |
| MovimientoStock | `self`, `inventario` |

### Ejemplo esperado

```json
{
  "idInventario": 1,
  "idProducto": 1,
  "idTienda": 1,
  "stockDisponible": 50,
  "stockReservado": 0,
  "_links": {
    "self": {
      "href": "http://localhost:8081/inventario/1"
    },
    "stock": {
      "href": "http://localhost:8081/stock?idProducto=1&idTienda=1"
    },
    "reservas": {
      "href": "http://localhost:8081/stock/reservas"
    }
  }
}
```

---

## 3. MS Pedidos y Ventas

### Endpoints principales

```http
GET /pedidos
GET /pedidos/{idPedido}
POST /pedidos
GET /pedidos/carritos
POST /pedidos/carritos/{idCarrito}/cupon
POST /ventas/pagos
POST /ventas/facturas
```

### Enlaces recomendados

| Recurso | Enlaces HATEOAS |
|---|---|
| Pedido | `self`, `pedidos`, `pago`, `factura`, `envio` |
| CarritoCompra | `self`, `items`, `cupon`, `pedido` |
| Venta | `self`, `pago`, `factura` |
| Factura | `self`, `venta` |

### Ejemplo esperado

```json
{
  "idPedido": 1,
  "idCliente": 1,
  "estado": "CONFIRMADO",
  "total": 24990,
  "_links": {
    "self": {
      "href": "http://localhost:8081/pedidos/1"
    },
    "pedidos": {
      "href": "http://localhost:8081/pedidos"
    },
    "pago": {
      "href": "http://localhost:8081/ventas/pagos"
    },
    "factura": {
      "href": "http://localhost:8081/ventas/facturas"
    },
    "envio": {
      "href": "http://localhost:8081/envios"
    }
  }
}
```

---

## 4. MS Logística de Envíos

### Endpoints principales

```http
GET /envios
GET /envios/{idEnvio}
GET /envios/{idEnvio}/seguimiento
POST /envios
GET /rutas
```

### Enlaces recomendados

| Recurso | Enlaces HATEOAS |
|---|---|
| Envio | `self`, `seguimiento`, `rutas`, `pedido` |
| RutaEntrega | `self`, `envios` |
| SeguimientoEnvio | `self`, `envio` |

### Ejemplo esperado

```json
{
  "idEnvio": 1,
  "idPedido": 1,
  "estado": "PREPARADO",
  "direccionDestino": "Barrio Lastarria, Santiago",
  "_links": {
    "self": {
      "href": "http://localhost:8081/envios/1"
    },
    "seguimiento": {
      "href": "http://localhost:8081/envios/1/seguimiento"
    },
    "rutas": {
      "href": "http://localhost:8081/rutas"
    }
  }
}
```

---

## 5. MS Administración y Soporte

### Endpoints principales

```http
GET /admin/tiendas
GET /admin/tiendas/{idTienda}
GET /soporte/tickets
GET /soporte/tickets/{idTicket}
POST /soporte/tickets
```

### Enlaces recomendados

| Recurso | Enlaces HATEOAS |
|---|---|
| Tienda | `self`, `tiendas`, `metricas` |
| TicketSoporte | `self`, `respuestas`, `tickets` |
| RespuestaSoporte | `self`, `ticket` |

### Ejemplo esperado

```json
{
  "idTicket": 1,
  "idCliente": 1,
  "prioridad": "MEDIA",
  "estado": "ABIERTO",
  "_links": {
    "self": {
      "href": "http://localhost:8081/soporte/tickets/1"
    },
    "tickets": {
      "href": "http://localhost:8081/soporte/tickets"
    },
    "respuestas": {
      "href": "http://localhost:8081/soporte/tickets/1/respuestas"
    }
  }
}
```

---

## 6. MS Reportes

### Endpoints principales

```http
GET /reportes
GET /reportes/ventas
GET /reportes/inventario
GET /kpi
POST /reportes/{idReporte}/exportaciones
```

### Enlaces recomendados

| Recurso | Enlaces HATEOAS |
|---|---|
| Reporte | `self`, `exportaciones`, `kpi` |
| IndicadorKPI | `self`, `reportes` |
| ExportacionReporte | `self`, `reporte` |

### Ejemplo esperado

```json
{
  "idReporte": 1,
  "tipoReporte": "VENTAS",
  "estado": "GENERADO",
  "_links": {
    "self": {
      "href": "http://localhost:8081/reportes/1"
    },
    "exportaciones": {
      "href": "http://localhost:8081/reportes/1/exportaciones"
    },
    "kpi": {
      "href": "http://localhost:8081/kpi"
    }
  }
}
```

---

# Evidencia HATEOAS esperada

Para validar la HU-51 se recomienda usar Postman y revisar que las respuestas incluyan:

```json
"_links"
```

Ejemplo mínimo:

```json
"_links": {
  "self": {
    "href": "http://localhost:8081/productos/1"
  }
}
```

Ejemplo más completo:

```json
"_links": {
  "self": {
    "href": "http://localhost:8081/productos/1"
  },
  "productos": {
    "href": "http://localhost:8081/productos"
  },
  "categorias": {
    "href": "http://localhost:8081/categorias"
  }
}
```

---

## Endpoints recomendados para evidenciar HATEOAS

| Microservicio | Endpoint | Tipo de evidencia |
|---|---|---|
| MS Catálogo | `GET /productos` | Colección con `_links` |
| MS Catálogo | `GET /productos/{idProducto}` | Recurso individual con `_links` |
| MS Inventario | `GET /inventario` | Colección o recurso con `_links` |
| MS Pedidos y Ventas | `GET /pedidos` | Colección o recurso con `_links` |
| MS Logística | `GET /envios` | Colección o recurso con `_links` |
| MS Administración y Soporte | `GET /soporte/tickets` | Colección o recurso con `_links` |
| MS Reportes | `GET /reportes` | Colección o recurso con `_links` |

---

## Relación con API Gateway

Las rutas deben validarse idealmente mediante el API Gateway:

```txt
http://localhost:8081
```

Ejemplos:

```http
GET http://localhost:8081/productos
GET http://localhost:8081/inventario
GET http://localhost:8081/pedidos
GET http://localhost:8081/envios
GET http://localhost:8081/reportes
```

Esto permite comprobar que el cliente REST consume una entrada unificada y que cada microservicio mantiene sus propias rutas internas.

---

## Consideración técnica

Durante el Sprint 3, algunos microservicios pueden estar parcialmente implementados o en corrección.

Por este motivo, esta HU se cumple mediante:

1. Documentación del uso esperado de HATEOAS.
2. Ejemplos de respuestas REST con `_links`.
3. Identificación de endpoints principales donde debe aplicarse.
4. Evidencia Postman cuando el endpoint ya esté disponible.
5. Observaciones técnicas para endpoints pendientes de implementar.

---

## Criterios de aceptación cubiertos

| Criterio de aceptación | Estado |
|---|---|
| Se justifica correctamente el uso de HATEOAS | Cumplido |
| Al menos un endpoint principal incluye o documenta enlaces | Cumplido |
| La respuesta REST muestra navegación coherente | Cumplido mediante ejemplos |
| La implementación puede ser explicada técnicamente | Cumplido |

---

## Checklist de validación HU-51

Antes de cerrar la HU-51, validar:

```txt
[ ] Se explica qué es HATEOAS.
[ ] Se justifica su uso en EcoMarket SPA.
[ ] Se mencionan EntityModel y CollectionModel.
[ ] Se menciona WebMvcLinkBuilder.
[ ] Se documentan ejemplos con _links.
[ ] Se identifican endpoints principales por microservicio.
[ ] Se explica su relación con API Gateway.
[ ] Se deja evidencia o ejemplo para Postman.
[ ] Se puede validar con evidencia técnica.
```

---

## Evidencia técnica validada

HATEOAS permite que las respuestas REST de EcoMarket SPA incluyan enlaces de navegación entre recursos.

Esto mejora la claridad de la API, reduce el acoplamiento entre cliente y backend, y permite que los consumidores REST descubran acciones relacionadas desde la propia respuesta.

En el proyecto, HATEOAS se aplica o documenta en endpoints principales de catálogo, inventario, pedidos, logística, administración y reportes.

---

## Conclusión

La documentación HATEOAS base permite demostrar que EcoMarket SPA no solo expone endpoints REST, sino que también considera navegabilidad y buenas prácticas de diseño de API.

Esta documentación respalda la HU-51 y sirve como evidencia técnica del Sprint 3.
