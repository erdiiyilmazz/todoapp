#!/bin/sh
set -e

host="$1"
shift
cmd="$@"

until curl -s http://$host:8091/pools/default > /dev/null; do
  >&2 echo "Couchbase is unavailable - sleeping"
  sleep 1
done

>&2 echo "Couchbase is up - executing command"
exec $cmd