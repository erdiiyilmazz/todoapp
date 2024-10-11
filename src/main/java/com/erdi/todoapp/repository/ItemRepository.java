package com.erdi.todoapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;
import com.erdi.todoapp.model.entity.Item;

@Repository
public interface ItemRepository extends CouchbaseRepository<Item, String> {

    List<Item> findByUserId(String userId);

    Optional<Item> findByIdAndUserId(String id, String userId);

}
