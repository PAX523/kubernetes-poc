#!/usr/bin/env sh

/__cacert_entrypoint.sh

java -jar /opt/service/service.jar --server.port=$SERVER_PORT
