#!/usr/bin/env bash

sudo docker rm -f external-service
sudo docker build -t external-service:latest .
sudo docker run -d \
                --name external-service \
                --network internal-network \
                -p 80:8080 \
                external-service
