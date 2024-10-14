#!/bin/sh
set -e

host="$1"
shift
cmd="$@"

echo "Waiting for Couchbase at $host:8091..."

until curl -s http://$host:8091/pools/default/buckets/todo > /dev/null; do
  >&2 echo "Couchbase or 'todo' bucket is not ready - sleeping"
  sleep 5
done

>&2 echo "Couchbase is up and 'todo' bucket is ready - executing command"
exec $cmd
