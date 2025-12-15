@echo off
setlocal

REM Configurar ruta al Maven incluido en el proyecto
set "PROJECT_ROOT=%~dp0"
set "MAVEN_BIN=%PROJECT_ROOT%maven\apache-maven-3.9.6\bin"
set "PATH=%MAVEN_BIN%;%PATH%"

echo ==========================================
echo   Iniciando Servidor EZBar Backend
echo ==========================================
echo.
echo Usando Maven en: %MAVEN_BIN%
echo.

REM Ejecutar la aplicacion Spring Boot
call mvn spring-boot:run

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Ocurrio un error al iniciar el servidor.
    pause
)
