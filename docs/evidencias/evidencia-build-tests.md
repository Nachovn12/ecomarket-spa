# Evidencia de Build y Tests — EP2 EcoMarket SPA

## Proyecto

**EcoMarket SPA**
**Asignatura:** Desarrollo Full Stack I — DSY1103
**Sección:** 003D
**Entrega:** EP2 | Entrega de Encargo grupal Parte 1
**Arquitectura:** Backend con microservicios independientes + API Gateway
**Persistencia:** MySQL por microservicio
**Testing:** JUnit + H2 en memoria para pruebas automatizadas

---

## Estado Git previo a validación

La validación fue realizada desde la rama de integración:

```text
develop
```

Estado del repositorio:

```text
On branch develop
Your branch is up to date with 'origin/develop'.

nothing to commit, working tree clean
```

Últimos merges relevantes integrados en `develop`:

```text
d1e0af8 Merge pull request #30 from Nachovn12/feature/ms-logistica-envios
de120d4 Merge pull request #33 from Nachovn12/feature/s4-benjamin-flores-pedidos-ventas
```

---

## Comandos ejecutados — Tests

Se ejecutaron pruebas automatizadas por microservicio usando Maven:

```powershell
mvn -f ms-usuarios-identidad/pom.xml clean test
mvn -f ms-catalogo/pom.xml clean test
mvn -f ms-inventario-abastecimiento/pom.xml clean test
mvn -f ms-pedidos-ventas/pom.xml clean test
mvn -f ms-logistica-envios/pom.xml clean test
mvn -f ms-administracion-soporte/pom.xml clean test
mvn -f ms-reportes/pom.xml clean test
```

---

## Resultado de Tests

| Microservicio                  | Tests ejecutados | Resultado     |
| ------------------------------ | ---------------: | ------------- |
| MS Usuarios e Identidad        |                1 | BUILD SUCCESS |
| MS Catálogo                    |                1 | BUILD SUCCESS |
| MS Inventario y Abastecimiento |                5 | BUILD SUCCESS |
| MS Pedidos y Ventas            |               15 | BUILD SUCCESS |
| MS Logística de Envíos         |               12 | BUILD SUCCESS |
| MS Administración y Soporte    |                1 | BUILD SUCCESS |
| MS Reportes                    |                7 | BUILD SUCCESS |

---

## Comandos ejecutados — Package

Se ejecutó empaquetado Maven por microservicio:

```powershell
mvn -f ms-usuarios-identidad/pom.xml clean package
mvn -f ms-catalogo/pom.xml clean package
mvn -f ms-inventario-abastecimiento/pom.xml clean package
mvn -f ms-pedidos-ventas/pom.xml clean package
mvn -f ms-logistica-envios/pom.xml clean package
mvn -f ms-administracion-soporte/pom.xml clean package
mvn -f ms-reportes/pom.xml clean package
```

---

## Resultado de Package

| Microservicio                  | Resultado     |
| ------------------------------ | ------------- |
| MS Usuarios e Identidad        | BUILD SUCCESS |
| MS Catálogo                    | BUILD SUCCESS |
| MS Inventario y Abastecimiento | BUILD SUCCESS |
| MS Pedidos y Ventas            | BUILD SUCCESS |
| MS Logística de Envíos         | BUILD SUCCESS |
| MS Administración y Soporte    | BUILD SUCCESS |
| MS Reportes                    | BUILD SUCCESS |

---

## Observaciones técnicas

Durante la ejecución aparecieron advertencias no bloqueantes asociadas a:

- `H2Dialect does not need to be specified explicitly`.
- `spring.jpa.open-in-view is enabled by default`.
- Advertencias de Mockito/ByteBuddy por ejecución sobre JDK moderno.
- En MS Reportes, advertencia por uso de `MySQLDialect` en contexto de pruebas con H2.

Estas advertencias no impiden la compilación, ejecución de tests ni generación de paquetes.

---

## Conclusión

La rama `develop` del proyecto **EcoMarket SPA** fue validada correctamente.

Resultado general:

```text
7 microservicios con tests ejecutados correctamente.
7 microservicios empaquetados correctamente.
0 errores bloqueantes.
BUILD SUCCESS global por microservicio.
```

Por lo tanto, el backend queda técnicamente validado para la entrega EP2.

