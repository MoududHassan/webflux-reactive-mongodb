package com.example.movieinfoservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

    @GetMapping("/flux")
    public Flux<Integer> getFlux(){
        return Flux.just(1,2,3);
    }

    @GetMapping("/mono")
    public Mono<String> getMono(){
        return Mono.just("Hello world.");
    }
}
