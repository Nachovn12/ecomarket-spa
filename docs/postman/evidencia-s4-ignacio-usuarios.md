# Evidencia Postman — HU-3 Actualización de perfil de cliente

## Proyecto

EcoMarket SPA — Desarrollo Fullstack I / DSY1103

## Microservicio

MS Usuarios e Identidad

## HU relacionada

HU-3 — Historia: Actualización de perfil de cliente

## Objetivo

Validar que un Cliente Web pueda consultar y actualizar su perfil, incluyendo información personal, dirección de envío y medio de pago.

---

## 1. Registro de cliente base

### Endpoint

- Método: POST
- URL: http://localhost:8083/api/usuarios/registro

### Body

```json
{
  "nombre": "Ignacio Valeria",
  "correo": "ignacio.valeria@duocuc.cl",
  "password": "clave123"
}
```

### Resultado esperado

- Código esperado: 201 Created
- El sistema registra un usuario con rol `CLIENTE`.
- La respuesta no expone la contraseña.
- La respuesta incluye enlaces HATEOAS.

### Resultado obtenido

- Código obtenido:
- Resultado:
- Evidencia/captura:

---

## 2. Consulta de perfil de cliente

### Endpoint

- Método: GET
- URL: http://localhost:8083/api/usuarios/clientes/1/perfil

### Body

No aplica.

### Resultado esperado

- Código esperado: 200 OK
- El sistema retorna los datos del perfil del cliente.
- La respuesta incluye:
  - id
  - nombre
  - correo
  - telefono
  - direccionEnvio
  - medioPago
  - rol
  - activo
  - fechaRegistro
- La respuesta incluye enlaces HATEOAS.

### Resultado obtenido

- Código obtenido:
- Resultado:
- Evidencia/captura:

---

## 3. Actualización de perfil de cliente

### Endpoint

- Método: PUT
- URL: http://localhost:8083/api/usuarios/clientes/1/perfil

### Body

```json
{
  "nombre": "Ignacio Valeria",
  "correo": "ignacio.valeria.actualizado@duocuc.cl",
  "telefono": "+56912345678",
  "direccionEnvio": "Santiago Centro, Chile",
  "medioPago": "Tarjeta terminada en 1234"
}
```

### Resultado esperado

- Código esperado: 200 OK
- El sistema actualiza nombre, correo, teléfono, dirección de envío y medio de pago.
- La respuesta no expone contraseña.
- La respuesta incluye enlaces HATEOAS.
- El usuario mantiene rol `CLIENTE`.

### Resultado obtenido

- Código obtenido:
- Resultado:
- Evidencia/captura:

---

## 4. Validación de correo inválido

### Endpoint

- Método: PUT
- URL: http://localhost:8083/api/usuarios/clientes/1/perfil

### Body

```json
{
  "nombre": "Ignacio Valeria",
  "correo": "correo-invalido",
  "telefono": "+56912345678",
  "direccionEnvio": "Santiago Centro, Chile",
  "medioPago": "Tarjeta terminada en 1234"
}
```

### Resultado esperado

- Código esperado: 400 Bad Request
- El sistema rechaza el correo inválido.
- El sistema retorna un JSON de error con detalle de validaciones.
- No se actualiza el perfil.

### Resultado obtenido

- Código obtenido:
- Resultado:
- Evidencia/captura:

---

## 5. Validación de cliente inexistente

### Endpoint

- Método: GET
- URL: http://localhost:8083/api/usuarios/clientes/999/perfil

### Body

No aplica.

### Resultado esperado

- Código esperado: 404 Not Found
- El sistema informa que el cliente no existe.
- La respuesta mantiene formato JSON consistente.

### Resultado obtenido

- Código obtenido:
- Resultado:
- Evidencia/captura:

---

## Evidencia técnica asociada

### Compilación

```powershell
mvn -f ms-usuarios-identidad/pom.xml clean package -DskipTests
```

Resultado esperado:

```text
BUILD SUCCESS
```

### Tests

```powershell
mvn -f ms-usuarios-identidad/pom.xml test
```

Resultado esperado:

```text
BUILD SUCCESS
```

---

## Checklist HU-3

- [ ] Cliente puede consultar su perfil.
- [ ] Cliente puede actualizar datos personales.
- [ ] Cliente puede actualizar dirección de envío.
- [ ] Cliente puede actualizar medio de pago.
- [ ] Se validan datos inválidos.
- [ ] No se expone contraseña.
- [ ] Se usa HATEOAS.
- [ ] Se usa manejo global de errores.
- [ ] Se validó en Postman.

