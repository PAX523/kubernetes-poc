#!/usr/bin/env bash

docker run  \
  -p 3306:3306 \
  -v /home/alpaxoul/ServerAlpaxoul/database/mysql:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=my-secret-pw \
  --name mysql-server \
  --network internal-network \
  --rm \
  -d \
  mysql/mysql-server:8.0.32-1.2.11-server

# Prepare SQL user to access database in local development environment:
#
#   docker exec -ti mysql-server bash
#   mysql -u root -p
#   SELECT host, user FROM mysql.user;
#   CREATE USER 'development'@'%' IDENTIFIED BY 'password';
#   GRANT ALL ON *.* TO 'development'@'%';
