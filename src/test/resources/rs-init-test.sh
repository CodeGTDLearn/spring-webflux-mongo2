#!/bin/bash
echo "SCRIPT was execute by the docker-compose - STARTING"
#mongod --replSet docker-rs --port 9042
sleep 5
echo "Delay-Sleep done"
mongo --port 9042 <<EOF
var config = {
    "_id": "docker-rs",
    "members": [
        {
            "_id": 0,
            "host": "mongo1:9042",
            "priority": 3
        },
        {
            "_id": 1,
            "host": "mongo2:9142",
            "priority": 2
        },
        {
            "_id": 2,
            "host": "mongo3:9242",
            "priority": 1
        }
    ]
};
rs.initiate(config);
rs.status();
use admin;
db.createUser({user: "admin",pwd: "admin",roles: [ { role: "root", db: "admin" }, "root" ]});
exit
EOF
echo "Adding in etc/host - done"
echo "127.0.0.1  localhost mongo1 mongo2 mongo3" >> /etc/hosts
echo cat > /etc/hosts
echo "SCRIPT was execute by the docker-compose - FINISHED"