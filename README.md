# Sistema Contable MIPYMES - "El Almacén El Planeador"

## Descripción del Proyecto

Sistema contable completo desarrollado para micro, pequeñas y medianas empresas (MIPYMES), basado en el caso de estudio "El Almacén El Planeador" propiedad del Sr. Horacio Porras. El sistema implementa un ciclo contable completo con módulos específicos para las necesidades de empresas comerciales.

## Características Principales

### ✅ Módulos Implementados

1. **Contabilidad General**
   - Catálogo de cuentas con saldos iniciales
   - Libro diario (asientos contables)
   - Validación de partida doble
   - Balance de comprobación
   - Estados financieros (Balance General y Estado de Resultados)

2. **Módulo de Inventario**
   - Gestión de productos
   - Control de existencias
   - Movimientos de inventario (entradas/salidas)
   - Reportes de stock mínimo
   - Valorización de inventarios

3. **Módulo de Bancos y Caja**
   - Gestión de cuentas bancarias
   - Registro de depósitos y cheques
   - Conciliación bancaria
   - Control de cheques en circulación

4. **Módulo de Clientes y Proveedores**
   - Gestión de clientes y proveedores
   - Cuentas por cobrar y pagar
   - Control de límites de crédito
   - Seguimiento de cuentas vencidas
   - Reportes de cartera

## Stack Tecnológico

### Backend
- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **Spring Security** (configurado básico)
- **H2 Database** (desarrollo)
- **PostgreSQL** (producción - pendiente migración)
- **Maven** (gestión de dependencias)

### Arquitectura
- **API REST** con endpoints documentados
- **Patrón MVC** (Model-View-Controller)
- **DTOs** para transferencia de datos
- **Repository Pattern** para acceso a datos
- **Service Layer** para lógica de negocio
- **Exception Handling** centralizado

## Estructura del Proyecto

```
src/main/java/edu/gt/umg/mipymes/contable/mipymes_contable_api/
├── controller/          # Controladores REST
│   ├── AsientoContableController.java
│   ├── BalanceComprobacionController.java
│   ├── CuentaController.java
│   ├── EstadosFinancierosController.java
│   ├── InventarioController.java
│   ├── BancosController.java
│   └── ClientesProveedoresController.java
├── service/             # Lógica de negocio
│   ├── AsientoContableService.java
│   ├── BalanceComprobacionService.java
│   ├── CuentaService.java
│   ├── EstadosFinancierosService.java
│   ├── InventarioService.java
│   ├── BancosService.java
│   └── ClientesProveedoresService.java
├── repository/          # Acceso a datos
│   ├── AsientoContableRepository.java
│   ├── CuentaRepository.java
│   ├── ProductoRepository.java
│   ├── CuentaBancariaRepository.java
│   ├── ClienteRepository.java
│   └── ProveedorRepository.java
├── entity/              # Entidades JPA
│   ├── AsientoContable.java
│   ├── MovimientoContable.java
│   ├── Cuenta.java
│   ├── Producto.java
│   ├── MovimientoInventario.java
│   ├── CuentaBancaria.java
│   ├── MovimientoBanco.java
│   ├── Cliente.java
│   ├── Proveedor.java
│   ├── CuentaPorCobrar.java
│   └── CuentaPorPagar.java
├── dto/                 # DTOs para API
├── exception/           # Manejo de excepciones
├── config/              # Configuraciones
│   ├── DataLoader.java  # Datos iniciales
│   └── WebConfig.java   # CORS
└── MipymesContableApiApplication.java
```

## Datos Iniciales

El sistema se inicializa con los saldos del caso "El Almacén El Planeador":

- **Efectivo en Caja:** Q38,100
- **Cuenta Bancaria:** Q68,000  
- **Inventario de Mercaderías:** Q78,000
- **Mobiliario y Equipo:** Q26,000
- **Proveedores:** Q37,000
- **Letras de Cambio por Pagar:** Q8,000
- **Capital:** Q165,100

