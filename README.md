# 🍽️ EZBar Backend

> **Sistema de Gestión Integral para Restaurantes** - Backend escalable y modular con Java/Spring Boot y Node.js

[![Java 17+](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot 3.2](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![MariaDB 10.4+](https://img.shields.io/badge/MariaDB-10.4%2B-C0765F)](https://mariadb.org/)
[![Node.js](https://img.shields.io/badge/Node.js-18%2B-339933?logo=node.js&logoColor=white)](https://nodejs.org/)
[![Clean Code](https://img.shields.io/badge/Clean%20Code-✓-brightgreen)](#-arquitectura-limpia)

---

## 📑 Contenido

1. **[Resumen Ejecutivo](#-resumen-ejecutivo)**
2. **[Características](#-características)**
3. **[Arquitectura](#-arquitectura)**
4. **[Requisitos Previos](#-requisitos-previos)**
5. **[Instalación Rápida](#-instalación-rápida)**
6. **[Configuración](#-configuración)**
7. **[Endpoints Disponibles](#-endpoints-disponibles)**
8. **[Estructura del Proyecto](#-estructura-del-proyecto)**
9. **[Desarrollo Local](#-desarrollo-local)**
10. **[Base de Datos](#-base-de-datos)**
11. **[Deployments](#-deployments)**
12. **[Autores](#-autores)**

---

## 🎯 Resumen Ejecutivo

**EZBar Backend** es una solución empresarial para la gestión integral de restaurantes, bares y cafeterías. Proporciona una **arquitectura de tres capas** que garantiza escalabilidad, mantenibilidad y alta disponibilidad.

### ✨ Características Principales

- ✅ **Backend Java robusto** con Spring Boot 3.2 y Clean Code aplicado
- ✅ **API Node.js** optimizada con caché y compresión
- ✅ **Base de datos relacional** MariaDB con integridad referencial
- ✅ **Seguridad HTTPS** configurada automáticamente
- ✅ **Monitoreo de salud** del sistema (health checks)
- ✅ **Descubrimiento automático** de servicios (UDP)
- ✅ **Gestión completa** de productos, pedidos, mesas, empleados e inventario

---

## 🏗️ Arquitectura

### Diagrama de Componentes

```
┌─────────────────┐
│  Frontend       │
│  (Flutter)      │
└────────┬────────┘
         │ HTTP/HTTPS
         ▼
┌──────────────────────────────────────┐
│   API Node.js                        │
│  (Express + Caching + Monitoring)    │
│  Puerto: 3000 (HTTP) / 3443 (HTTPS) │
└────────────┬─────────────────────────┘
             │
             │ Internal API
             ▼
┌──────────────────────────────────────┐
│   Backend Java (Spring Boot 3.2)     │
│  (Controllers → Services → Repos)    │
│  Puerto: 8080 / HTTPS                │
└────────────┬─────────────────────────┘
             │ JDBC
             ▼
┌──────────────────────────────────────┐
│   MariaDB (Base de Datos)            │
│  Puerto: 3306                        │
└──────────────────────────────────────┘
```

### Capas de Aplicación

#### **1. Backend Java (Spring Boot 3.2.0)**
- **Controllers**: Manejadores de peticiones HTTP
- **Services**: Lógica de negocio
- **Repositories**: Acceso a datos con Spring Data JPA
- **Models**: Entidades JPA mapeadas a la BD
- **DTOs**: Transferencia de datos
- **Exception Handlers**: Gestión centralizada de errores

#### **2. API Node.js (Express)**
- **Middleware**: CORS, compresión, rate limiting, timeouts
- **Routes**: Enrutamiento de peticiones
- **Services**: Comunicación con Backend Java
- **Cache**: Caché en memoria con Caffeine
- **Monitoring**: Health checks y métricas de rendimiento
- **Security**: Headers de seguridad, HTTPS, validación

#### **3. Base de Datos (MariaDB)**
- **13 tablas relacionales** con integridad referencial
- **Cascading deletes** para mantener consistencia
- **Índices optimizados** para consultas frecuentes
- **Triggers y procedimientos** para automatización

---

## 📋 Requisitos Previos

### Software Requerido
- **Java Development Kit (JDK) 17** o superior
  - Descargar: https://www.oracle.com/java/technologies/downloads/
- **Apache Maven 3.8** o superior
  - Descargar: https://maven.apache.org/download.cgi
- **Node.js 18** o superior
  - Descargar: https://nodejs.org/
- **MariaDB 10.4** o superior
  - Descargar: https://mariadb.org/download/
- **Git** para control de versiones

### Herramientas Recomendadas
- **IntelliJ IDEA** (Community o Enterprise)
- **Visual Studio Code** con extensiones Java
- **MySQL Workbench** para gestionar BD
- **Postman** o **Insomnia** para testing de API

### Verificar Instalaciones
```bash
# Java
java -version

# Maven
mvn -version

# Node.js
node --version
npm --version

# MariaDB
mysql --version
```

---

## 🚀 Instalación Rápida

### 1️⃣ Clonar el Repositorio
```bash
git clone https://github.com/Monti1751/EZBar_Backend.git
cd EZBar_Backend
```

### 2️⃣ Configurar Base de Datos
```sql
-- En MariaDB/MySQL
CREATE DATABASE IF NOT EXISTS EZBarDB;

CREATE USER 'ezbar'@'localhost' IDENTIFIED BY 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON EZBarDB.* TO 'ezbar'@'localhost';
FLUSH PRIVILEGES;
```

O ejecutar el script SQL incluido:
```bash
mysql -u root -p < src/main/resources/Base\ de\ datos/EzBarDB.sql
```

### 3️⃣ Configurar Backend Java
Editar `src/main/resources/config.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/EZBarDB
spring.datasource.username=ezbar
spring.datasource.password=tu_contraseña_segura
server.port=8080
```

### 4️⃣ Compilar y Ejecutar Backend Java
```bash
# Compilación
mvn clean install

# Ejecutar
mvn spring-boot:run
# O generar JAR y ejecutar
mvn clean package
java -jar target/ezbar-backend-1.0.0.jar
```

✅ El backend estará disponible en: `http://localhost:8080/api`

### 5️⃣ Configurar y Ejecutar API Node.js
```bash
cd Api/api

# Instalar dependencias
npm install

# Configurar variables de entorno (crear .env)
cat > .env << EOF
NODE_ENV=development
PORT=3000
HTTPS_ENABLED=true
BACKEND_URL=http://localhost:8080/api
DB_HOST=localhost
DB_NAME=EZBarDB
DB_USER=ezbar
DB_PASSWORD=tu_contraseña_segura
CACHE_ENABLED=true
EOF

# Ejecutar
npm start
# O en desarrollo con nodemon
npm run dev
```

✅ La API Node.js estará disponible en: `http://localhost:3000/api`

---

## ⚙️ Configuración

### Backend Java

#### Variables de Entorno
```properties
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/EZBarDB
    username: ezbar
    password: ${DB_PASSWORD}
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MariaDBDialect
    hibernate:
      ddl-auto: update  # validate | update | create-drop

server:
  port: 8080
  servlet:
    context-path: /api
  compression:
    enabled: true
    min-response-size: 1024
```

#### Configuración de Seguridad
- CORS habilitado para todas las rutas
- Validación de entrada con Jakarta Validation
- Manejo de excepciones centralizado
- DTOs para encapsular datos

### API Node.js

#### Variables de Entorno (.env)
```env
# Servidor
NODE_ENV=development
PORT=3000
HTTPS_PORT=3443
HTTPS_ENABLED=true

# Backend Java
BACKEND_URL=http://localhost:8080/api

# Base de Datos
DB_HOST=localhost
DB_PORT=3306
DB_NAME=EZBarDB
DB_USER=ezbar
DB_PASSWORD=contraseña

# Cache
CACHE_ENABLED=true
CACHE_TTL=300

# Seguridad
RATE_LIMIT_WINDOW_MS=60000
RATE_LIMIT_MAX_REQUESTS=100
```

#### Certificados SSL/HTTPS
Los certificados se generan automáticamente en:
- `Api/api/certs/dev-cert.pem`
- `Api/api/certs/dev-key.pem`

Para generar nuevos certificados:
```bash
openssl req -nodes -new -x509 -keyout certs/dev-key.pem -out certs/dev-cert.pem -days 365
```

---

## 🔌 Endpoints Disponibles

### 📊 Endpoints Generales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/health` | Estado del sistema (BD, Backend, API) |
| `GET` | `/api/fix-db` | Reparar integridad referencial (CASCADE) |

### 🏷️ Productos
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/productos` | Listar todos los productos |
| `GET` | `/api/productos/{id}` | Obtener producto por ID |
| `POST` | `/api/productos` | Crear nuevo producto |
| `PUT` | `/api/productos/{id}` | Actualizar producto |
| `DELETE` | `/api/productos/{id}` | Eliminar producto |

### 🏷️ Categorías
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/categorias` | Listar categorías |
| `POST` | `/api/categorias` | Crear categoría |
| `PUT` | `/api/categorias/{id}` | Actualizar categoría |
| `DELETE` | `/api/categorias/{id}` | Eliminar categoría |

### 🧑‍💼 Empleados
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/empleados` | Listar empleados |
| `POST` | `/api/empleados` | Crear empleado |
| `PUT` | `/api/empleados/{id}` | Actualizar empleado |
| `DELETE` | `/api/empleados/{id}` | Eliminar empleado |

### 🪑 Mesas
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/mesas` | Listar mesas |
| `POST` | `/api/mesas` | Crear mesa |
| `PUT` | `/api/mesas/{id}` | Actualizar mesa (estado, ubicación) |
| `DELETE` | `/api/mesas/{id}` | Eliminar mesa |

### 📝 Pedidos
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/pedidos` | Listar pedidos |
| `POST` | `/api/pedidos` | Crear pedido |
| `PUT` | `/api/pedidos/{id}` | Actualizar estado pedido |
| `DELETE` | `/api/pedidos/{id}` | Cancelar pedido |

### 💳 Pagos
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/pagos` | Listar pagos |
| `POST` | `/api/pagos` | Registrar pago |
| `GET` | `/api/pagos/{id}` | Obtener detalle de pago |

### 👥 Usuarios
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/usuarios` | Listar usuarios |
| `POST` | `/api/usuarios` | Crear usuario |
| `PUT` | `/api/usuarios/{id}` | Actualizar usuario |

### 🔐 Autenticación
| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/auth/login` | Login de usuario |
| `POST` | `/api/auth/logout` | Logout de sesión |
| `POST` | `/api/auth/register` | Registrar nuevo usuario |

**📖 Documentación interactiva de API disponible en**: `http://localhost:3000/api/health`

---

## 📁 Estructura del Proyecto

```
EZBar_Backend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ezbar/
│   │   │       ├── Main.java                    # Punto de entrada
│   │   │       ├── controllers/                 # REST Controllers
│   │   │       ├── services/                    # Lógica de negocio
│   │   │       ├── repositories/                # Data Access Layer
│   │   │       ├── models/ (ClasesBD/)         # Entidades JPA
│   │   │       ├── config/                      # Configuración Spring
│   │   │       ├── exception/                   # Excepciones personalizadas
│   │   │       └── utils/                       # Clases utilitarias
│   │   │
│   │   └── resources/
│   │       ├── application.yml                  # Config Spring Boot
│   │       ├── application-dev.yml              # Config Desarrollo
│   │       ├── application-prod.yml             # Config Producción
│   │       ├── config.properties                # Propiedades
│   │       ├── logback-spring.xml              # Logging config
│   │       ├── checkstyle.xml                  # Code quality rules
│   │       └── Base\ de\ datos/
│   │           ├── EzBarDB.sql                 # Schema inicial
│   │           └── DatoDePrueba.sql            # Datos de prueba
│   │
│   └── test/
│       └── java/
│           └── com/ezbar/
│               └── controllers/                # Tests de API
│
├── Api/
│   └── api/                                    # API Node.js
│       ├── server.js                           # Punto de entrada
│       ├── package.json                        # Dependencias
│       ├── .env                                # Variables de entorno
│       └── src/
│           ├── app.js                          # Configuración Express
│           ├── logger.js                       # Sistema de logging
│           ├── config/
│           │   ├── constants.js                # Constantes globales
│           │   ├── database.js                 # Pool de conexión DB
│           │   ├── ssl.js                      # Certificados SSL
│           │   └── env.js                      # Parser de .env
│           ├── middleware/                     # Middlewares personalizados
│           ├── routes/                         # Definición de rutas
│           ├── services/                       # Lógica de negocio
│           ├── utils/                          # Utilidades
│           └── certs/                          # Certificados SSL
│
├── maven/                                      # Maven local (opcional)
│   └── apache-maven-3.9.6/
│
├── pom.xml                                     # POM (Maven)
│
├── README.md                                   # Este archivo
├── QUICK_START.md                              # Guía rápida
├── SPRING_BOOT_SETUP.md                        # Setup de Spring Boot
├── CLEAN_CODE_BACKEND.md                       # Estándares de código
├── IMPLEMENTACION_COMPLETADA.md                # Estado de implementación
└── INDEX.md                                    # Índice de documentación
```

---

## 💻 Desarrollo Local

### Ejecutar en Modo Debug (Java)

#### Opción 1: VS Code
```bash
# Abrir VS Code en el proyecto
code .

# Presionar F5 o ir a Run → Start Debugging
# Configuración está en .vscode/launch.json
```

#### Opción 2: IntelliJ IDEA
1. Abrir el proyecto
2. Click derecho en `Main.java` → Run 'Main.main()'
3. O usar Shift + F10

#### Opción 3: Maven
```bash
mvn spring-boot:run
```

### Ejecutar API Node.js en Modo Watch
```bash
cd Api/api
npm install -g nodemon  # Si no lo tienes
npm run dev
```

### Testing

#### Backend Java
```bash
# Ejecutar todos los tests
mvn test

# Tests específicos
mvn test -Dtest=ProductosControllerTest

# Coverage
mvn clean test jacoco:report
```

#### API Node.js
```bash
npm test
```

### Linting y Código

#### Backend Java
```bash
# Verificar código con Checkstyle
mvn checkstyle:check

# Formatear código
mvn spotless:apply
```

---

## 💾 Base de Datos

### Tablas Principales

| Tabla | Registros | Propósito |
|-------|-----------|----------|
| `PUESTOS` | ~10 | Roles de empleados (Admin, Mesero, Cocinero, Caja) |
| `EMPLEADOS` | ~100 | Información de empleados |
| `ZONAS` | ~5 | Áreas del restaurante (Terraza, Interior, Barra) |
| `MESAS` | ~50 | Mesas del local con coordenadas |
| `CATEGORIAS` | ~20 | Categorías de productos |
| `PRODUCTOS` | ~500 | Catálogo de productos con imágenes |
| `PEDIDOS` | ~5000 | Histórico de pedidos |
| `DETALLE_PEDIDOS` | ~10000 | Líneas de cada pedido |
| `PAGOS` | ~5000 | Registro de transacciones |
| `INVENTARIO` | ~1000 | Movimientos de stock |
| `USUARIOS` | ~100 | Cuentas de usuario |
| `PRODUCTO_INGREDIENTES` | ~1000 | Relación productos-ingredientes |

### Relaciones de Integridad
- ✅ Foreign Key CASCADE: Eliminación en cascada
- ✅ Unique Constraints: Unicidad de datos
- ✅ Check Constraints: Validación de valores
- ✅ Default Values: Valores por defecto automáticos

### Backup y Restore

```bash
# Backup completo
mysqldump -u ezbar -p EZBarDB > backup_$(date +%Y%m%d).sql

# Restore
mysql -u ezbar -p EZBarDB < backup_20260515.sql
```

---

## 🚢 Deployments

### Docker (Recomendado)

```dockerfile
# Dockerfile para Backend Java
FROM openjdk:17-jdk-slim
COPY target/ezbar-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
```

```bash
# Build y run
docker build -t ezbar-backend:1.0 .
docker run -p 8080:8080 \
  -e DB_HOST=mariadb \
  -e DB_USER=ezbar \
  -e DB_PASSWORD=secret \
  ezbar-backend:1.0
```

### Cloud Deployment

#### Azure App Service
```bash
az webapp create \
  --resource-group myGroup \
  --plan myServicePlan \
  --name ezbar-backend \
  --runtime "java|17"

az webapp deployment source config-zip \
  --resource-group myGroup \
  --name ezbar-backend \
  --src target/ezbar-backend.jar
```

#### AWS Elastic Beanstalk
```bash
eb init -p java-17 ezbar-backend
eb create ezbar-backend-env
eb deploy
```

---

## 📈 Monitoreo y Logs

### Health Check
```bash
curl http://localhost:3000/api/health

# Respuesta:
{
  "status": "OK",
  "environment": "development",
  "backend": { "status": "ONLINE", "url": "http://localhost:8080/api" },
  "database": { "status": "ONLINE", "host": "localhost", "name": "EZBarDB" },
  "cache": { "enabled": true, "hitRate": 0.85 },
  "memory": { "used": "245MB", "max": "512MB" }
}
```

### Logs
```bash
# Backend Java
tail -f logs/spring-boot.log

# API Node.js
tail -f logs/api.log

# Base de Datos
mysql -e "SHOW ENGINE INNODB STATUS;" > innodb_status.log
```

---

## 🔒 Seguridad

### Recomendaciones Implementadas

✅ **HTTPS/TLS** - Certificados generados automáticamente  
✅ **CORS** - Configurado por origen  
✅ **Rate Limiting** - Protección contra abuso  
✅ **Input Validation** - Validación de entrada en todas las capas  
✅ **SQL Injection Prevention** - Prepared Statements (ORM)  
✅ **CSRF Protection** - Headers de seguridad  
✅ **Password Hashing** - Bcrypt (no implementado aún)  
✅ **Logging de Auditoría** - Registro de cambios críticos  

### Checklist de Seguridad
- [ ] Cambiar contraseñas por defecto
- [ ] Configurar HTTPS con certificados válidos
- [ ] Implementar autenticación JWT
- [ ] Configurar firewall
- [ ] Encriptar datos sensibles en reposo
- [ ] Habilitar backups automáticos
- [ ] Monitores y alertas configuradas

---

## 📚 Documentación Adicional

- **[QUICK_START.md](QUICK_START.md)** - Guía de inicio rápido
- **[SPRING_BOOT_SETUP.md](SPRING_BOOT_SETUP.md)** - Configuración detallada de Spring Boot
- **[CLEAN_CODE_BACKEND.md](CLEAN_CODE_BACKEND.md)** - Estándares y patrones de código
- **[INDEX.md](INDEX.md)** - Índice completo de documentación

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el repositorio
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

---

## 📄 Licencia

Este proyecto está bajo licencia **MIT**. Ver [LICENSE](LICENSE) para detalles.

---

## 👥 Autores

Este módulo Backend forma parte del proyecto completo **EZBar**, desarrollado por:

| Autor | GitHub | Rol |
|-------|--------|-----|
| Miguel Tomás | [@ismigue23](https://github.com/ismigue23) | Backend Lead |
| Francisco Montesinos | [@Monti1751](https://github.com/Monti1751) | Architecture & DevOps |
| Miguel Jiménez | [@MiguelJimenezSerrano](https://github.com/MiguelJimenezSerrano) | Full Stack |
| Miguel Duque | [@El-Mig](https://github.com/El-Mig) | QA & Testing |

### Versión
- **Versión Actual**: 1.0.0
- **Estado**: Alpha (En desarrollo activo)
- **Última Actualización**: 15 de mayo de 2026

---

## 📞 Soporte

Para reportar problemas, sugerencias o preguntas:

- **Issues**: [GitHub Issues](https://github.com/Monti1751/EZBar_Backend/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Monti1751/EZBar_Backend/discussions)
- **Email**: contacto@ezbar.local

---

<div align="center">

**Desarrollado con ❤️ para la hostelería moderna**

[![GitHub Stars](https://img.shields.io/github/stars/Monti1751/EZBar_Backend?style=social)](https://github.com/Monti1751/EZBar_Backend)
[![GitHub Forks](https://img.shields.io/github/forks/Monti1751/EZBar_Backend?style=social)](https://github.com/Monti1751/EZBar_Backend)

</div>
