@echo off
setlocal enabledelayedexpansion
echo ==========================================
echo   Iniciando Servidores EZBar
echo ==========================================
echo.

REM Intentar detectar la IP Local
set "IP_ADDRESS="
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr /C:"IPv4"') do (
    set "TEMP_IP=%%a"
    set "TEMP_IP=!TEMP_IP: =!"
    REM Preferir IPs que empiezan por 192 o 172 o 10 (locales comunes)
    echo !TEMP_IP! | findstr /R "^192\. ^172\. ^10\." >nul
    if !errorlevel! equ 0 (
        set "IP_ADDRESS=!TEMP_IP!"
    ) else (
        if not defined IP_ADDRESS set "IP_ADDRESS=!TEMP_IP!"
    )
)

if not defined IP_ADDRESS set "IP_ADDRESS=No detectada (Verifica ipconfig)"

echo Tu IP Local detectada parece ser: %IP_ADDRESS%
echo Asegurate de que tu movil este en la misma red Wi-Fi y usa esta IP en la app.
echo.

REM Definir rutas relativas
set "SCRIPT_DIR=%~dp0"
REM Eliminar backslash final si existe
if "%SCRIPT_DIR:~-1%"=="\" set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

set "BACKEND_JAR=%SCRIPT_DIR%\target\ezbar-backend-1.0.0.jar"
set "API_DIR=%SCRIPT_DIR%\Api\api"

echo Verificando archivos...
echo JAR Backend: "%BACKEND_JAR%"
echo API Node: "%API_DIR%"
echo.

if not exist "%BACKEND_JAR%" (
    echo [ERROR] No se encuentra el JAR del backend.
    echo Ruta buscada: "%BACKEND_JAR%"
    echo.
    echo Probablemente necesitas compilar el backend primero.
    echo Ejecuta: cd EZBar_Backend ^& mvn clean install
    echo.
    pause
    exit /b 1
)

if not exist "%API_DIR%\server.js" (
    echo [ERROR] No se encuentra el archivo server.js de la API.
    echo Ruta buscada: "%API_DIR%\server.js"
    echo.
    pause
    exit /b 1
)

echo Iniciando Backend Java (Puerto 8080)...
start "EZBar Java Backend" java -jar "%BACKEND_JAR%"

echo Iniciando Middleware Node.js (Puerto 3000)...
cd /d "%API_DIR%"
start "EZBar Node Middleware" npm start

echo Iniciando Ngrok (Puerto 3000)...
start "EZBar Ngrok" "D:\GitHub\ngrok-v3-stable-windows-amd64\ngrok.exe" http 3000

echo.
echo Servidores iniciados correctamente (revisa las otras ventanas para errores).
echo.
pause
