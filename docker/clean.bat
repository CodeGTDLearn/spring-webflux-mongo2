@echo off
echo ===========================================================================
echo                        DOCKER CLEAN-UP - STARTING
echo ===========================================================================

cd
docker system df

echo ===========================================================================
echo          DOCKER-COMPOSE CLEANING-UP %parameter1% %parameter2%
echo ===========================================================================
::set parameter1=%1
::set parameter2=%2
::docker-compose -f %parameter1%.yml down --remove-orphans
::docker-compose -f %parameter2%.yml down --remove-orphans
docker-compose -f dev-compose.yml down --remove-orphans
docker-compose -f prod-compose.yml down --remove-orphans
::------------------------------------------------------------------------------
docker container prune --force
docker system prune --volumes --force
docker network prune --force
docker builder prune --all --force

::----------------------------IMAGES TO CHANGE----------------------------------
docker image rm pauloportfolio/mongo1
docker image rm pauloportfolio/mongo2
docker image rm pauloportfolio/mongo3
::------------------------------------------------------------------------------

docker image ls
docker system df

:: RE-SETTING JDK8 AS DEFAULT
set JAVA_HOME=C:\Program Files\Java\jdk-8.0.282.8-hotspot

:: CLOSING ALL CMD-SCREENS
:: TASKKILL /F /IM cmd.exe /T

:: exit
echo ===========================================================================
echo                      DOCKER CLEAN-UP - FINISHING
echo ===========================================================================