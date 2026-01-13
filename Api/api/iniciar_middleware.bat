@echo off
echo ==========================================
echo   Iniciando Middleware Node.js (Puerto 3000)
echo ==========================================
echo.
echo Asegurate de que el servidor Java (puerto 8080) este corriendo tambien!
echo.

cd /d "%~dp0"
call npm start

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Ocurrio un error al iniciar el middleware.
    pause
)
