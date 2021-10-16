rem https://dzone.com/articles/packaging-springboot-application-with-external-dep
echo off

echo off  
FOR /F "tokens=1,2 delims==" %%G IN (env.properties) DO (set %%G=%%H)  
echo + deployEnv      -- %deployEnv%
echo + logLevel      -- %logLevel%

set "crt_dir=%~dp0"
for %%I in ("%crt_dir%\..") do set "APP_HOME=%%~fI"
echo + APP_HOME      -- %APP_HOME%

set "sharedPath=%APP_HOME%\externalPath\env\%deployEnv%"
echo + sharedPath      -- %sharedPath%

set "logDir=%APP_HOME%\logs"
echo + logDir      -- %logDir%



echo %CD%
java -Xloggc:%logDir%/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%logDir% -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9888 -Dcom.sun.management.jmxremote.ssl=FALSE -Dcom.sun.management.jmxremote.authenticate=FALSE -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -DAPP_HOME=%APP_HOME% -DLOG_LEVEL=%logLevel% -Dspring.profiles.active=%deployEnv% -DsharedPath=%sharedPath% -Dloader.path=%APP_HOME%/config -jar %APP_HOME%/lib/open-commons-neer-0.0.1-SNAPSHOT.jar
