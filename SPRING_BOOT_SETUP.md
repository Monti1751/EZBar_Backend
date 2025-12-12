# EZBar Backend - Spring Boot

## Descripción
Backend en Spring Boot para el Sistema de Gestión de Restaurante EZBar.

## Requisitos
- Java 21+
- Maven 3.8+
- MariaDB 10.4+

## Compilación

### Opción 1: Maven desde terminal
```bash
cd C:\Users\framonsil\GitHub\EZBar_Backend
mvn clean install
```

### Opción 2: Compilación automática en VS Code
El proyecto debería compilarse automáticamente con la extensión de Red Hat Java.

## Ejecución

### Opción 1: Ejecutar con Maven
```bash
mvn spring-boot:run
```

### Opción 2: Ejecutar el JAR compilado
```bash
java -jar target/ezbar-backend-1.0.0.jar
```

### Opción 3: Desde VS Code (Debug o Run)
Usa las configuraciones ya disponibles en "Run: Main" o "Debug: Main"

## URLs Disponibles

El servidor estará disponible en: `http://localhost:8080`

### Endpoints de API

#### Salud del servidor
- `GET /api/health` - Verifica el estado del servidor y la base de datos

#### Productos
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `POST /api/productos` - Crear nuevo producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto

#### Categorías
- `GET /api/categorias` - Listar todas las categorías
- `GET /api/categorias/{id}` - Obtener categoría por ID
- `POST /api/categorias` - Crear nueva categoría
- `PUT /api/categorias/{id}` - Actualizar categoría
- `DELETE /api/categorias/{id}` - Eliminar categoría

#### Empleados
- `GET /api/empleados` - Listar todos los empleados
- `GET /api/empleados/{id}` - Obtener empleado por ID
- `POST /api/empleados` - Crear nuevo empleado
- `PUT /api/empleados/{id}` - Actualizar empleado
- `DELETE /api/empleados/{id}` - Eliminar empleado

#### Mesas
- `GET /api/mesas` - Listar todas las mesas
- `GET /api/mesas/{id}` - Obtener mesa por ID
- `POST /api/mesas` - Crear nueva mesa
- `PUT /api/mesas/{id}` - Actualizar mesa
- `DELETE /api/mesas/{id}` - Eliminar mesa

#### Pedidos
- `GET /api/pedidos` - Listar todos los pedidos
- `GET /api/pedidos/{id}` - Obtener pedido por ID
- `POST /api/pedidos` - Crear nuevo pedido
- `PUT /api/pedidos/{id}` - Actualizar pedido
- `DELETE /api/pedidos/{id}` - Eliminar pedido

#### Detalles de Pedidos
- `GET /api/detallepedidos` - Listar todos los detalles
- `GET /api/detallepedidos/{id}` - Obtener detalle por ID
- `POST /api/detallepedidos` - Crear nuevo detalle
- `PUT /api/detallepedidos/{id}` - Actualizar detalle
- `DELETE /api/detallepedidos/{id}` - Eliminar detalle

#### Pagos
- `GET /api/pagos` - Listar todos los pagos
- `GET /api/pagos/{id}` - Obtener pago por ID
- `POST /api/pagos` - Crear nuevo pago
- `PUT /api/pagos/{id}` - Actualizar pago
- `DELETE /api/pagos/{id}` - Eliminar pago

#### Inventario
- `GET /api/inventario` - Listar todo el inventario
- `GET /api/inventario/{id}` - Obtener elemento de inventario por ID
- `POST /api/inventario` - Crear nuevo elemento
- `PUT /api/inventario/{id}` - Actualizar elemento
- `DELETE /api/inventario/{id}` - Eliminar elemento

#### Producto Ingredientes
- `GET /api/productoingredientes` - Listar todas las relaciones
- `GET /api/productoingredientes/{id}` - Obtener relación por ID
- `POST /api/productoingredientes` - Crear nueva relación
- `PUT /api/productoingredientes/{id}` - Actualizar relación
- `DELETE /api/productoingredientes/{id}` - Eliminar relación

#### Puestos
- `GET /api/puestos` - Listar todos los puestos
- `GET /api/puestos/{id}` - Obtener puesto por ID
- `POST /api/puestos` - Crear nuevo puesto
- `PUT /api/puestos/{id}` - Actualizar puesto
- `DELETE /api/puestos/{id}` - Eliminar puesto

## Conexión con Node.js

El backend en Node.js (`probarnode/server.js`) se conectará automáticamente a:
```
http://localhost:8080/api/health
```

Si la configuración está correcta, debería detectar el servidor automáticamente.

## Configuración

Los parámetros se encuentran en:
- `src/main/resources/config.properties` (propiedades)
- `src/main/resources/application.yml` (YAML)

### Variables importantes:
- `server.port=8080` - Puerto de escucha
- `spring.datasource.url=jdbc:mariadb://localhost:3306/ezbar` - Conexión a BD
- `spring.datasource.username=root` - Usuario BD
- `spring.datasource.password=` - Contraseña BD

## Problemas Comunes

### "Conexión rechazada en puerto 8080"
1. Asegúrate de que MariaDB está corriendo
2. Verifica que el puerto 8080 está libre: `netstat -ano | findstr :8080`
3. Reinstala Maven: `mvn clean install -U`

### "Base de datos no encontrada"
1. Crea la base de datos: `CREATE DATABASE ezbar;`
2. Verifica las credenciales en `application.yml`

### "Maven no reconocido"
1. Instala Maven desde https://maven.apache.org/download.cgi
2. Agrega Maven al PATH de Windows
3. Reinicia VS Code
