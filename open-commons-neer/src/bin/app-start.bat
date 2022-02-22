rem https://dzone.com/articles/packaging-springboot-application-with-external-dep
echo off

echo off  
rem FOR /F "tokens=1,2 delims==" %%G IN (env.properties) DO (set %%G=%%H)  
FOR /F "tokens=1,2 delims==" %%G IN (env.properties) DO (
	set %%G=%%H
	echo "%%G=%%H"
)

FOR /F "tokens=1,2 delims==" %%G IN (env-%deployEnv%.properties) DO (
	set %%G=%%H
	rem echo "%%G=%%H"
)

set "crt_dir=%~dp0"
for %%I in ("%crt_dir%\..") do set "APP_HOME=%%~fI"
echo + APP_HOME      -- %APP_HOME%

set "logDir=%APP_HOME%\logs"
echo + logDir      -- %logDir%



echo %CD%

set "runCommand=java -Xloggc:%logDir%/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%logDir% -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9888 -Dcom.sun.management.jmxremote.ssl=FALSE -Dcom.sun.management.jmxremote.authenticate=FALSE -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -Dspring.profiles.active=%deployEnv% -DAPP_HOME=%APP_HOME% -DLOG_LEVEL=%logLevel% -DdbName=%dbName% -DdbUser=%dbUser% -DdbPassword=%dbPassword% -Dloader.path=%APP_HOME%/config -jar %APP_HOME%/lib/open-commons-neer-0.0.1-SNAPSHOT.jar"

echo %runCommand%
%runCommand%

rem java -Xloggc:%logDir%/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%logDir% -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9888 -Dcom.sun.management.jmxremote.ssl=FALSE -Dcom.sun.management.jmxremote.authenticate=FALSE -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -Dspring.profiles.active=%deployEnv% -DAPP_HOME=%APP_HOME% -DLOG_LEVEL=%logLevel% -DdbName=%dbName% -Dloader.path=%APP_HOME%/config -jar %APP_HOME%/lib/open-commons-neer-0.0.1-SNAPSHOT.jar
pause
