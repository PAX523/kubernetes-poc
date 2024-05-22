#!/usr/bin/env bash

docker run  \
  -p 80:80 \
  -v /home/alpaxoul/ServerAlpaxoul/nginx/nginx.conf:/etc/nginx/nginx.conf \
  --name nginx \
  --network internal-network \
  --rm \
  -d \
  nginx/nginx-ingress:edge-alpine
