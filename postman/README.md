# Colección Postman E2E — EcoMarket SPA

## Descripción
Colección Postman que cubre los endpoints clave de los 7 microservicios para validación end-to-end durante la defensa técnica EP3.

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
| `runCliente` | `12345678-5` | RUN del cliente de prueba |
| `idPedido` | `1` | ID del pedido para consultas |
| `idCarrito` | `1` | ID del carrito de compras |
| `idProducto` | `1` | ID del producto |

## Orden de ejecución sugerido (flujo E2E)
1. **Login** (`ms-usuarios / POST login`) — obtiene el token JWT
2. **Buscar productos** (`ms-catalogo / GET productos`)
3. **Crear carrito** (`ms-pedidos-ventas / POST carritos`)
4. **Consultar pedido** (`ms-pedidos-ventas / GET pedido por ID`)
5. **Consultar stock** (`ms-inventario / GET inventario`)
6. **Consultar envío** (`ms-logistica / GET envíos`)
7. **Crear ticket** (`ms-administracion / POST tickets`)
8. **Consultar reportes** (`ms-reportes / GET reportes`)

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
