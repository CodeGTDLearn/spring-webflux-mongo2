#!/bin/bash

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
rs.initiate(config, { force: true });
rs.status();
EOF