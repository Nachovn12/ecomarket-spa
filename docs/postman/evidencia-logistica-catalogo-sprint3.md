# Evidencia de Pruebas Postman - Sprint 3
## Microservicio: ms-logistica-envios y ms-catalogo

Se han validado los siguientes endpoints utilizando Postman, confirmando respuestas exitosas (200 OK, 201 Created) y el uso estricto de HATEOAS:

### Envíos
- POST /api/envios - OK (201)
- GET /api/envios - OK (200)
- GET /api/envios/{id} - OK (200)
- GET /api/envios/pedido/{idPedido} - OK (200)
- PATCH /api/envios/{id}/estado - OK (200)
- PATCH /api/envios/{id}/incidencia - OK (200)

### Proveedores
- POST /api/envios/proveedores - OK (201)
- GET /api/envios/proveedores/activos - OK (200)
- PATCH /api/envios/proveedores/{id}/desactivar - OK (200)

### Rutas
- POST /api/rutas - OK (201)
- PATCH /api/rutas/{id}/estado - OK (200)

**Nota:** Las capturas de pantalla de los payloads JSON y respuestas HATEOAS se encuentran adjuntas en el ticket de Jira correspondiente (HU-55).
