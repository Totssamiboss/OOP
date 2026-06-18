@echo off
chcp 65001 > nul
cd /d "%~dp0"

title Запуск OopLabApp
echo [SYSTEM] Сборка и запуск приложения...
echo [SYSTEM] Текущая папка: %CD%
echo --------------------------------------------------

dotnet run

echo --------------------------------------------------
echo [SYSTEM] Программа завершила работу.
pause