#!/bin/sh

echo 'Starting environment configured to work with infra ..'

docker-compose -f db.yml -f env.yml up -d

echo '..Started'