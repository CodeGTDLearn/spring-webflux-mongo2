@echo off
echo ===========================================================================
echo                        1) DOCKER-COMPOSE: Starts...
echo ===========================================================================

echo ===========================================================================
echo                         2) DOCKER-COMPOSE: Maven
echo ===========================================================================
cd ..
call mvn clean package -DskipTests

echo ===========================================================================
echo                        3) DOCKER-COMPOSE: Cleaning
echo ===========================================================================
cd docker
call clean.bat compose
docker scan --version --json --group-issues

echo ===========================================================================
echo    4) DOCKER-COMPOSE: Uping the follow Compose-Service(s): %parameter1%
echo ===========================================================================
set parameter1=%1
if %parameter1%==dev   (docker-compose -f dev-compose.yml    up --build --force-recreate)
if %parameter1%==prod  (docker-compose -f prod-compose.yml   up --build --force-recreate)

echo ===========================================================================
echo                     5) DOCKER-COMPOSE: ...Ending
echo ===========================================================================