# Evidencia Postman — Sprint S4 — Benjamín Flores Arévalo

## Objetivo

Registrar evidencia de validación manual en Postman para endpoints asociados a HU-12 y HU-15 del MS Pedidos y Ventas.

---

## Endpoint 1

- **HU:** HU-12
- **Método:** GET
- **URL:** `http://localhost:8086/api/pedidos/{idPedido}/estado`
- **Body:** N/A
- **Código esperado:** 200
- **Código obtenido:** 200
- **Resultado:** OK — retorna estado actual del pedido.

## Endpoint 2

- **HU:** HU-12
- **Método:** GET
- **URL:** `http://localhost:8086/api/pedidos/clientes/{idCliente}/historial`
- **Body:** N/A
- **Código esperado:** 200
- **Código obtenido:** 200
- **Resultado:** OK — retorna lista de pedidos del cliente.

## Endpoint 3

- **HU:** HU-12
- **Método:** GET
- **URL:** `http://localhost:8086/api/pedidos/{idPedido}/historial`
- **Body:** N/A
- **Código esperado:** 200
- **Código obtenido:** 200
- **Resultado:** OK — retorna historial de estados del pedido.

## Endpoint 4

- **HU:** HU-15
- **Método:** POST
- **URL:** `http://localhost:8086/api/ventas/{idVenta}/devoluciones`
- **Body:**

```json
{
  "idCliente": 1,
  "motivo": "Producto defectuoso"
}
```

- **Código esperado:** 201
- **Código obtenido:** 201
- **Resultado:** OK — devolución registrada correctamente.

## Endpoint 5

- **HU:** HU-15
- **Método:** PATCH
- **URL:** `http://localhost:8086/api/devoluciones/{id}/estado`
- **Body:**

```json
{
  "estado": "APROBADA"
}
```

- **Código esperado:** 200
- **Código obtenido:** 200
- **Resultado:** OK — estado de devolución actualizado.

## Endpoint 6

- **HU:** HU-15
- **Método:** POST
- **URL:** `http://localhost:8086/api/reclamaciones`
- **Body:**

```json
{
  "idCliente": 1,
  "idPedido": 1,
  "motivo": "Pedido incompleto",
  "descripcion": "Faltaron 2 productos"
}
```

- **Código esperado:** 201
- **Código obtenido:** 201
- **Resultado:** OK — reclamación registrada correctamente.

## Endpoint 7

- **HU:** HU-15
- **Método:** PATCH
- **URL:** `http://localhost:8086/api/reclamaciones/{id}/estado`
- **Body:**

```json
{
  "estado": "CERRADA"
}
```

- **Código esperado:** 200
- **Código obtenido:** 200
- **Resultado:** OK — estado de reclamación actualizado.

## Endpoint 8 — Caso de error

- **HU:** HU-15
- **Método:** PATCH
- **URL:** `http://localhost:8086/api/devoluciones/1/estado`
- **Body:**

```json
{
  "estado": "INVALIDO"
}
```

- **Código esperado:** 400
- **Código obtenido:** 400
- **Resultado:** Error 400 — estado de devolución inválido.

---

## Evidencia técnica asociada

Las capturas reales de Postman deben adjuntarse en la carpeta de evidencias del equipo o en el tablero colaborativo utilizado para la entrega, manteniendo trazabilidad con las HU indicadas.

---

## Alcance académico

Este documento es de carácter académico. La entidad `Factura` representa el concepto dentro del sistema EcoMarket SPA y no integra servicios externos tributarios.
