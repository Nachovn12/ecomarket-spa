# EcoMarket SPA - API Gateway

## Objetivo

El API Gateway funciona como componente técnico de entrada para EcoMarket SPA.

No es un microservicio de negocio. La arquitectura mantiene 7 microservicios de dominio y agrega el API Gateway como capa técnica de enrutamiento REST.

## Rol en la arquitectura

El API Gateway centraliza el acceso a los microservicios y permite que el Frontend Web consuma una única URL base.

Esto evita que el cliente tenga que conocer directamente la ubicación de cada microservicio.

## Puerto

```txt
http://localhost:8081
````

## Rutas principales

| Ruta Gateway     | Microservicio destino          | Responsabilidad                                         |
| ---------------- | ------------------------------ | ------------------------------------------------------- |
| `/auth/**`       | MS Usuarios e Identidad        | Login y autenticación                                   |
| `/usuarios/**`   | MS Usuarios e Identidad        | Usuarios, clientes, usuarios internos, roles y permisos |
| `/productos/**`  | MS Catálogo                    | Productos ecológicos                                    |
| `/categorias/**` | MS Catálogo                    | Categorías de productos                                 |
| `/inventario/**` | MS Inventario y Abastecimiento | Inventario por tienda                                   |
| `/stock/**`      | MS Inventario y Abastecimiento | Stock, reservas y movimientos                           |
| `/pedidos/**`    | MS Pedidos y Ventas            | Pedidos y carrito                                       |
| `/ventas/**`     | MS Pedidos y Ventas            | Ventas, pagos y facturación                             |
| `/envios/**`     | MS Logística de Envíos         | Envíos y seguimiento                                    |
| `/rutas/**`      | MS Logística de Envíos         | Rutas de entrega                                        |
| `/admin/**`      | MS Administración y Soporte    | Administración interna                                  |
| `/soporte/**`    | MS Administración y Soporte    | Tickets y soporte                                       |
| `/reportes/**`   | MS Reportes                    | Reportes ejecutivos                                     |
| `/kpi/**`        | MS Reportes                    | Indicadores KPI                                         |

## Justificación técnica

El API Gateway mejora la arquitectura porque:

* Centraliza accesos.
* Ordena las rutas REST.
* Evita que el cliente consuma directamente todos los microservicios.
* Facilita la integración con el Frontend Web.
* Permite aplicar validaciones futuras desde una capa común.
* Favorece escalabilidad.
* Reduce el acoplamiento entre cliente y microservicios.
* Facilita la mantenibilidad del sistema.

## Restricciones

El API Gateway:

* No tiene base de datos.
* No tiene entidades JPA.
* No tiene repositorios.
* No implementa lógica de negocio.
* No reemplaza a los 7 microservicios definidos.
* Solo enruta peticiones REST hacia los servicios internos.

## Validación

* Compilación Maven del módulo `api-gateway`.
* Ejecución de pruebas unitarias.
* Ejecución local en puerto `8081`.
* Validación de `/actuator/health`.
* Rutas principales documentadas.

## Rama de trabajo

```txt
feature/api-gateway
```

## Pull Request

```txt
feature/api-gateway -> develop
```


