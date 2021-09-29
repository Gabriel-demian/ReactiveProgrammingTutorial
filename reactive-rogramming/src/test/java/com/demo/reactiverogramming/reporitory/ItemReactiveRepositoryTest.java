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
import reactor.core.publisher.Mono;
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
                .blockLast(); // wait until all files are saved.

    }

    @Test
    public void getAllItemsTest(){
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemByIdTest(){
        StepVerifier.create(itemReactiveRepository.findById("123"))
                .expectSubscription()
                .expectNextMatches((item) -> item.getDescription().equals("Samsung Watch"))
                .verifyComplete();
    }

    @Test
    public void findItemByDescriptionTest(){
        StepVerifier.create(itemReactiveRepository.findByDescription("Samsung Watch"))
                .expectSubscription()
                .expectNextMatches((item) -> item.getDescription().equals("Samsung Watch") && item.getId().equals("123"))
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findByDescription("Samsung Watch"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItemTest(){
        // cuando guardamos un item de manera no bloqueante obtenemos un Mono.
        Item item = new Item(null, "Google Home Mini", 20.00);
        Mono<Item> savedItem = itemReactiveRepository.save(item);

        StepVerifier.create(savedItem)
                .expectSubscription()
                .expectNextMatches(item1 -> item1.getId()!=null && item1.getDescription().equals("Google Home Mini") && item1.getPrice() == 20.00)
                .verifyComplete();
    }

    @Test
    public void updateItemTest(){

        double newPrice = 520.00;
        Mono<Item> updatedItem = itemReactiveRepository.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(newPrice); // setting the new price
                    return item;
                })
                .flatMap(item -> {
                    return itemReactiveRepository.save(item); // saving the item with the new price
                });

        StepVerifier.create(updatedItem)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice()==520.00)
                .verifyComplete();
    }

    @Test
    public void deleteItemByIdTest(){

        Mono<Void> deletedItem = itemReactiveRepository.findById("123") // Mono<Item>
                .map(Item::getId) // get id -> transform from one type to another type
                .flatMap((id) -> {
                    return itemReactiveRepository.deleteById(id);
                });

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("The new Item List: "))
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void deleteItemByIdTest2(){

        Mono<Void> deletedItem = itemReactiveRepository.findByDescription("LG TV")
                .map(Item::getId)
                .flatMap((id) -> {
                    return itemReactiveRepository.deleteById(id);
                });

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("The new Item List: "))
                .expectNextCount(4)
                .verifyComplete();
    }
}
