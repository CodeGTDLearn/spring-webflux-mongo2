@echo off
echo ===========================================================================
echo                     DOCKER-COMPOSE-UP SCRIPT: STARTING
echo ===========================================================================

:: SETTING JDK11 AS DEFAULT
set JAVA_HOME=C:\Program Files\Java\jdk-11.0.10+9

:: MAVEN CLEAN PACKAGE
cd ..
call mvn clean package -DskipTests

:: DOCKER CLEAN-UP SYSTEM
cd docker
call clean.bat compose

docker scan --version --json --group-issues

:: DOCKER-COMPOSE UP
set parameter1=%1
echo ===========================================================================
echo                       DOCKER-COMPOSE UPs: %parameter1%
echo ===========================================================================
if %parameter1%==prod (docker-compose -f prod-compose.yml up --build --force-recreate)
if %parameter1%==dev  (docker-compose -f dev-compose.yml  up --build --force-recreate)

REM TIMEOUT 05
timeout 5

echo ===========================================================================
echo                             DOCKER-EXEC-CHMOD
echo ===========================================================================
::echo %parameter1%
::if %parameter1%==prod (docker exec mongo1 bash -c "chmod +x /scripts/rs-init.sh && sh /scripts/rs-init.sh")

pause
echo ===========================================================================
echo                     DOCKER-COMPOSE-UP SCRIPT: FINISHING
echo ===========================================================================