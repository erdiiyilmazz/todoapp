package com.erdi.todoapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration{

    @Value("${spring.couchbase.connection-string}")
    private String host;

    @Value("${spring.couchbase.username}")
    private String username;

    @Value("${spring.couchbase.password}")
    private String password;

    @Value("${spring.couchbase.bucket-name}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return host;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }
}