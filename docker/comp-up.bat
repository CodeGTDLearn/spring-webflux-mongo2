REM ===================================================================
REM                 DOCKER-COMPOSE SYSTEM: STARTING
REM ===================================================================

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
docker-compose -f compose.yml up --build --force-recreate

REM TIMEOUT 05
timeout 5

REM ===================================================================
REM                            CONDITIONAL
REM ===================================================================
set parameter1=%1
echo %parameter1%
if %parameter1%==prod (docker exec mongo1 bash -c "chmod +x /scripts/rs-init.sh && sh /scripts/rs-init.sh")

pause
REM ===================================================================
REM                 DOCKER-COMPOSE SYSTEM: FINISHING
REM ===================================================================