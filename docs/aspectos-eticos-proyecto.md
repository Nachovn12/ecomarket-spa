# Informe de Aspectos Éticos del Proyecto - EcoMarket SPA
## Sprint 3 — Concienciación y Buenas Prácticas

En el marco del desarrollo del sistema EcoMarket SPA, se han considerado y resguardado los siguientes pilares éticos fundamentales en los microservicios de Catálogo y Logística:

### 1. Privacidad de Datos y Confidencialidad (Ley 19.628 Chile)
- **Logística de Envíos:** Las direcciones físicas de despacho, nombres de clientes y números telefónicos son tratados bajo el principio de necesidad mínima de acceso. Los transportistas solo visualizan la información indispensable para la entrega.

### 2. Transparencia con Proveedores Ecológicos
- **Soft Delete:** Para proteger la integridad histórica de los despachos y contratos, los proveedores no se eliminan físicamente (evitando pérdida de trazabilidad de pagos), sino que pasan a un estado inactivo.

### 3. Integridad en Reseñas de Productos
- **Protección al Consumidor:** El sistema de calificaciones del ms-catalogo asegura que comentarios críticos legítimos sobre la huella de carbono o calidad del producto no puedan ser manipulados ni borrados arbitrariamente por los administradores, garantizando transparencia al consumidor.
