@echo off
REM ====================================
REM   Ejecutando Tests con Cobertura
REM   Backend Spring Boot - JaCoCo
REM ====================================

echo.
echo ====================================
echo   Ejecutando Tests con Cobertura
echo ====================================
echo.

cd /d "%~dp0"

REM Ejecutar tests con Maven
echo Ejecutando: mvn clean test
echo.
call mvn clean test

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo   ❌ Tests fallaron
    echo ========================================
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo   ✅ Tests completados exitosamente
echo ========================================
echo.
echo 📊 Reporte de cobertura generado en:
echo    target\site\jacoco\index.html
echo.
echo Abriendo reporte en navegador...
echo.

REM Verificar si existe el reporte
if exist "target\site\jacoco\index.html" (
    start target\site\jacoco\index.html
) else (
    echo ⚠️  No se encontró el reporte HTML
    echo    Verifica que los tests se hayan ejecutado correctamente
)

echo.
pause
