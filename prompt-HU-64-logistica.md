# Prompt de Implementación - HU-64

## Contexto General del Proyecto

- **Proyecto:** EcoMarket SPA (monorepo: Nachovn12/ecomarket-spa)
- **Sprint:** S5 - Pruebas Unitarias EP3
- **Rúbrica:** Rubrica_EP3_EcoMarket.md (indicadores IE 3.1.1, 3.1.2, 3.1.3)
- **Patrón de referencia del profesor:** https://github.com/profealejandroduoc/unitarias25
- **Referencia interna del equipo:** ms-pedidos-ventas/ (111 tests, JaCoCo >80%)
- **HU padre:** HU-60 EP-08 Pruebas Unitarias
- **Asignado en Jira:** Benjamin Espinoza
- **Reviewer en Jira:** Ignacio Valeria (Nachovn12)

---

## HU-64: Pruebas MS Logistica de Envios

### Story Points: 5 (8h estimadas)

### Descripción
Como desarrollador del MS Logistica de Envios, quiero contar con pruebas unitarias de `LogisticaService` (único service del MS), los 3 controllers (`EnvioController`, `ProveedorController`, `RutaEntregaController`) y la clase utilitaria `EtaCalculator` para validar la asignación de envíos a proveedores, el cálculo de rutas óptimas y el seguimiento de paquetes.

### Dominio EcoMarket SPA
El MS Logística recibe los pedidos confirmados del MS Pedidos mediante `PedidosClientService` y los asigna a un transportista (proveedor) con una ruta optimizada calculada por `EtaCalculator`. El cliente consulta el estado del envío mediante el endpoint de seguimiento.
**Reglas Críticas:** 
1. Asignar envío al proveedor con menor tiempo estimado.
2. Calcular la ruta óptima minimizando el tiempo total y devolviendo la distancia en Km (`EtaCalculator`).

---

## Plan de Trabajo (orden OBLIGATORIO)

**La IA y el desarrollador DEBEN seguir este orden exacto para evitar problemas en el PR.**

### Paso 0 - Sincronización y Rama Exclusiva (Git)

**OBLIGATORIO:** Siempre se debe traer la última actualización desde `develop` antes de empezar.

```bash
git checkout develop
git pull origin develop
git checkout -b feature/ep3-tests-ms-logistica-envios
```

### Paso 1 - Configurar JaCoCo en `pom.xml`

Agregar el plugin al `pom.xml` de `ms-logistica-envios`. **DEBE** incluir las exclusiones:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.13</version>
    <executions>
        <execution>
            <id>prepare-agent</id>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
    <configuration>
        <excludes>
            <exclude>**/dto/**</exclude>
            <exclude>**/model/**</exclude>
            <exclude>**/exception/**</exclude>
            <exclude>**/config/**</exclude>
            <exclude>**/util/EtaCalculator.class</exclude> <!-- Excluir solo si se testea 100% puro y falla en coverage mixto, pero idealmente NO excluirlo -->
        </excludes>
    </configuration>
</plugin>
```
*(Nota: No excluyas `EtaCalculator`, asegúrate de testearlo para ganar coverage).*

### Paso 2 - Crear ServiceTest y EtaCalculatorTest (Clase plana, SIN @Nested)

Estructura:
- `LogisticaServiceTest.java`: **Clase plana** con `@ExtendWith(MockitoExtension.class)`. Inyectar repositorios con `@Mock`.
- `EtaCalculatorTest.java`: Test puro de JUnit (sin dependencias Spring) que valide el cálculo de tiempo y km.

**Tests mínimos:**
- Asignación al proveedor de menor tiempo.
- Ruta óptima con `EtaCalculator` (1 parada = 0km, vacía = Excepción, múltiples = orden óptimo).
- Alta de envío y tracking.

### Paso 3 - Crear los ControllerTests con @WebMvcTest

Estructura de `EnvioControllerTest`, `ProveedorControllerTest`, `RutaEntregaControllerTest`:
- Usar `@WebMvcTest(Controller.class)`
- Usar `@MockitoBean LogisticaService`
- **Validar JSON DTO Directo:** Sin `_links` ni `_embedded`.



### Paso 4 - Validación Final con JaCoCo

```bash
.\ms-logistica-envios\mvnw.cmd -f ms-logistica-envios/pom.xml clean test jacoco:report
```

**OJO:** Si el porcentaje de JaCoCo en el HTML es menor a 80%, la IA **NO DEBE** avanzar. Debe iterar y agregar más pruebas hasta superar el 80% real.

### Paso 5 - Push, Pull Request y Documentación Jira (Cierre)

1. Subir a la rama:
```bash
git add .
git commit -m "test(ms-logistica-envios): pruebas unitarias con JaCoCo > 80%"
git push origin feature/ep3-tests-ms-logistica-envios
```

2. Crear Pull Request hacia `develop`.
3. **Reviewer obligatorio:** Ignacio Valeria (`Nachovn12`).
4. **Jira:** Cambiar HU a "Finalizada".
5. **Documentación:** Dejar un comentario en Jira con los resultados de los tests y el % oficial de JaCoCo.

---

## Criterios de Aceptación (formato Gherkin)

**AC-1: ServiceTest con Mockito**
- **Cuando** se ejecuta LogisticaServiceTest
- **Entonces** usa `@ExtendWith` y todos pasan.

**AC-2: ControllerTests con `@WebMvcTest`**
- **Cuando** se ejecutan los 3 controller tests
- **Entonces** usan `@WebMvcTest` y validan JSON directo.

**AC-3: Asignacion a proveedor mas rápido**
- **Dado** 2 proveedores (24h y 12h)
- **Cuando** `LogisticaService` asigna
- **Entonces** selecciona el de 12h.

**AC-4: Ruta Optima (EtaCalculator)**
- **Dado** paradas de entrega
- **Cuando** `EtaCalculator` resuelve
- **Entonces** minimiza el tiempo, retorna Km y lanza error si está vacía.

**DoD:**
- PR asignado a `Nachovn12`.
- JaCoCo > 80%.
- Jira finalizado y documentado.
