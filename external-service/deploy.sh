#!/usr/bin/env bash

sudo docker rm -f poc-external-service
sudo docker build -t poc-external-service .
sudo docker run -d \
                --name poc-external-service \
                --network internal-network \
                poc-external-service
