# Pruebas Postman para Defensa EP2 — EcoMarket SPA

**Asignatura:** Desarrollo Full Stack I — DSY1103
**Proyecto:** EcoMarket SPA
**Ubicación recomendada en el repo:** `docs/postman/pruebas-rubrica-demo-ep2.md`

## Objetivo

Este documento reúne las pruebas prácticas que el equipo debe dominar para la defensa técnica. Está pensado para copiar y pegar requests en Postman y demostrar lo que pide la rúbrica: microservicios, patrón CSR, persistencia JPA, CRUD REST, reglas de negocio, validaciones, excepciones, logs, HATEOAS, API Gateway, GitHub/Jira y evidencias.

---

## 1. Preparación previa

### 1.1 Abrir XAMPP

1. Abrir **XAMPP Control Panel**.
2. Iniciar **Apache** y **MySQL**.
3. Abrir phpMyAdmin:

```text
http://localhost/phpmyadmin
```

4. Validar que existan las bases:

```text
bd_usuarios
bd_catalogo
bd_inventario
bd_ventas
bd_logistica
bd_admin
bd_reportes
```

Si falta alguna, ejecutar:

```sql
CREATE DATABASE IF NOT EXISTS bd_usuarios;
CREATE DATABASE IF NOT EXISTS bd_catalogo;
CREATE DATABASE IF NOT EXISTS bd_inventario;
CREATE DATABASE IF NOT EXISTS bd_ventas;
CREATE DATABASE IF NOT EXISTS bd_logistica;
CREATE DATABASE IF NOT EXISTS bd_admin;
CREATE DATABASE IF NOT EXISTS bd_reportes;
```

### 1.2 Levantar microservicios

Abrir una terminal por servicio:

```powershell
mvn -f api-gateway/pom.xml spring-boot:run
mvn -f ms-usuarios-identidad/pom.xml spring-boot:run
mvn -f ms-catalogo/pom.xml spring-boot:run
mvn -f ms-inventario-abastecimiento/pom.xml spring-boot:run
mvn -f ms-pedidos-ventas/pom.xml spring-boot:run
mvn -f ms-logistica-envios/pom.xml spring-boot:run
mvn -f ms-administracion-soporte/pom.xml spring-boot:run
mvn -f ms-reportes/pom.xml spring-boot:run
```

Validar puertos:

```powershell
netstat -ano | findstr :8081
netstat -ano | findstr :8083
netstat -ano | findstr :8084
netstat -ano | findstr :8085
netstat -ano | findstr :8086
netstat -ano | findstr :8087
netstat -ano | findstr :8088
netstat -ano | findstr :8089
```

Todos deben aparecer en `LISTENING`.

### 1.3 Environment de Postman

Seleccionar o crear environment:

```text
EcoMarket SPA - Local Environment
```

Variables:

| Variable         | Valor                   |
| ---------------- | ----------------------- |
| `gateway_url`    | `http://localhost:8081` |
| `usuarios_url`   | `http://localhost:8083` |
| `catalogo_url`   | `http://localhost:8084` |
| `inventario_url` | `http://localhost:8085` |
| `pedidos_url`    | `http://localhost:8086` |
| `logistica_url`  | `http://localhost:8087` |
| `admin_url`      | `http://localhost:8088` |
| `reportes_url`   | `http://localhost:8089` |

---

## 2. Matriz rúbrica → prueba en vivo

| Rúbrica           | Prueba que se debe mostrar                                        |
| ----------------- | ----------------------------------------------------------------- |
| CSR               | Abrir Controller → Service → Repository → Entity/DTO              |
| Persistencia      | Ejecutar POST, luego GET y revisar phpMyAdmin                     |
| CRUD REST         | Probar POST, GET, PUT/PATCH y DELETE cuando aplique               |
| Reglas de negocio | Cupón, cancelación de pedido, factura duplicada, cambio de estado |
| Bean Validation   | Enviar JSON inválido y mostrar 400 Bad Request                    |
| Excepciones       | Login inválido, factura duplicada, recurso inexistente            |
| Logs              | Ejecutar endpoint y mostrar consola del microservicio             |
| Comunicación REST | Probar directo y por API Gateway                                  |
| GitHub/Jira       | Mostrar `docs/evidencias-tecnicas/`, commits y HU                 |

---

# BLOQUE A — MS Usuarios e Identidad

## A1. Registrar cliente

```http
POST {{usuarios_url}}/api/usuarios/registro
```

Body:

```json
{
  "nombre": "Cliente Demo",
  "correo": "cliente.demo@ecomarket.cl",
  "password": "Demo1234"
}
```

Esperado:

```text
201 Created
```

Validar que la respuesta incluya:

```text
id
nombre
correo
rol CLIENTE
activo true
_links
```

