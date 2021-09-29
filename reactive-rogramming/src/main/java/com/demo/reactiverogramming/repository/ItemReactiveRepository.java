package com.demo.reactiverogramming.repository;

import com.demo.reactiverogramming.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

    Mono<Item> findByDescription(String description); // since it is returning only one result we don't need a Flux. With Mono is enough

}
