# Changelog
## Versión 0.8.0 -(23-2-2026)

### Añadido
* Conexión con la pantalla de usuarios de frontend

### Corregido
* Eliminación en cascada de las mesas de las zonas borradas y de los platos de las secciones borradas

## Versión 0.7.0 -(9-2-2026)

### Añadido
* Gestión de images para almacenar en la base de datos
* Tests unitarios

### Corregido
* Restructuración de la base de datos para que pueda almacenar imagenes
* Corrección de logs en diferentes niveles(información, debug y error)

## Versión 0.5.0 -(15-12-2025)

### Añadido
* Se ha pasado el backend a Maven
* Se ha creado en la ruta src/main/java/Controladores/ el archivo SetupController.java
* Creación de la API en Node.js 
* Creación de los endpoints para guardar la información:
  * Se guardan las zonas creadas y se eliminan de forma correcta
  * Se guardan las mesas creadas y se eliminan de forma correcta de cada zona
  * Se guardan las secciones creadas y se eliminan de forma correcta de la carta
* Se ha creado en la ruta src/main/java/com/ezbar/ el archivo Main.java para ver si se ha establecido la conexión con la API 

### Corregido
* Restructuración del backend segun un proyecto Maven
* Modificación de todos los archivos .java según Hibernate

## Versión 0.2.0 -(30-11-2025)

## Añadido  
- Se han añadido clases para la BD
- Se han añadido controladores para dichas clases
- Se ha una clase para conectar con la base de datos
