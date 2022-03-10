#!/bin/bash
echo "sleeping for 10 seconds"
mongod --replSet docker-rs --port 9042
sleep 5
mongo --port 9042 <<EOF
var config = {
    "_id": "docker-rs",
    "members": [
        {
            "_id": 0,
            "host": "mongo1:9042"
        },
        {
            "_id": 1,
            "host": "mongo2:9142"
        },
        {
            "_id": 2,
            "host": "mongo3:9242"
        }
    ]
};
rs.initiate(config);
rs.status();
use admin;
db.createUser({user: "admin",pwd: "admin",roles: [ { role: "root", db: "admin" }, "root" ]});
exit
EOF
"127.0.0.1  localhost mongo1 mongo2 mongo3" >> /etc/hosts
sleep 5
cat > /etc/hosts