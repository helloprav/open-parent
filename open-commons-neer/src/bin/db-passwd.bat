FOR /F "tokens=1,2 delims==" %%G IN (env.properties) DO (
	set %%G=%%H
	echo "%%G=%%H"
)

FOR /F "tokens=1,2 delims==" %%G IN (env-%deployEnv%.properties) DO (
	set %%G=%%H
	rem echo "%%G=%%H"
)

set "crt_dir=%~dp0"
cd ..\mysql-8.0.19-winx64\bin
mysql -u root --skip-password -e "alter user 'root'@'localhost' identified by '%rootPassword%';"
cd %crt_dir%
