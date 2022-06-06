@echo off
echo ===========================================================================
echo                        1) DOCKER-COMPOSE: Starts...
echo ===========================================================================

echo ===========================================================================
echo                         2) DOCKER-COMPOSE: Maven
echo ===========================================================================
cd
cd ..\..
cd
call mvn clean package -DskipTests

echo ===========================================================================
echo                        3) DOCKER-COMPOSE: Cleaning
echo ===========================================================================
cd docker
cd scripts
call compose-clean.bat
docker scan --version --json --group-issues
cd

set parameter1=%1
echo ===========================================================================
echo       4) DOCKER-COMPOSE: Uping the Compose-Service(s): %parameter1%
echo ===========================================================================
if %parameter1%==devrs  (docker-compose -f singlenode-replicaset-noauth-compose.yml up --build --force-recreate)
if %parameter1%==devstd (docker-compose -f standalone-compose.yml up --build --force-recreate)
if %parameter1%==prodrs (docker-compose -f threenodes-replicaset-noauth-compose.yml   up --build --force-recreate)

echo ===========================================================================
echo                     5) DOCKER-COMPOSE: ...Ending
echo ===========================================================================