#!/bin/bash

podman run --detach \
    --volume ./data:/var/lib/postgresql/data \
    --env POSTGRES_USER=postgres \
    --env POSTGRES_PASSWORD=postgres \
    --env POSTGRES_DB=mystart \
    --name mystart-postgres \
    -p 5432:5432 \
    postgres:17.2 