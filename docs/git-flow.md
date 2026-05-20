# Flujo Git - EcoMarket SPA

## Ramas principales

### main
Rama estable/final del proyecto. No se trabaja directamente sobre esta rama.

### develop
Rama principal de integración del equipo. Todo avance validado desde las ramas feature se integra primero en develop.

## Ramas por microservicio

- feature/ms-usuarios-identidad
- feature/ms-catalogo
- feature/ms-inventario-abastecimiento
- feature/ms-pedidos-ventas
- feature/ms-logistica-envios
- feature/ms-administracion-soporte
- feature/ms-reportes

## Asignación por integrante

### Ignacio Valeria
- feature/ms-usuarios-identidad
- feature/ms-administracion-soporte
- Apoyo en develop, arquitectura e integración.

### Benjamín Flores
- feature/ms-pedidos-ventas

### Benjamín Palma
- feature/ms-inventario-abastecimiento
- feature/ms-reportes

### Benjamín Espinoza
- feature/ms-catalogo
- feature/ms-logistica-envios

## Convención de commits

- docs(TT-01): documentar arquitectura de microservicios
- chore(TT-02): definir flujo Git por microservicio
- feat(HU-1): implementar registro de cliente
- feat(HU-16): agregar producto al inventario
- test(HU-19): agregar prueba de consulta de stock
- docs(TT-03): documentar estructura Spring Boot Maven

## Flujo de trabajo

1. Cada integrante trabaja en su rama feature por microservicio.
2. Los commits deben mencionar la HU o TT correspondiente.
3. Los avances se integran primero en develop.
4. develop se usa como rama de integración.
5. main queda reservada para avances estables y validados.
