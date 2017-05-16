#!/usr/bin/env bash
cd ../
./gradlew clean build -x test :auth_server:jar
cd -
docker build --tag braganavt/auth_service:1 ./
