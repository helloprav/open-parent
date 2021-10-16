
echo off  
FOR /F "tokens=1,2 delims==" %%G IN (env.properties) DO (set %%G=%%H)  
echo + deployEnv      -- %deployEnv%
echo + dbName         -- %dbName%

mysql -hlocalhost -uroot -proot %dbName% < db-objects-create-triggers.sql
