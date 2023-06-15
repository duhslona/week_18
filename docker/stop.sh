#!/bin/sh

echo 'Stopping environment configured to work with infra ..'

docker-compose -f db.yml -f env.yml down

echo '..Done'