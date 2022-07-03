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
if %parameter1%==rs-auth (
    cd
    cd rs-singlenode-auth
    ::docker-compose -f replicaset-auth-compose.yml up --build --force-recreate
    docker-compose -f replicaset-auth-compose.yml up --build --force-recreate --detach
    docker-compose -f replicaset-auth-compose.yml rm -svf rs-setup
    docker-compose -f replicaset-auth-compose.yml rm -svf mongo-key
    cd ..                )

cd
if %parameter1%==rs (
    ::docker-compose -f replicaset-compose.yml up --build --force-recreate
    docker-compose -f replicaset-compose.yml up --build --force-recreate --detach
    docker-compose -f replicaset-compose.yml rm -svf rs-setup)

if %parameter1%==std     (docker-compose -f standalone-compose.yml            up --build --force-recreate)
::if %parameter1%==rs3     (docker-compose -f threenodes-replicaset-noauth-compose.yml up --build --force-recreate)

echo ===========================================================================
echo                     5) DOCKER-COMPOSE: ...Ending
echo ===========================================================================