**Qué explicar:**
El `UsuarioController` recibe el JSON, valida el DTO y delega en `UsuarioService`. El service normaliza correo, valida duplicidad, asigna rol `CLIENTE` y persiste mediante `UsuarioRepository`.

---

## A2. Login correcto

```http
POST {{usuarios_url}}/api/auth/login
```

Body:

```json
{
  "correo": "cliente.demo@ecomarket.cl",
  "password": "Demo1234"
}
```

Esperado:

```text
200 OK
```

Validar que responda:

```text
mensaje Inicio de sesión exitoso
tokenSesion
rol CLIENTE
funcionalidadesDisponibles
```

**Qué explicar:**
El sistema valida credenciales y usuario activo. Retorna un token académico para evidenciar seguridad básica.

---

## A3. Login incorrecto

```http
POST {{usuarios_url}}/api/auth/login
```

Body:

```json
{
  "correo": "cliente.demo@ecomarket.cl",
  "password": "incorrecta"
}
```

Esperado:

```text
401 Unauthorized
```

**Qué explicar:**
El error se controla correctamente, no se cae el microservicio y se responde con código HTTP adecuado.

---

## A4. Validación correo inválido

```http
POST {{usuarios_url}}/api/usuarios/registro
```

Body:

```json
{
  "nombre": "Cliente Error",
  "correo": "correo-invalido",
  "password": "Demo1234"
}
```

Esperado:

```text
400 Bad Request
```

**Qué explicar:**
Bean Validation evita persistir datos inválidos.

---

# BLOQUE B — MS Catálogo

## B1. Crear categoría

```http
POST {{catalogo_url}}/api/categorias
```

Body:

```json
{
  "nombre": "Cuidado personal",
  "descripcion": "Productos ecológicos para higiene diaria"
}
```

Esperado:

```text
201 Created o 200 OK
```

Si ya existe, usar:

```json
{
  "nombre": "Cuidado personal demo",
  "descripcion": "Productos ecológicos para higiene diaria"
}
```

---

## B2. Crear producto

```http
POST {{catalogo_url}}/api/productos
```

Body:

```json
{
  "sku": "ECO-SHAMPOO-001",
  "nombre": "Shampoo sólido ecológico",
  "precio": 4990,
  "descripcion": "Shampoo sólido para uso diario",
  "descripcionEcologica": "Producto biodegradable y sin envase plástico",
  "idCategoria": 1
}
```

Esperado:

```text
201 Created o 200 OK
```

Si el SKU ya existe, usar `ECO-SHAMPOO-002`.

---

## B3. Buscar producto

```http
GET {{catalogo_url}}/api/productos/buscar?palabraClave=shampoo
```

Esperado:

```text
200 OK
```

**Qué explicar:**
La búsqueda se implementa en `CatalogoService`, usando repositorios JPA para consultar productos por criterios del dominio.

---

## B4. Producto inválido

```http
POST {{catalogo_url}}/api/productos
```

Body:

```json
{
  "sku": "ECO-ERROR-001",
  "nombre": "",
  "precio": -100,
  "descripcion": "Producto inválido",
  "descripcionEcologica": "Producto inválido",
  "idCategoria": 1
}
```

Esperado:

```text
400 Bad Request
```

---

# BLOQUE C — MS Inventario y Abastecimiento

## C1. Crear producto de inventario

```http
POST {{inventario_url}}/api/productos
```

Body:

```json
{
  "nombre": "Shampoo sólido ecológico",
  "sku": "ECO-SHAMPOO-001",
  "precio": 4990,
  "stock": 30,
  "categoria": "Cuidado personal",
  "sucursal": "Santiago"
}
```

Esperado:

```text
201 Created o 200 OK
```

Si el SKU ya existe, usar `ECO-SHAMPOO-002`.

---

## C2. Consultar producto por SKU

```http
GET {{inventario_url}}/api/productos/sku/ECO-SHAMPOO-001
```

Esperado:

```text
200 OK
```

---

## C3. Listar productos de inventario

```http
GET {{inventario_url}}/api/productos
```

Esperado:

```text
200 OK
```

---

## C4. Ajustar stock

```http
POST {{inventario_url}}/api/inventario/ajustes
```

Body:

```json
{
  "idProducto": 1,
  "cantidadNueva": 25,
  "motivo": "Ajuste por conteo físico",
  "usuarioResponsable": "Gerente de Tienda"
}
```

Esperado:

```text
201 Created o 200 OK
```

**Qué explicar:**
Inventario gestiona stock y disponibilidad. La lógica está en el service y se persiste en `bd_inventario`.

---

# BLOQUE D — MS Pedidos y Ventas

## D1. Crear carrito

```http
POST {{pedidos_url}}/api/pedidos/carritos
```

Body:

```json
{
  "idCliente": 1
}
```

