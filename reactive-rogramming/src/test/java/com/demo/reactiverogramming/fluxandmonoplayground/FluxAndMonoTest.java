package com.demo.reactiverogramming.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DirtiesContext
public class FluxAndMonoTest {

    @Test
    public void fluxTest(){

        // .log(); will print all the logs of the method.
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                //.concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just(" After Error"))
                .log();

        // We need to subscribe to be able to read the data
        // (e)-> System.err.println(e)  will catch the errors.
        stringFlux
                .subscribe(System.out::println,
                        (e)-> System.err.println("Exception is " + e)
                , () -> System.out.println("Completed"));
    }

    @Test
    public void fluxTestElements_WithoutError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring").log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }

    @Test
    public void fluxTestElements_WithError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fluxTestElements_WithErrorMessage(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .expectErrorMessage("Exception Occurred")
                .verify();
    }

    @Test
    public void fluxTestElementsCount_WithError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fluxTestElements_WithError1(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring","Spring Boot","Reactive Spring")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void monoTest(){

        Mono<String> stringMono = Mono.just("Spring");

        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTest_Error(){

        StepVerifier.create(Mono.error(new RuntimeException("Error Ocurred")).log())
                .expectError(RuntimeException.class)
                .verify();

    }
}
