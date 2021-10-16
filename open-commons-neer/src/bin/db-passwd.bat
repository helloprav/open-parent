set "crt_dir=%~dp0"
cd ..\mysql-8.0.19-winx64\bin
mysql -u root --skip-password -e "alter user 'root'@'localhost' identified by 'root';"
cd %crt_dir%
