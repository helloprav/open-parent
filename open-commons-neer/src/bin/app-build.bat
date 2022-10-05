CLS
FOR /F "tokens=1,2 delims==" %%G IN (env.properties) DO (
	set %%G=%%H
	echo "%%G=%%H"
)
set appDistName=%appName%-%appVersion%
set distDir=%appName%\target\%appDistName%-distribution
set currentDir=%~dp0
set scriptDir=%~dp0
set scriptFile=%~dpnx0
echo %scriptDir%

echo %date%
REM ## Step1: Calculate ececution date time
set day=%date:~7,2%
set month=%date:~4,2%
set year=%date:~10,4%
set today=%year%%month%%day%

FOR /F "TOKENS=1 DELIMS=:" %%A IN ("%TIME%") DO SET HH=%%A
FOR /F "TOKENS=2 DELIMS=:" %%A IN ("%TIME%") DO SET MM=%%A

REM replace empty space with 0 [https://www.dostips.com/DtTipsStringManipulation.php]
set hh=%hh: =0%

set dTM=%today%-%hh%%mm%

REM ## Step2: Navigate to open-parent and do complete mvn clean install
cd ../../..
call mvn clean install -Drevision=%appVersion%
@echo on
if %errorlevel%==0 (
	REM ## cd src\bin
	echo %cd%
	echo %distDir%
	cd
	cd %distDir%
	cd
	echo %cd%
	mkdir \Work\Apps\Neer\%dTM%\%appDistName%\logs
	xcopy /s %appDistName% \Work\Apps\Neer\%dTM%\%appDistName%
) else (
	echo error
)

cd %scriptDir%
pause