Esperado:

```text
201 Created o 200 OK
```

Guardar el `id` del carrito.

---

## D2. Agregar item al carrito

```http
POST {{pedidos_url}}/api/pedidos/carritos/1/items
```

Body:

```json
{
  "idProducto": 1,
  "nombreProducto": "Shampoo sólido ecológico",
  "cantidad": 2,
  "precioUnitario": 4990,
  "stockDisponible": 30
}
```

Esperado:

```text
200 OK
```

**Qué explicar:**
`CarritoService` valida stock recibido, agrega el producto y recalcula totales.

---

## D3. Crear cupón

```http
POST {{pedidos_url}}/api/pedidos/cupones
```

Body:

```json
{
  "codigo": "ECO10",
  "tipoDescuento": "PORCENTAJE",
  "valor": 10,
  "montoMinimo": 5000,
  "activo": true
}
```

Esperado:

```text
201 Created o 200 OK
```

Si ya existe, usar `ECO15`.

---

## D4. Aplicar cupón

```http
POST {{pedidos_url}}/api/pedidos/carritos/1/cupon
```

Body:

```json
{
  "codigo": "ECO10"
}
```

Esperado:

```text
200 OK
```

---

## D5. Crear pedido desde carrito

```http
POST {{pedidos_url}}/api/pedidos/desde-carrito/1
```

Esperado:

```text
201 Created o 200 OK
estado PENDIENTE
```

**Qué explicar:**
El pedido se genera desde el carrito, el carrito cambia de estado, se registra historial y se mantiene trazabilidad.

---

## D6. Consultar estado del pedido

```http
GET {{pedidos_url}}/api/pedidos/1/estado
```

Esperado:

```text
200 OK
```

---

## D7. Cancelar pedido pendiente

```http
PATCH {{pedidos_url}}/api/pedidos/1/cancelar
```

Esperado:

```text
200 OK
estado CANCELADO
```

**Qué explicar:**
Solo se permite cancelar pedidos en estado `PENDIENTE`.

---

## D8. Registrar venta presencial

```http
POST {{pedidos_url}}/api/ventas/presencial
```

Body:

```json
{
  "idCliente": 1,
  "idTienda": 1,
  "items": [
    {
      "idProducto": 1,
      "nombreProducto": "Shampoo sólido ecológico",
      "cantidad": 2,
      "precioUnitario": 4990
    }
  ],
  "descuento": 0
}
```

Esperado:

```text
201 Created o 200 OK
```

Guardar `idVenta`.

---

## D9. Generar factura

```http
POST {{pedidos_url}}/api/ventas/1/factura
```

Esperado:

```text
201 Created o 200 OK
```

---

## D10. Factura duplicada

Ejecutar nuevamente:

```http
POST {{pedidos_url}}/api/ventas/1/factura
```

Esperado:

```text
409 Conflict
```

**Qué explicar:**
La regla evita generar dos facturas para la misma venta. Se demuestra regla de negocio y excepción controlada.

---

# BLOQUE E — MS Logística de Envíos

## E1. Crear proveedor

```http
POST {{logistica_url}}/api/envios/proveedores
```

Body:

```json
{
  "rut": "76123456-7",
  "razonSocial": "Transporte Verde SPA",
  "tipoProveedor": "TRANSPORTE",
  "cobertura": "Santiago",
  "contacto": "Operaciones",
  "telefono": "+56911112222",
  "email": "contacto@transporteverde.cl",
  "activo": true
}
```

Esperado:

```text
201 Created o 200 OK
```

---

## E2. Crear ruta

```http
POST {{logistica_url}}/api/rutas
```

Body:

```json
{
  "estado": "PLANIFICADA"
}
```

Esperado:

```text
201 Created o 200 OK
```

---

## E3. Crear envío

```http
POST {{logistica_url}}/api/envios
```

Body:

```json
{
  "idPedido": 1,
  "origen": "Tienda Santiago",
  "destino": "Av. Siempre Viva 123",
  "ubicacionActual": "Centro de distribución",
  "estado": "PREPARADO",
  "fechaEstimadaEntrega": "2026-05-28T12:00:00"
}
```

Esperado:

```text
201 Created o 200 OK
```

---

## E4. Consultar envíos

```http
GET {{logistica_url}}/api/envios
```

Esperado:

```text
200 OK
```

---

## E5. Cambiar estado de envío

```http
PATCH {{logistica_url}}/api/envios/1/estado
```

Body:

```json
{
  "estado": "EN_CAMINO"
}
```

Esperado:

```text
200 OK
```

**Qué explicar:**
Logística separa envíos, rutas, proveedores y seguimiento del flujo comercial.

---

# BLOQUE F — MS Administración y Soporte

## F1. Crear tienda

```http
POST {{admin_url}}/api/admin/tiendas
```

