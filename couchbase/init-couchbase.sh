#!/bin/bash
set -e

until curl -s http://localhost:8091/pools/default > /dev/null; do
    echo "Waiting for Couchbase to start..."
    sleep 5
done

echo "Couchbase is up. Initializing..."

/opt/couchbase/bin/couchbase-cli cluster-init -c localhost:8091 \
    --cluster-username Administrator \
    --cluster-password password \
    --services data,index,query \
    --cluster-ramsize 512 \
    --cluster-index-ramsize 256 \
    --cluster-fts-ramsize 256 \
    --index-storage-setting default

echo "Cluster initialized. Waiting for services to start..."
sleep 30

echo "Creating bucket..."
/opt/couchbase/bin/couchbase-cli bucket-create -c localhost:8091 \
    -u Administrator \
    -p password \
    --bucket todo \
    --bucket-type couchbase \
    --bucket-ramsize 128

echo "Bucket created. Waiting for bucket to be ready..."
sleep 20

echo "Creating primary index..."
for i in {1..5}; do
    if /opt/couchbase/bin/cbq -e http://localhost:8091 -u Administrator -p password \
        -s "CREATE PRIMARY INDEX ON \`todo\`" > /dev/null 2>&1; then
        echo "Primary index created successfully."
        break
    else
        echo "Attempt $i to create primary index failed. Retrying in 10 seconds..."
        sleep 10
    fi
done

echo "Couchbase initialization completed."

until curl -s http://localhost:8091/pools/default/buckets/todo > /dev/null; do
    echo "Waiting for 'todo' bucket to be accessible..."
    sleep 5
done

touch /opt/couchbase/var/lib/couchbase/init_complete

tail -f /dev/null
