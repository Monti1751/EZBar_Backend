@echo off
setlocal enabledelayedexpansion

REM Configurar Maven en el PATH
set "MAVEN_HOME=C:\maven\apache-maven-3.9.6"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

REM Verificar que Maven está disponible
echo Verificando Maven...
mvn --version

REM Navegar al directorio del proyecto
echo.
echo Compilando proyecto EZBar Backend...
cd /d "C:\Users\framonsil\GitHub\EZBar_Backend"

REM Compilar con Maven
mvn clean install -DskipTests

if %errorlevel% equ 0 (
    echo.
    echo ✓ Compilación exitosa!
    echo Ejecutando servidor...
    mvn spring-boot:run
) else (
    echo.
    echo ✗ Error en la compilación
    pause
)

endlocal
