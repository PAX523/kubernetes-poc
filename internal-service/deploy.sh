#!/usr/bin/env bash

sudo docker rm -f internal-service
sudo docker build -t internal-service:1.0.0 .
sudo docker run -d \
                --name internal-service \
                --network internal-network \
                internal-service
