echo "registering service"
cd ..\mysql-8.0.19-winx64\bin
.\mysqld --install "mysql-8.0.19-winx64"
sc start "mysql-8.0.19-winx64"
rem .\mysqld --remove "mysql-8.0.19-winx64"
