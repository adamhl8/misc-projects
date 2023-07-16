@echo off

net.exe session 1>NUL 2>NUL || (echo This script requires administrative permissions. Please run as administrator. & pause & exit /B 1)

netsh winsock reset
netsh int ipv4 reset
netsh int ipv6 reset
ipconfig /release
ipconfig /flushdns
ipconfig /renew

pause