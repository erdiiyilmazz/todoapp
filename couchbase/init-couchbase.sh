set -e

until curl -s http://localhost:8091/pools > /dev/null; do
    echo "Waiting for Couchbase to start..."
    sleep 5
done

if ! couchbase-cli server-list -c localhost:8091 -u Administrator -p password > /dev/null 2>&1; then
    echo "Initializing Couchbase cluster..."
    couchbase-cli cluster-init -c localhost \
        --cluster-username Administrator \
        --cluster-password password \
        --services data,index,query \
        --cluster-ramsize 512 \
        --cluster-index-ramsize 256 \
        --index-storage-setting default
else
    echo "Cluster is already initialized."
fi

if ! couchbase-cli bucket-list -c localhost:8091 -u Administrator -p password | grep -q "todo"; then
    echo "Creating 'todo' bucket..."
    couchbase-cli bucket-create -c localhost \
        --username Administrator \
        --password password \
        --bucket todo \
        --bucket-type couchbase \
        --bucket-ramsize 128
else
    echo "Bucket 'todo' already exists."
fi

sleep 10

echo "Creating primary index..."
max_retries=5
retry_interval=10
for i in $(seq 1 $max_retries); do
    if echo "CREATE PRIMARY INDEX ON \`todo\`;" | cbq -e http://localhost:8091 -u Administrator -p password > /dev/null 2>&1; then
        echo "Primary index created successfully"
        break
    else
        echo "Failed to create primary index. Retrying in $retry_interval seconds..."
        sleep $retry_interval
    fi
done

if [ $i -eq $max_retries ]; then
    echo "Failed to create primary index after $max_retries attempts"
    exit 1
fi

echo "Couchbase initialization completed."

tail -f /dev/null