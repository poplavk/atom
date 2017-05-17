#!/usr/bin/env bash
docker build --tag braganavt/frontend_service:1 ./
docker run --rm -p 80:80 --name front braganavt/frontend_service:1