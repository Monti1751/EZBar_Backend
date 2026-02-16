# Configuración de HTTPS - EZBar Backend

## Generar Certificado SSL

Para desarrollo, ejecuta:
```bash
cd d:\GitHub\EZBar_Backend
generate_ssl_certificate.bat
```

Esto generará un certificado en: `src/main/resources/ssl/ezbar-keystore.p12`

## Configurar HTTPS en Desarrollo

El archivo `application-dev.yml` tiene HTTPS deshabilitado por defecto.
Para habilitarlo, agrega en `application-dev.yml`:

```yaml
server:
  ssl:
    enabled: true
    key-store: classpath:ssl/ezbar-keystore.p12
    key-store-password: ezbar-dev-security-2024
    key-store-type: PKCS12
    key-alias: tomcat
```

## Producción

En `application-prod.yml`, HTTPS está habilitado por defecto.
Proporciona las variables de entorno:
- `SSL_KEYSTORE_PATH`: Ruta al archivo .p12
- `SSL_KEYSTORE_PASSWORD`: Contraseña del keystore

## Endpoints Disponibles

### Caché
- `GET /api/cache/stats/{cacheName}` - Estadísticas del caché
- `DELETE /api/cache/{cacheName}` - Limpiar caché específico
- `DELETE /api/cache/all` - Limpiar todos los cachés

### Memoria
- `GET /api/memory/info` - Información de memoria
- `GET /api/memory/health` - Estado de salud
- `GET /api/memory/system-info` - Información del sistema
- `POST /api/memory/gc` - Ejecutar recolección de basura

### Actuator (solo en DEV)
- `GET /actuator/health` - Estado de salud
- `GET /actuator/metrics` - Métricas
- `GET /actuator/env` - Variables de entorno
