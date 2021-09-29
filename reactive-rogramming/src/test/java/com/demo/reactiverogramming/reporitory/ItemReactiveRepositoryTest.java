package com.demo.reactiverogramming.reporitory;

import com.demo.reactiverogramming.document.Item;
import com.demo.reactiverogramming.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(
            new Item(null, "Samsung TV", 400.00),
            new Item(null, "LG TV", 420.00),
            new Item(null, "Apple Watch", 299.99),
            new Item(null, "Beats Headphones", 149.50),
            new Item("123", "Samsung Watch", 159.20)
    );

    @Before
    public void setUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext((item -> {
                    System.out.println("Inserted Item is: " + item);
                }))
                .blockLast(); // espera a que todos los archivos esten guardados.

    }

    @Test
    public void getAllItems(){
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemById(){
        StepVerifier.create(itemReactiveRepository.findById("123"))
                .expectSubscription()
                .expectNextMatches((item) -> item.getDescription().equals("Samsung Watch"))
                .verifyComplete();
    }

    @Test
    public void findItemByDescription(){
        StepVerifier.create(itemReactiveRepository.findByDescription("Samsung Watch"))
                .expectSubscription()
                .expectNextMatches((item) -> item.getDescription().equals("Samsung Watch") && item.getId().equals("123"))
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findByDescription("Samsung Watch"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }



}