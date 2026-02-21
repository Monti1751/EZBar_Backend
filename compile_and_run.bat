@echo off
setlocal enabledelayedexpansion
echo.
echo ================================================
echo  EZBar - Iniciando Servicios
echo ================================================
echo.

REM Obtener el directorio del script
set "PROJECT_DIR=%~dp0"
echo Directorio del proyecto: "%PROJECT_DIR%"
cd /d "%PROJECT_DIR%"

REM Configurar Maven en el PATH
set "MAVEN_HOME=%PROJECT_DIR%maven\apache-maven-3.9.6"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

REM Configuración JVM para memoria
set "MAVEN_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

REM Solicitar modo de ejecución
echo.
echo ¿Que modo deseas ejecutar?
echo [1] Desarrollo (DEBUG - con logs detallados)
echo [2] Produccion (PROD - optimizado)
echo.
set /p MODE="Selecciona opcion [1-2] (default: 1): "
if "%MODE%"=="" set MODE=1

if "%MODE%"=="1" (
    set "SPRING_PROFILE=dev"
    echo Modo: DESARROLLO
) else if "%MODE%"=="2" (
    set "SPRING_PROFILE=prod"
    echo Modo: PRODUCCION
) else (
    echo ERROR: Opción inválida
    pause
    exit /b 1
)

echo.
echo [1/3] Verificando Maven...
echo Maven Home: "%MAVEN_HOME%"

REM Verificar si existe el directorio de Maven
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo.
    echo x ERROR: No se encuentra Maven en "%MAVEN_HOME%\bin\mvn.cmd"
    echo.
    pause
    exit /b 1
)

REM Verificar que Maven funciona
call mvn --version
if %errorlevel% neq 0 (
    echo.
    echo x ERROR: Maven no funciona correctamente. Error: %errorlevel%
    echo.
    pause
    exit /b 1
)

echo.
echo v Maven disponible
echo.
echo [2/3] Compilando y ejecutando Backend (Spring Boot)...
echo   Se abrira una nueva ventana para el backend
echo   Perfil: %SPRING_PROFILE%
echo.

REM Iniciar Backend en nueva ventana con perfil
start "EZBar Backend - Spring Boot - %SPRING_PROFILE%" cmd /k "cd /d "%PROJECT_DIR%" && mvn clean install -DskipTests && mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=%SPRING_PROFILE%"

echo v Backend iniciado en ventana separada
echo.
echo [3/3] Esperando 30 segundos antes de iniciar la API...
echo.

REM Esperar 30 segundos
timeout /t 30 /nobreak

echo.
echo Iniciando API (Node.js)...
echo   Se abrira una nueva ventana para la API
echo.

REM Iniciar API en nueva ventana
if "%MODE%"=="1" (
    start "EZBar API - Node.js (DEV)" cmd /k "cd /d "%PROJECT_DIR%Api\api" && npm run start:dev"
) else (
    start "EZBar API - Node.js (PROD)" cmd /k "cd /d "%PROJECT_DIR%Api\api" && npm start"
)

echo.
echo Iniciando Ngrok...
echo   Se abrira una nueva ventana para Ngrok
echo.

REM Ruta a ngrok
set "NGROK_PATH=D:\GitHub\ngrok-v3-stable-windows-amd64\ngrok.exe"

REM Verificar si existe ngrok
if not exist "%NGROK_PATH%" (
    echo.
    echo x ADVERTENCIA: Ngrok no encontrado en "%NGROK_PATH%"
    echo.
) else (
    REM Iniciar Ngrok en nueva ventana (expone puerto 3000)
    start "EZBar Ngrok" "%NGROK_PATH%" http 3000
)

echo.
echo ================================================
echo  v Servicios Iniciados
echo ================================================
echo.
echo Backend: Ventana "EZBar Backend - Spring Boot - %SPRING_PROFILE%"
echo API:     Ventana "EZBar API - Node.js"
echo Ngrok:   Ventana "EZBar Ngrok"
echo.
echo Puedes cerrar esta ventana.
echo.
pause

