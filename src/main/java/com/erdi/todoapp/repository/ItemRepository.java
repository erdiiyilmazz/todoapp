package com.erdi.todoapp.repository;

import java.util.List;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;
import com.erdi.todoapp.model.entity.Item;

@Repository
public interface ItemRepository extends CouchbaseRepository<Item, String> {
    List<Item> findByUserIdAndDeletedFalse(String userId);
}
