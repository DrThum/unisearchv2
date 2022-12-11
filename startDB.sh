#!/bin/bash

docker run --rm \
  --name unisearch-db \
  -p 5432:5432 \
  -e POSTGRES_USER="unisearch"\
  -e POSTGRES_PASSWORD="unisearch"\
  -e POSTGRES_DB="unisearch"\
  -d postgres

sleep 1

psql postgresql://unisearch:unisearch@localhost/unisearch < src/main/resources/bootstrap.sql
