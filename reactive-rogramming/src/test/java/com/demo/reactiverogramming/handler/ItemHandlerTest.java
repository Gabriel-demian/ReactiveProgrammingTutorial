package com.demo.reactiverogramming.handler;

import com.demo.reactiverogramming.document.Item;
import com.demo.reactiverogramming.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static com.demo.reactiverogramming.constants.ItemConstant.ITEM_FUNCTIONAL_END_POINT_V1;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext // to use embedded databases
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public List<Item> data(){
        return Arrays.asList(
                new Item(null, "Samsung TV", 399.99),
                new Item(null, "LG TV", 259.00),
                new Item(null, "Apple Watch", 300.20),
                new Item(null, "Beats HeadPhones", 59.99),
                new Item("12345", "Samsung Watch", 189.61)
        );
    }

    @Before
    public void setUp(){

        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .doOnNext((item -> {
                    System.out.println("Inserted item is : " + item);
                }))
                .blockLast();
    }

    @Test
    public void getAllItemsTest(){
        webTestClient.get().uri(ITEM_FUNCTIONAL_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(5);
    }

}
