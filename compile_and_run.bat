@echo off
setlocal
echo.
echo ================================================
echo  EZBar - Iniciando Servicios (DEBUG MODE)
echo ================================================
echo.

REM Obtener el directorio del script
set "PROJECT_DIR=%~dp0"
echo Directorio del proyecto: "%PROJECT_DIR%"
cd /d "%PROJECT_DIR%"

REM Configurar Maven en el PATH
set "MAVEN_HOME=%PROJECT_DIR%maven\apache-maven-3.9.6"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

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
echo [2/3] Iniciando Backend (Spring Boot)...
echo   Se abrira una nueva ventana para el backend
echo.

REM Iniciar Backend en nueva ventana
start "EZBar Backend - Spring Boot" cmd /k "cd /d "%PROJECT_DIR%" && mvn clean install -DskipTests && mvn spring-boot:run"

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
start "EZBar API - Node.js" cmd /k "cd /d "%PROJECT_DIR%Api\api" && npm start"

echo.
echo Iniciando Ngrok...
echo.
start "EZBar Ngrok" "D:\GitHub\ngrok-v3-stable-windows-amd64\ngrok.exe" http 3000

echo.
echo ================================================
echo  v Servicios Iniciados
echo ================================================
echo.
echo Backend: Ventana "EZBar Backend - Spring Boot"
echo API:     Ventana "EZBar API - Node.js"
echo.
echo Puedes cerrar esta ventana.
echo.
pause

