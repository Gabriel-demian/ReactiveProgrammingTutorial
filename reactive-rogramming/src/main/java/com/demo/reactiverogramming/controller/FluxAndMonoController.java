package com.demo.reactiverogramming.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    // this will return a collection
    @GetMapping("/flux")
    public Flux<Integer> getFlux(){

        return Flux.just(1,2,3,4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    // this will return n results
    //APPLICATION_STREAM_JSON_VALUE is deprecated but returns a string format / APPLICATION_NDJSON_VALUE download a file /
    @GetMapping(value = "/fluxStream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> getStream(){

        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }

    // mono can only return 1 element
    @GetMapping("/mono")
    public Mono<Integer> getMono(){

        return Mono.just(1)
                .log();
    }
}
