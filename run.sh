#!/usr/bin/env bash

set -e

mvn clean install
export DOCKER_IP=$(docker-machine ip $(docker-machine active))
docker-compose up -d
docker-compose logs