#!/usr/bin/env bash
docker container stop hey-backend-app
docker container stop hey-frontend-app
docker container stop hey-redis

docker container rm hey-backend-app
docker container rm hey-frontend-app
docker container rm hey-redis


