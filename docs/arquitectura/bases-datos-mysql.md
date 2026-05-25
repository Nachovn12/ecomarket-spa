# Bases de Datos MySQL — EcoMarket SPA

## Proyecto

| Campo | Detalle |
| --- | --- |
| Proyecto | EcoMarket SPA |
| Asignatura | Desarrollo Full Stack I — DSY1103 |
| Sección | 003D |
| Entrega | EP2, Entrega de Encargo grupal Parte 1 |
| Arquitectura | Microservicios independientes con API Gateway |
| Motor de base de datos | MySQL |

---

## 1. Objetivo

Este documento describe la estrategia de persistencia utilizada en el backend de **EcoMarket SPA**, basada en el principio de **base de datos por microservicio**.

Cada microservicio posee su propia base de datos relacional MySQL, lo que permite mantener independencia entre dominios, reducir el acoplamiento y facilitar la evolución del sistema dentro de una arquitectura distribuida.

---

## 2. Estrategia de persistencia

La solución aplica el patrón arquitectónico:

```text
Database per Service
```

Esto significa que cada microservicio administra sus propias entidades, repositorios y reglas de persistencia, sin acceder directamente a las tablas de otros microservicios.

La comunicación entre microservicios debe realizarse mediante servicios REST y no mediante consultas directas entre bases de datos.

---

## 3. Bases de datos por microservicio

| Microservicio                  | Base de datos MySQL       | Responsabilidad                                                   |
| ------------------------------ | ------------------------- | ----------------------------------------------------------------- |
| MS Usuarios e Identidad        | `bd_usuarios`   | Login, registro, roles, permisos y usuarios internos              |
| MS Catálogo                    | `bd_catalogo`   | Productos, categorías, reseñas, búsqueda y atributos ecológicos   |
| MS Inventario y Abastecimiento | `bd_inventario` | Stock, reservas, movimientos, alertas y reabastecimiento          |
| MS Pedidos y Ventas            | `bd_ventas`     | Carrito, pedidos, ventas, pagos, facturas, cupones y devoluciones |
| MS Logística de Envíos         | `bd_logistica`  | Envíos, rutas, proveedores, seguimiento y reabastecimiento        |
| MS Administración y Soporte    | `bd_admin`      | Tiendas, tickets, alertas, métricas y respaldos                   |
| MS Reportes                    | `bd_reportes`   | Reportes, KPIs, exportaciones y auditoría                         |
| API Gateway                    | No aplica                 | Enrutamiento REST y punto único de entrada                        |

---

## 4. Script SQL para crear las bases de datos

Ejecutar el siguiente script en MySQL Workbench, phpMyAdmin o consola MySQL:

```sql
CREATE DATABASE IF NOT EXISTS bd_usuarios
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_catalogo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_inventario
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_ventas
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_logistica
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_admin
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_reportes
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

---

## 5. Configuración general de conexión

Cada microservicio utiliza un archivo de configuración ubicado en:

```text
src/main/resources/application.properties
```

La configuración base esperada para MySQL es:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/NOMBRE_BD
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

La propiedad `spring.datasource.password` puede variar según la configuración local de MySQL o XAMPP de cada integrante.

---

## 6. Configuración por microservicio

### MS Usuarios e Identidad

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_usuarios
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### MS Catálogo

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_catalogo
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### MS Inventario y Abastecimiento

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_inventario
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### MS Pedidos y Ventas

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_ventas
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### MS Logística de Envíos

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_logistica
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### MS Administración y Soporte

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_admin
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### MS Reportes

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_reportes
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

---

## 7. Persistencia con JPA/Hibernate

Los microservicios implementan persistencia mediante:

```text
Spring Data JPA
Hibernate
Repositorios JpaRepository
Entidades anotadas con @Entity
Relaciones entre entidades cuando corresponde
Validaciones con Bean Validation
```

Ejemplo de entidad:

```java
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    private String sku;
    private String nombre;
    private Double precio;
}
```

Ejemplo de repositorio:

```java
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
```

---

## 8. Uso de H2 en pruebas automatizadas

Durante las pruebas automatizadas se utiliza H2 en memoria para permitir una ejecución rápida, controlada e independiente del entorno local.

Esta configuración se utiliza solo para testing y no reemplaza la persistencia principal del proyecto.

Archivo utilizado para pruebas:

```text
src/test/resources/application.properties
```

Ejemplo de configuración para pruebas:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
```

