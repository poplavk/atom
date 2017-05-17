#!/usr/bin/env bash
cd ../../
./gradlew clean build -x test -x check :homeworks/HW3:jar
cd -
docker build --tag braganavt/game_service:1 ./
docker run --rm -p 8089:8089 --name game_server --link auth_service:auth_service braganavt/game_service:1