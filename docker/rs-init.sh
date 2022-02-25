#!/bin/bash
echo "SCRIPT - STARTING"
mongod --replSet docker-rs --port 9042
sleep 5s
echo "Delay-Sleep done"
mongo --port 9042 <<EOF
var replicasetConfig = {
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
rs.initiate(replicasetConfig);
rs.status();
use admin;
db.createUser({user: "admin",pwd: "admin",roles: [ { role: "root", db: "admin" }, "root" ]});
exit
EOF
echo "Adding in etc/host - done"
"127.0.0.1  localhost mongo1 mongo2 mongo3" >> /etc/hosts
cat > /etc/hosts
echo "SCRIPT - FINISHED"