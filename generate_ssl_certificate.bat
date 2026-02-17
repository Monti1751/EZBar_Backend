@echo off
REM ================================================
REM Script para generar certificado SSL para HTTPS
REM ================================================

echo.
echo Generando certificado SSL...
echo.

REM Directorio de salida
set OUTPUT_DIR=src\main\resources\ssl
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

REM Datos del certificado
set KEYSTORE_NAME=ezbar-keystore.p12
set KEYSTORE_PATH=%OUTPUT_DIR%\%KEYSTORE_NAME%
set KEYSTORE_PASSWORD=ezbar-dev-security-2024
set CERT_VALIDITY=365
set ALIAS=tomcat

REM Generar keystore
keytool -genkeypair ^
  -keyalg RSA ^
  -keysize 2048 ^
  -keystore "%KEYSTORE_PATH%" ^
  -keypass "%KEYSTORE_PASSWORD%" ^
  -storepass "%KEYSTORE_PASSWORD%" ^
  -alias "%ALIAS%" ^
  -storetype PKCS12 ^
  -validity %CERT_VALIDITY% ^
  -dname "CN=localhost, OU=EZBar, O=EZBar, L=Madrid, ST=Madrid, C=ES"

if %errorlevel% equ 0 (
    echo.
    echo v Certificado generado exitosamente!
    echo.
    echo UBICACION: %KEYSTORE_PATH%
    echo ALIAS: %ALIAS%
    echo CONTRASEÑA: %KEYSTORE_PASSWORD%
    echo VALIDEZ: %CERT_VALIDITY% dias
    echo.
    echo IMPORTANTE: Para usar este certificado:
    echo 1. Copia el archivo a src/main/resources/ssl/
    echo 2. En application-dev.yml (o prod):
    echo    server:
    echo      ssl:
    echo        key-store: classpath:ssl/%KEYSTORE_NAME%
    echo        key-store-password: %KEYSTORE_PASSWORD%
    echo        key-store-type: PKCS12
    echo        key-alias: %ALIAS%
    echo.
) else (
    echo.
    echo x ERROR: No se pudo generar el certificado
    echo.
)

pause
