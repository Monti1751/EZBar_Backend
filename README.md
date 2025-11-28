# EZBar Backend

## Índice
1. [Arquitectura del Backend](#1-arquitectura-del-backend)  
2. [Instalación y Configuración](#2-instalación-y-configuración)  
3. [Funcionamiento](#3-funcionamiento)  
4. [Estado del Proyecto](#4-estado-del-proyecto)  
5. [Autores](#5-autores)  

---

## 1. Arquitectura del Backend

El **Backend** de EZBar está desarrollado en **Java** y forma parte de una arquitectura en la que intervienen tres componentes principales:

- **Backend Java:** Encargado de la lógica de negocio y la comunicación con la base de datos **MariaDB**.  
- **API Node.js:** Capa intermedia que expone los endpoints que consume el Frontend.  
- **Base de Datos MariaDB:** Almacena información de productos, mesas, pedidos y usuarios.

El Backend Java actúa como núcleo del sistema, gestionando las operaciones internas, mientras que la API en **Node.js** sirve de puente entre la aplicación móvil y este backend, favoreciendo una arquitectura modular y escalable.

## 2. Instalación y Configuración

### Requisitos Previos

Antes de ejecutar el Backend, asegúrate de tener instalado:

- **Java 17 o superior**
- **Maven**
- **MariaDB 10.x o superior**
- IDE recomendado: IntelliJ IDEA o Eclipse

---

### Clonar el Repositorio

```bash
git clone https://github.com/Monti1751/EZBar_Backend.git
cd EZBar_Backend
```
### Configuración de la Base de Datos
```
CREATE DATABASE ezbar;

CREATE USER 'ezbar'@'localhost' IDENTIFIED BY 'tu_contraseña';
GRANT ALL PRIVILEGES ON ezbar.* TO 'ezbar'@'localhost';
FLUSH PRIVILEGES;

```
### Configuración del Archivo de Propiedades

En el archivo config.properties (o equivalente en tu proyecto), configura la conexión JDBC:

```
db.url=jdbc:mariadb://localhost:3306/ezbar
db.user=ezbar
db.password=tu_contraseña

```
### Configuración del Archivo de Propiedades

Compilación y Ejecución

```
mvn clean package
```
Ejecutar el servidor:

```
java -jar target/EZBar_Backend.jar

```
### El Backend quedará disponible en:
```
http://localhost:8080

```
## 3. Funcionamiento

El Backend de EZBar se encarga de gestionar toda la lógica de negocio relacionada con la hostelería, incluyendo productos, zonas, mesas y pedidos.

### Flujo de Comunicación

1. El **Frontend (Flutter)** realiza peticiones a la **API Node.js**.
2. La API interpreta las peticiones y las redirige al **Backend Java**, que ejecuta la lógica correspondiente.
3. El Backend accede a la **base de datos MariaDB** para leer o modificar información.
4. La respuesta se devuelve al Frontend, actualizando el estado de la aplicación.

---

### Funcionalidades Principales del Backend

- **Gestión de productos:**  
  Alta, baja y modificación de productos disponibles en el local.

- **Gestión de zonas y mesas:**  
  Organización del espacio del establecimiento en zonas (Terraza, Barra, Comedor) y control del estado de cada mesa.

- **Gestión de pedidos:**  
  Registro de pedidos, actualización de tickets, cierre de cuentas y control de flujo entre camareros y cocina/barra.

- **Persistencia de datos:**  
  Toda la información se almacena en **MariaDB**, garantizando consistencia y disponibilidad.

---

### Endpoints Internos

Aunque la API pública está gestionada por Node.js, el Backend Java expone servicios internos que permiten:

- Procesar operaciones complejas.  
- Validar reglas de negocio.  
- Mantener la integridad de los datos.

> Nota: Estos servicios no están expuestos directamente al cliente, sino que son consumidos por la API intermedia.

## 4. Estado del Proyecto

El Backend de EZBar se encuentra actualmente en **versión Alpha**, lo que significa que está en una fase temprana de desarrollo y validación.  
Durante esta etapa se están verificando las funcionalidades principales y la correcta comunicación entre los distintos componentes del sistema.

### Funcionalidades en Estado Alpha

- Conexión estable con la base de datos **MariaDB**.  
- Procesamiento de la lógica de negocio interna (productos, mesas, zonas, pedidos).  
- Comunicación funcional con la **API intermedia en Node.js**.  
- Respuestas consistentes a las operaciones solicitadas por el Frontend.

### Próximos Objetivos

- Optimización del rendimiento del servidor Java.  
- Ampliación de endpoints internos para nuevas funcionalidades.  
- Refactorización y limpieza de código.  
- Preparación para una futura **versión Beta**, más estable y completa.

> El proyecto está en evolución activa, por lo que se esperan cambios frecuentes en la estructura y funcionamiento del Backend.

## 5. Autores

Este módulo Backend forma parte del proyecto completo **EZBar**, desarrollado por:

- **Miguel Tomás**    
  - GitHub: [ismigue23](https://github.com/ismigue23)

- **Francisco Montesinos**   
  - GitHub: [Monti1751](https://github.com/Monti1751)

- **Miguel Jiménez**  
  - GitHub: [MiguelJimenezSerrano](https://github.com/MiguelJimenezSerrano)

- **Miguel Duque**  
  - GitHub: [El-Mig](https://github.com/El-Mig)
    
> Para más información sobre el proyecto completo, consulta el README principal del repositorio.
