#!/bin/bash
set -e

# Wait for Couchbase Server to be ready
until curl -s http://localhost:8091/pools > /dev/null; do
    echo "Waiting for Couchbase Server to start..."
    sleep 1
done

# Setup initial cluster
couchbase-cli cluster-init \
    --cluster localhost \
    --cluster-name myCluster \
    --cluster-username Administrator \
    --cluster-password password \
    --services data,index,query \
    --cluster-ramsize 512 \
    --cluster-index-ramsize 256 \
    --index-storage-setting default

# Wait for cluster to be ready
sleep 10

# Create bucket
couchbase-cli bucket-create \
    --cluster localhost \
    --username Administrator \
    --password password \
    --bucket todo \
    --bucket-type couchbase \
    --bucket-ramsize 256

# Wait for bucket to be ready
sleep 10

# After creating the bucket, add this:
echo "Verifying bucket creation..."
couchbase-cli bucket-list -c localhost -u Administrator -p password | grep todo
if [ $? -ne 0 ]; then
    echo "Bucket 'todo' was not created successfully."
    exit 1
fi

# Create user
couchbase-cli user-manage \
    --cluster localhost \
    --username Administrator \
    --password password \
    --set \
    --rbac-username todoapp \
    --rbac-password todoapppass \
    --roles admin,bucket_full_access[todo] \
    --auth-domain local

# Create primary index
cbq -e http://localhost:8093 -u Administrator -p password \
    -s "CREATE PRIMARY INDEX ON \`todo\`"

echo "Couchbase initialized successfully."

# Final health check
for i in {1..30}; do
  if curl -s http://localhost:8091/pools/default > /dev/null; then
    echo "Couchbase is fully operational."
    exit 0
  fi
  echo "Waiting for Couchbase to be fully operational... ($i/30)"
  sleep 5
done

echo "Couchbase did not become fully operational in time."
exit 1