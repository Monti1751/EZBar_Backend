# Changelog
## -26-01-2026

### Añadido
* Se ha añadido un sistema de logs
* Se ha añadido un .gitignore con su respectivo .env
* Se ha añadido un start_servers.bat oara iniciar conjuntamente la API y el backend

### Corregido
* Ya se conecta la apk con el server
* Restructuración de las carpetas y clases .java

## -15-12-2025

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

**30-11-2025**

## Añadido  
- Se han añadido clases para la BD
- Se han añadido controladores para dichas clases
- Se ha una clase para conectar con la base de datos
