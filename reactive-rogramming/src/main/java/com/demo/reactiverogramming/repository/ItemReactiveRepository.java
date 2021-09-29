package com.demo.reactiverogramming.repository;

import com.demo.reactiverogramming.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {


}
