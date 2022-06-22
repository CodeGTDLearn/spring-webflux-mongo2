# !/bin/bash

echo "================================================================================================"
echo                               "RS-SCRIPT: Starting..."
echo "================================================================================================"

echo "================================================================================================"
echo     "RS-SCRIPT: REPEAT Env-Vars FROM: env-rs-admin-auth.env + env-rs-general.env or Secrets "
echo "================================================================================================"
MONGO_DB=mydb
MONGO_USERNAME=usern
MONGO_PASSWORD=passxxx
HOST_DB_COMPOSE_SERVICE_NAME=$1
PORT_DB=$2
SLEEP_TIME=5s

echo "VAR 1: $MONGO_DB"
echo "VAR 2: $MONGO_USERNAME"
echo "VAR 3: ${MONGO_PASSWORD}"
echo "VAR 4: ${HOST_DB_COMPOSE_SERVICE_NAME}"
echo "VAR 5: ${PORT_DB}"

echo "================================================================================================"
echo                              "RS-SCRIPT: Configuring Replicaset"
echo "================================================================================================"
sleep $SLEEP_TIME
mongo -u "$MONGO_USERNAME" -p "$MONGO_PASSWORD" --host $HOST_DB_COMPOSE_SERVICE_NAME --port $PORT_DB --eval "rs.initiate()"
sleep $SLEEP_TIME
mongo -u "$MONGO_USERNAME" -p "$MONGO_PASSWORD" --host $HOST_DB_COMPOSE_SERVICE_NAME --port $PORT_DB --eval "rs.status()"
sleep $SLEEP_TIME
mongo -u "$MONGO_USERNAME" -p "$MONGO_PASSWORD" --host $HOST_DB_COMPOSE_SERVICE_NAME --port $PORT_DB --eval "rs.conf()"
sleep $SLEEP_TIME

echo "================================================================================================"
echo                         "RS-SCRIPT: Cleaning Environment variables..."
echo "================================================================================================"
unset MONGO_DB
unset MONGO_USERNAME
unset MONGO_PASSWORD
unset HOST_DB_COMPOSE_SERVICE_NAME
unset PORT_DB

echo "VAR 1: $MONGO_DB"
echo "VAR 2: $MONGO_USERNAME"
echo "VAR 3: ${MONGO_PASSWORD}"
echo "VAR 4: ${HOST_DB_COMPOSE_SERVICE_NAME}"
echo "VAR 5: ${PORT_DB}"

echo "================================================================================================"
echo                                    "RS-SCRIPT: Finished..."
echo "================================================================================================"