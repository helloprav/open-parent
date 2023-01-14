cls
echo off

FOR /F "tokens=1,2 delims==" %%G IN (env.properties) DO (
	set %%G=%%H
	echo "%%G=%%H"
)

FOR /F "tokens=1,2 delims==" %%G IN (env-%deployEnv%.properties) DO (
	set %%G=%%H
	echo "%%G=%%H"
)
echo on
set "crt_dir=%~dp0"


start cmd.exe /c db-z-install.bat
timeout /t 10 /nobreak

cd %dbHome%

rem read the file: installed.dat, if not found execute below line to set password
.\bin\mysql -u %dbUser% --skip-password -e "alter user '%dbUser%'@'localhost' identified by '%dbPassword%';"
rem create command to create empty file: installed.dat

cd %crt_dir%
echo "MySQL Database Setup Completed Successfully"
pause
