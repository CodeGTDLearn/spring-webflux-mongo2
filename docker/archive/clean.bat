@echo off
echo ===========================================================================
echo                           CLEAN-UP: starting
echo ===========================================================================

cd
docker system df

echo ===========================================================================
echo           CLEAN-UP: compose cleaning-up %parameter1% %parameter2%
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

echo ===========================================================================
echo                      CLEAN-UP: listing images
echo ===========================================================================
docker image ls

echo ===========================================================================
echo                      CLEAN-UP: listing system
echo ===========================================================================
docker system df

:: SETTING JDK17 AS DEFAULT
::set JAVA_HOME=C:\Users\SERVIDOR\.jdks\openjdk-17.0.2

:: CLOSING ALL CMD-SCREENS
:: TASKKILL /F /IM cmd.exe /T

:: exit
echo ===========================================================================
echo                         CLEAN-UP: finishing
echo ===========================================================================