Body:

```json
{
  "nombre": "EcoMarket Lastarria",
  "ciudad": "Santiago",
  "horarioApertura": "09:00",
  "horarioCierre": "19:00"
}
```

Esperado:

```text
201 Created o 200 OK
```

---

## F2. Listar tiendas

```http
GET {{admin_url}}/api/admin/tiendas
```

Esperado:

```text
200 OK
```

---

## F3. Crear ticket de soporte

```http
POST {{admin_url}}/api/soporte/tickets
```

Body:

```json
{
  "idCliente": 1,
  "correoCliente": "cliente.demo@ecomarket.cl",
  "asunto": "Consulta por pedido",
  "descripcion": "Cliente solicita información sobre el estado del pedido.",
  "prioridad": "MEDIA"
}
```

Esperado:

```text
201 Created o 200 OK
```

---

## F4. Responder ticket

```http
POST {{admin_url}}/api/soporte/tickets/1/respuestas
```

Body:

```json
{
  "respuesta": "Su pedido se encuentra en preparación.",
  "respondidoPor": "Equipo de Soporte"
}
```

Esperado:

```text
201 Created o 200 OK
```

---

## F5. Error por correo inválido

```http
POST {{admin_url}}/api/soporte/tickets
```

Body:

```json
{
  "idCliente": 1,
  "correoCliente": "correo-invalido",
  "asunto": "Consulta inválida",
  "descripcion": "Prueba de validación.",
  "prioridad": "MEDIA"
}
```

Esperado:

```text
400 Bad Request
```

---

# BLOQUE G — MS Reportes

## G1. Crear reporte

```http
POST {{reportes_url}}/api/reportes
```

Body:

```json
{
  "tipo": "VENTAS",
  "fechaInicio": "2026-05-01",
  "fechaFin": "2026-05-25",
  "idTienda": 1,
  "generadoPor": "Gerente de Tienda",
  "formatoExportacion": "PDF"
}
```

Esperado:

```text
201 Created o 200 OK
```

---

## G2. Listar reportes

```http
GET {{reportes_url}}/api/reportes
```

Esperado:

```text
200 OK
```

---

## G3. Crear KPI

```http
POST {{reportes_url}}/api/kpi
```

Body:

```json
{
  "tipo": "VENTAS_TOTALES",
  "valor": 1250000,
  "descripcion": "Ventas totales del periodo"
}
```

Esperado:

```text
201 Created o 200 OK
```

---

## G4. Consultar KPIs

```http
GET {{reportes_url}}/api/kpi
```

Esperado:

```text
200 OK
```

---

# BLOQUE H — API Gateway

## H1. Registro de usuario vía Gateway

```http
POST {{gateway_url}}/api/usuarios/registro
```

Body:

```json
{
  "nombre": "Cliente Gateway",
  "correo": "cliente.gateway@ecomarket.cl",
  "password": "Demo1234"
}
```

Esperado:

```text
201 Created
```

---

## H2. Listar productos vía Gateway

```http
GET {{gateway_url}}/api/productos
```

Esperado:

```text
200 OK
```

---

## H3. Listar pedidos vía Gateway

```http
GET {{gateway_url}}/api/pedidos
```

Esperado:

```text
200 OK
```

**Qué explicar:**
El API Gateway centraliza el acceso y enruta la solicitud al microservicio correspondiente.

---

# 5. Validación de persistencia en phpMyAdmin

Después de ejecutar las pruebas, revisar registros en:

```text
bd_usuarios
bd_catalogo
bd_inventario
bd_ventas
bd_logistica
bd_admin
bd_reportes
```

Tablas sugeridas:

```text
usuarios
productos
categorias
inventario
carritos
pedidos
ventas
facturas
envios
proveedores
tiendas
tickets
reportes
indicadores_kpi
```

Frase para defensa:

> Ejecutamos un POST desde Postman, el microservicio procesó la solicitud, JPA/Hibernate persistió los datos y luego verificamos los registros con GET y phpMyAdmin.

---

# 6. Cambios en vivo para practicar

## 6.1 Agregar validación en DTO

Ejemplo:

```java
@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
private String password;
```

Probar con:

```json
{
  "nombre": "Cliente Error",
  "correo": "cliente.error@ecomarket.cl",
  "password": "123"
}
```

Esperado:

```text
400 Bad Request
```

---

## 6.2 Agregar regla de negocio en Service

Ejemplo:

```java
if (request.getCantidad() <= 0) {
    throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
}
```

Probar en carrito con cantidad `0`.

---

## 6.3 Agregar log

Ejemplo:

```java
log.info("Generando factura para venta {}", idVenta);
```

Luego ejecutar:

```http
POST {{pedidos_url}}/api/ventas/1/factura
```

Mostrar el log en consola.