La persistencia principal del proyecto corresponde a MySQL.

---

## 9. Separación de responsabilidades

Cada microservicio mantiene su propia base de datos y no debe acceder directamente a las tablas de otro microservicio.

Ejemplos:

- MS Pedidos y Ventas no consulta directamente tablas de inventario.
- MS Reportes consume información mediante endpoints o flujos definidos, no mediante acceso directo a bases externas.
- MS Logística de Envíos gestiona sus propios envíos, rutas, proveedores y seguimientos.
- MS Catálogo administra productos, categorías y reseñas desde su propia base de datos.
- MS Administración y Soporte mantiene de forma independiente tiendas, tickets, alertas y métricas del sistema.

---

## 10. Comunicación entre microservicios

La comunicación entre microservicios se realiza mediante servicios REST.

Flujos principales:

| Origen              | Destino                        | Propósito                                    |
| ------------------- | ------------------------------ | -------------------------------------------- |
| MS Pedidos y Ventas | MS Inventario y Abastecimiento | Consultar y reservar stock                   |
| MS Pedidos y Ventas | MS Logística de Envíos         | Solicitar despacho de pedidos                |
| MS Reportes         | MS Pedidos y Ventas            | Obtener información de ventas                |
| MS Reportes         | MS Inventario y Abastecimiento | Obtener información de stock                 |
| MS Reportes         | MS Administración y Soporte    | Obtener información de tiendas y rendimiento |

La integración entre servicios debe realizarse mediante endpoints REST, manteniendo la independencia de las bases de datos.

---

## 11. Consideraciones para ejecución local

Antes de ejecutar los microservicios, se debe verificar lo siguiente:

```text
MySQL debe estar iniciado.
Las bases de datos deben estar creadas.
Cada application.properties debe apuntar a su base de datos correspondiente.
Las credenciales de conexión deben ser correctas.
El puerto configurado por cada microservicio debe estar disponible.
```

Si se utiliza XAMPP, el servicio obligatorio es:

```text
MySQL
```

Apache es opcional y solo se requiere si se utiliza phpMyAdmin para administrar las bases de datos.

---

## 12. Puertos sugeridos por microservicio

| Componente | Puerto configurado |
|---|---:|
| API Gateway | 8081 |
| MS Usuarios e Identidad | 8083 |
| MS Catálogo | 8084 |
| MS Inventario y Abastecimiento | 8085 |
| MS Pedidos y Ventas | 8086 |
| MS Logística de Envíos | 8087 |
| MS Administración y Soporte | 8088 |
| MS Reportes | 8089 |

Los puertos pueden variar según la configuración final del archivo `application.properties` de cada microservicio.

---

## 13. Beneficios de la estrategia

La separación de bases de datos permite:

- Bajo acoplamiento entre microservicios.
- Mayor independencia de despliegue.
- Mejor mantenibilidad del código.
- Separación clara por dominio.
- Mayor alineación con arquitectura distribuida.
- Mejor organización de entidades y repositorios.
- Reducción del punto único de fallo del sistema monolítico original.

---

## 14. Conclusión

EcoMarket SPA utiliza una arquitectura de microservicios con persistencia distribuida basada en MySQL.

Cada microservicio cuenta con su propia base de datos, entidades JPA, repositorios Spring Data y configuración independiente, cumpliendo con los principios de arquitectura distribuida solicitados en la evaluación EP2 de Desarrollo Full Stack I.

