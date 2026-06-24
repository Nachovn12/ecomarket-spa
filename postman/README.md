# Colección Postman E2E — EcoMarket SPA

## Descripción
Colección Postman que cubre los endpoints clave de los 7 microservicios para validación end-to-end durante la defensa técnica EP3.

**HU-97:** Flujo de Usuarios & Catálogo  
**HU-98:** Flujo de Pedidos, Inventario & Logística  
**Épica transversal:** HU-67

## Cómo importar
1. Abrir Postman
2. Click en **Import** (esquina superior izquierda)
3. Arrastrar o seleccionar el archivo `EcoMarket-E2E.postman_collection.json`
4. Las variables se importan automáticamente con la colección

## Variables incluidas

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `base_url_gateway` | `http://localhost:8081` | URL del API Gateway |
| `token_jwt` | (vacío) | Se llena automáticamente al ejecutar login |
| `runCliente` | `12345678-5` | RUN del cliente de prueba (formato chileno) |
| `idPedido` | `1` | ID del pedido para consultas |
| `idCarrito` | `1` | ID del carrito (se captura dinámicamente al crear carrito) |
| `idProducto` | `1` | ID del producto (se captura dinámicamente de GET productos) |
| `idEnvio` | `1` | ID del envío (se captura dinámicamente de GET envíos) |

## Orden de ejecución sugerido (flujo E2E)
1. **Login** (`ms-usuarios / POST login`) — obtiene el token JWT y lo inyecta automáticamente
2. **Buscar productos** (`ms-catalogo / GET productos`) — captura `idProducto` dinámicamente
3. **Ver categorías** (`ms-catalogo / GET categorias`)
4. **Consultar stock** (`ms-inventario / GET inventario`)
5. **Crear carrito** (`ms-pedidos-ventas / POST carritos`) — captura `idCarrito` dinámicamente
6. **Consultar pedido** (`ms-pedidos-ventas / GET pedido por ID`)
7. **Listar envíos** (`ms-logistica / GET envíos`) — captura `idEnvio` dinámicamente
8. **Seguimiento de envío** (`ms-logistica / GET seguimiento`) — usa `{{idEnvio}}` capturado
9. **Ver tiendas** (`ms-administracion / GET tiendas`)
10. **Crear ticket** (`ms-administracion / POST tickets`)
11. **Consultar reportes** (`ms-reportes / GET reportes`)
12. **Consultar KPIs** (`ms-reportes / GET KPIs`)

## Assertions incluidas por endpoint

Cada request valida:
- ✅ **Status code** correcto (200 o 201 según corresponda)
- ✅ **Estructura de respuesta** — propiedades obligatorias del DTO
- ✅ **Tipo de dato** — arrays, strings, números según el campo
- ✅ **Captura dinámica** — IDs se propagan automáticamente entre requests

## Prerequisitos
- Los 7 microservicios deben estar corriendo en sus puertos respectivos (8083-8089)
- El API Gateway debe estar corriendo en el puerto 8081
- Ejecutar primero el request de **Login** para obtener el token JWT

## Microservicios y Puertos

| Microservicio | Puerto |
|---|---|
| API Gateway | 8081 |
| ms-usuarios-identidad | 8083 |
| ms-catalogo | 8084 |
| ms-inventario-abastecimiento | 8085 |
| ms-pedidos-ventas | 8086 |
| ms-logistica-envios | 8087 |
| ms-administracion-soporte | 8088 |
| ms-reportes | 8089 |
