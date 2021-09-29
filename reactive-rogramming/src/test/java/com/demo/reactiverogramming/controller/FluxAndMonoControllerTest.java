package com.demo.reactiverogramming.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@DirtiesContext
@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void flux_approach1(){
        Flux<Integer> integerFlux = webTestClient.get().uri("/flux")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(Integer.class)
                    .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void flux_approach2(){
        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    public void flux_approach3(){

        List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4);

        EntityExchangeResult<List<Integer>> entityExchangeResult = webTestClient
                .get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        Assert.assertEquals(expectedIntegerList, entityExchangeResult.getResponseBody());
    }

    @Test
    public void flux_approach4(){

        List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4);

        webTestClient
                .get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((response) -> {
                    Assert.assertEquals(expectedIntegerList, response.getResponseBody());
                });
    }
    @Test
    public void fluxStream(){

        Flux<Long> longStreamFlux = webTestClient.get().uri("/fluxStream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        // when we have an endpoint that return n results we can test it with a few results and then cancel the request.
        StepVerifier.create(longStreamFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .thenCancel()
                .verify();
    }

    @Test
    public void mono(){
        Integer expectedValue = 1;

        webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((response) -> {
                    Assert.assertEquals(expectedValue, response.getResponseBody());
                });
    }
}
