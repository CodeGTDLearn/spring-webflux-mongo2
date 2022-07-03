@echo off
echo ===========================================================================
echo                           CLEAN-UP: starting
echo ===========================================================================
cd
cd ..
cd
docker system df

echo ===========================================================================
echo           CLEAN-UP: compose cleaning-up %parameter1% %parameter2%
echo ===========================================================================
docker-compose -f replicaset-compose.yml down --remove-orphans
docker-compose -f standalone-compose.yml down --remove-orphans
docker system df

cd rs-singlenode-auth
cd
docker-compose -f replicaset-auth-compose.yml down --remove-orphans
cd ..
cd

docker container prune --force
docker system prune --volumes --force
docker network prune --force
docker builder prune --all --force

docker image rm pauloportfolio/mongo1
docker image rm pauloportfolio/api-rs
docker image rm pauloportfolio/api-rs-auth
docker image rm pauloportfolio/api-standalone
::------------------------------------------------------------------------------

echo ===========================================================================
echo                      CLEAN-UP: listing images
echo ===========================================================================
docker image ls

echo ===========================================================================
echo                      CLEAN-UP: listing system
echo ===========================================================================
docker system df

echo ===========================================================================
echo                         CLEAN-UP: finishing
echo ===========================================================================
docker system df