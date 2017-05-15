#!/usr/bin/env bash
cd ../../
./gradlew clean build -x test :homeworks/HW3:jar
cd -
docker build --tag braganavt/game_service:1 ./
