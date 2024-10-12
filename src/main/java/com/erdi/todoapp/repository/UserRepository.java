package com.erdi.todoapp.repository;

import java.util.Optional;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import com.erdi.todoapp.model.entity.User;

@Repository
public interface UserRepository extends CouchbaseRepository<User, String> {
    Optional<User> findByUsername(String username);
}
