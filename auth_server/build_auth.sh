#!/usr/bin/env bash
cd ../
./gradlew clean build -x test -x check :auth_server:jar
cd -
docker build --tag braganavt/auth_service:1 ./
docker run --rm -p 8080:8080 --name  auth_service braganavt/auth_service:1
