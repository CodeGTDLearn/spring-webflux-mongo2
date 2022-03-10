#!/bin/bash
# GUIDE: https://blog.tericcabrel.com/mongodb-replica-set-docker-compose/
echo '-------------------------------------------------------------------------'
echo                   "A) REPLICASET CONFIG - Starting..."
echo '-------------------------------------------------------------------------'

echo '-------------------------------------------------------------------------'
echo               "B) CREATE RS-INSTANCE-1 (Compose-Service-1)"
echo '-------------------------------------------------------------------------'
# A RS is a group of 'mongod processes' with the same data set
#mongod --replSet dbrs --port 9042
echo '-------------------------------------------------------------------------'
echo              "C) SLEEP: WAITING  RS-INSTANCE-1 LOADING"
echo '-------------------------------------------------------------------------'
#sleep 10

echo '-------------------------------------------------------------------------'
echo                      "D) CONFIGURING RS-INSTANCE-1"
echo '-------------------------------------------------------------------------'
# initiate the RS by connecting to the first running RS-instance (mongo1)
mongo --port 9042 <<EOF
var config = {
    "_id": "docker-rs",
    "version": 1,
    "members": [
        {
            "_id": 1,
            "host": "mongo1:9042",
            "priority": 3
        },
        {
            "_id": 2,
            "host": "mongo2:9142",
            "priority": 2
        },
        {
            "_id": 3,
            "host": "mongo3:9242",
            "priority": 1
        }
    ]
};
rs.initiate(config, { force: true });
rs.status();
use admin;
db.createUser({user: "admin",pwd: "admin",roles: [ { role: "root", db: "admin" }, "root" ]});
exit;
EOF
#echo '-------------------------------------------------------------------------'
#echo                          "E) CONFIG LINUX-HOSTS"
#echo '-------------------------------------------------------------------------'
echo '127.0.0.1  localhost mongo1 mongo2 mongo3' >> /etc/hosts
sleep 2
#
#echo '-------------------------------------------------------------------------'
#echo                           "E.1) SHOW LINUX-HOSTS"
#echo '-------------------------------------------------------------------------'
cat /etc/hosts
#
#echo '-------------------------------------------------------------------------'
#echo                   "A) REPLICASET CONFIG - Ending..."
#echo '-------------------------------------------------------------------------'