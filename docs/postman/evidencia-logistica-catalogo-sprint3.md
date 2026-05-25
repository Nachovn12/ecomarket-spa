# Evidencia de Pruebas Postman - Sprint 3
## Microservicios: ms-logistica-envios y ms-catalogo

Esta evidencia respalda el Sprint 3 de EcoMarket SPA, validando endpoints funcionales de Logistica de Envios y Catalogo desde Postman, junto con respuestas REST con HATEOAS.

---

## MS Logistica de Envios

### Envios

- POST /api/envios - OK (201 Created)
- GET /api/envios - OK (200 OK)
- GET /api/envios/{id} - OK (200 OK)
- GET /api/envios/pedido/{idPedido} - OK (200 OK)
- PATCH /api/envios/{id}/estado - OK (200 OK)
- PATCH /api/envios/{id}/incidencia - OK (200 OK)

### Proveedores

- POST /api/envios/proveedores - OK (201 Created)
- GET /api/envios/proveedores - OK (200 OK)
- GET /api/envios/proveedores/{id} - OK (200 OK)
- GET /api/envios/proveedores/activos - OK (200 OK)
- GET /api/envios/proveedores/buscar?tipo={tipo}&cobertura={cobertura} - OK (200 OK)
- PUT /api/envios/proveedores/{id} - OK (200 OK)
- PATCH /api/envios/proveedores/{id}/activar - OK (204 No Content)
- PATCH /api/envios/proveedores/{id}/desactivar - OK (204 No Content)

### Rutas

- POST /api/rutas - OK (201 Created)
- GET /api/rutas - OK (200 OK)
- GET /api/rutas/{id} - OK (200 OK)
- PUT /api/rutas/{id} - OK (200 OK)
- PATCH /api/rutas/{id}/estado - OK (200 OK)
- DELETE /api/rutas/{id} - OK (204 No Content)

---

## MS Catalogo

### Productos

- POST /api/productos - OK (201 Created)
- GET /api/productos - OK (200 OK)
- GET /api/productos/{id} - OK (200 OK)
- PUT /api/productos/{id} - OK (200 OK)
- DELETE /api/productos/{id} - OK (204 No Content)

### Busqueda de productos

- GET /api/productos/buscar?palabraClave={texto} - OK (200 OK)
- GET /api/productos/buscar?idCategoria={idCategoria} - OK (200 OK)
- GET /api/productos/buscar?precioMinimo={min}&precioMaximo={max} - OK (200 OK)
- GET /api/productos/ecologicos?atributoEcologico={atributo} - OK (200 OK)

### Categorias

- POST /api/categorias - OK (201 Created)
- GET /api/categorias - OK (200 OK)
- GET /api/categorias/{id} - OK (200 OK)
- PUT /api/categorias/{id} - OK (200 OK)
- DELETE /api/categorias/{id} - OK (204 No Content)

### Resenas

- POST /api/resenas - OK (201 Created)

---

## Validacion HATEOAS

Las respuestas principales incorporan navegacion REST mediante `_links`, principalmente relaciones `self`, colecciones relacionadas y endpoints de consulta.

Ejemplo esperado:

```json
{
  "idProducto": 1,
  "sku": "ECO-SHAMPOO-001",
  "nombre": "Shampoo solido ecologico",
  "precio": 4990,
  "_links": {
    "self": {
      "href": "http://localhost:8084/api/productos/1"
    },
    "productos": {
      "href": "http://localhost:8084/api/productos"
    }
  }
}
```

---

## Issues respaldadas

| Issue | Cobertura |
|---|---|
| HU-6 | Busqueda de productos ecologicos por palabra clave, categoria y precio |
| HU-20 | Gestion de envios |
| HU-22 | Actualizacion de estado de pedidos en transito |
| HU-23 | Gestion de proveedores |
| HU-49 | CRUD del MS Catalogo |
| HU-54 | CRUD del MS Logistica de Envios |
| HU-55 | Validacion de endpoints de Catalogo y Logistica en Postman |

---

## Nota

Las capturas de pantalla de payloads JSON y respuestas HATEOAS deben mantenerse como evidencia complementaria en Jira o carpeta de evidencias del proyecto.