#!/bin/bash

gradle clean bootJar

docker build -t fenix2k/javaapp .
docker run -it --rm \
    --name javaAppServer \
    -p 8080:8080 \
    fenix2k/javaapp
