#!/usr/bin/env bash

sudo docker rm -f internal-service
sudo docker build -t internal-service:latest .
sudo docker run -d \
                --name internal-service \
                --network internal-network \
                internal-service