## API Endpoints Principales

### Contabilidad
- `GET /api/cuentas` - Catálogo de cuentas
- `POST /api/asientos` - Crear asiento contable
- `PUT /api/asientos/{id}/contabilizar` - Contabilizar asiento
- `GET /api/balance-comprobacion` - Balance de comprobación
- `GET /api/estados-financieros/balance-general` - Balance general
- `GET /api/estados-financieros/estado-resultados` - Estado de resultados

### Inventario
- `GET /api/inventario/productos` - Listar productos
- `POST /api/inventario/productos` - Crear producto
- `POST /api/inventario/movimientos` - Registrar movimiento
- `GET /api/inventario/reporte` - Reporte de inventario

### Bancos
- `GET /api/bancos/cuentas` - Cuentas bancarias
- `POST /api/bancos/movimientos` - Registrar movimiento bancario
- `GET /api/bancos/resumen` - Resumen bancario

### Clientes y Proveedores
- `GET /api/clientes-proveedores/clientes` - Listar clientes
- `GET /api/clientes-proveedores/cuentas-cobrar/vencidas` - CxC vencidas
- `GET /api/clientes-proveedores/resumen` - Resumen general

## Instalación y Ejecución

### Prerrequisitos
- Java 21 o superior
- Maven 3.6+
- PostgreSQL 12+ (para producción)

### Pasos de Instalación

1. **Clonar el repositorio:**
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd mipymes-contable-api
   ```

2. **Compilar el proyecto:**
   ```bash
   ./mvnw clean compile
   ```

3. **Ejecutar en modo desarrollo:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acceder a la aplicación:**
   - API: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:contabledb`

## Configuración

### Desarrollo (H2)
```properties
spring.datasource.url=jdbc:h2:mem:contabledb
spring.jpa.hibernate.ddl-auto=create-drop
```

### Producción (PostgreSQL)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/contabledb
spring.datasource.username=postgres
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
```

## Testing con Postman

### Ejemplos de Requests

**Crear Producto:**
```json
POST /api/inventario/productos
{
  "codigo": "PROD001",
  "nombre": "Cuaderno Espiral",
  "precioCompra": 5.50,
  "precioVenta": 8.00,
  "existencia": 50,
  "stockMinimo": 10,
  "unidadMedida": "UNIDAD"
}
```

**Crear Asiento Contable:**
```json
POST /api/asientos
{
  "fecha": "2024-01-01",
  "descripcion": "Depósito bancario",
  "tipo": "OPERACION",
  "totalDebitos": 12000.00,
  "totalCreditos": 12000.00
}
```

## Estado del Proyecto

### ✅ Completado
- Backend completo con API REST
- Todos los módulos funcionales
- Validaciones implementadas
- Manejo de errores
- Configuración CORS
- Datos de prueba

### 🚧 Pendiente
- Frontend Angular
- Migración a PostgreSQL
- Despliegue en la nube
- Documentación de API (Swagger)
- Tests unitarios
- Autenticación JWT completa

## Equipo de Desarrollo

- **Sergio Daniel Hernandez Perez** - 2690-19-12232
- **Edgar Miguel Zapeta Perez** - 1490-18-4199
- **Luis Mike Christopher Mulul Castro** - 1490-18-3503
- **Brayan Emilio Ramírez Morán** - 1490-15-12167
- **Diego Andre Argueta Madrigales** - 1490-21-14005

## Universidad

**Universidad Mariano Gálvez de Guatemala**  
Curso: Desarrollo Web  
Proyecto: Sistema Contable para MIPYMES

---

## Notas Técnicas

- El sistema implementa completamente el caso "El Almacén El Planeador" del PDF proporcionado
- Todos los módulos están integrados y funcionando
- La API está diseñada para soportar un frontend Angular
- El código sigue las mejores prácticas de Spring Boot
- Preparado para escalabilidad y mantenimiento

**¡Sistema listo para demostración y próxima fase de desarrollo!**