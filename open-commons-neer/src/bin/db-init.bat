start cmd.exe /c db-start.bat
timeout /t 30 /nobreak
call db-passwd.bat
rem call db-service.bat
echo "MySQL Database Setup Completed Successfully"
