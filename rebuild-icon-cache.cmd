@echo off

net.exe session 1>NUL 2>NUL || (echo This script requires administrative permissions. Please run as administrator. & pause & exit /B 1)

echo/
echo The explorer process must be temporarily killed before deleting the cache files. 
echo/
echo Please SAVE ALL OPEN WORK before continuing.
echo/
pause
echo/
ie4uinit.exe -show
taskkill /IM explorer.exe /F
echo/
echo Attempting to delete files...
timeout /t 2 /nobreak > NUL
goto delete


:delete
echo/
del /A /F /Q %localappdata%\IconCache.db
del /A /F /Q %localappdata%\Microsoft\Windows\Explorer\iconcache*
del /A /F /Q %localappdata%\Microsoft\Windows\Explorer\thumbcache*
echo/
CHOICE /C:YN /M "Were all the files deleted? (There should be 3 lines that start with \"Could Not Find\".)"
IF ERRORLEVEL 2 goto delete
IF ERRORLEVEL 1 goto restart


:restart
echo/
echo/
echo You will need to restart the PC to finish rebuilding your icon cache.
echo/
CHOICE /C:YN /M "Do you want to restart your PC now?"
IF ERRORLEVEL 2 goto no
IF ERRORLEVEL 1 goto yes

:yes
shutdown /r /t 0

:no
start explorer.exe
exit /B
