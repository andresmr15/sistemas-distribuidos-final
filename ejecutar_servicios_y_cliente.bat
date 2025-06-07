cd /d %~dp0
start "" cmd /k "cd /d %~dp0servicio-deportes && call ejecutar_servicio.bat"
start "" cmd /k "cd /d %~dp0servicio-financiera && call ejecutar_servicio.bat"
start "" cmd /k "cd /d %~dp0servicio-laboratorio && call ejecutar_servicio.bat"
timeout /t 9 /nobreak >nul
start "" cmd /k "cd /d %~dp0servicio-orquestador && call ejecutar_servicio.bat"
start "" cmd /k "cd /d %~dp0estudiante-clienterest && call ejecutar_cliente.bat"