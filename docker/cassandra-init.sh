#!/usr/bin/env bash

until cqlsh -f create_db.cql; do
  echo "cqlsh: Cannot initialize Cassandra - will try in a sec!"
  sleep 2
done &

exec /docker-entrypoint.sh "$@"