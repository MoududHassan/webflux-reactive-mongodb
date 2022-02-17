package com.webflux.demo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class FluxAndMonoGenerator {

    public Flux<String> namesFlux(){
        return Flux.fromIterable(List.of("Moudud","Jannat","Rakib")).log();
    }

    public Mono<String> nameMono(){
        return Mono.just("Rakib");
    }

    public Flux<String> namesFluxMap(int stringLength){
        return Flux.fromIterable(List.of("Moudud New","Jannat New","Rakib"))
//                .map(String::toUpperCase)
                .map(name -> name.toUpperCase())
                .filter(name -> name.length() > stringLength)
                .log();
    }

    public Flux<String> namesFluxFlatMap(int stringLength){
        return Flux.fromIterable(List.of("Moudud New","Jannat New","Rakib"))
                .map(name -> name.toUpperCase())
                .filter(name -> name.length() > stringLength)
                .flatMap(s -> splitString(s))
                .log();
    }

    public Flux<String> splitString(String name){
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> namesFluxFlatMapDelay(int stringLength){
        return Flux.fromIterable(List.of("Moudud New","Jannat New","Rakib"))
                .map(name -> name.toUpperCase())
                .filter(name -> name.length() > stringLength)
                .flatMap(s -> splitStringDelay(s))
                .log();
    }

    public Flux<String> splitStringDelay(String name){
        var charArray = name.split("");
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(1000));
    }

    public Flux<String> namesFluxConcateMapDelay(int stringLength){
        return Flux.fromIterable(List.of("Moudud New","Jannat New","Rakib"))
                .map(name -> name.toUpperCase())
                .filter(name -> name.length() > stringLength)
                .concatMap(s -> splitStringDelay(s))
                .log();
    }

    //mono to mono of list

    public Mono<List<String>> namesMonoFlatMap(int stringLength){
        return Mono.just("Moudud New")
                .map(name -> name.toUpperCase())
                .filter(name -> name.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Mono<List<String>> splitStringMono(String name){
        var charArray = name.split("");
        return Mono.just(List.of(charArray));
    }


    //mono to flux
    public Flux<String> namesMonoFlatMapMany(int stringLength){
        return Mono.just("Moudud New")
                .map(name -> name.toUpperCase())
                .filter(name -> name.length() > stringLength)
                .flatMapMany(s -> splitString(s))
                .log();
    }

    public Flux<String> namesFluxImmutability(){
        var nameFlux = Flux.fromIterable(List.of("Moudud","Jannat","Rakib"));
        nameFlux.map(String::toUpperCase);
        return nameFlux;
    }


    //transform
    public Flux<String> namesFluxTransform(int stringLength){

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        return Flux.fromIterable(List.of("Moudud New","Jannat New","Rakib"))
                .transform(filterMap)
                .flatMap(s -> splitString(s))
                .defaultIfEmpty("default")
                .log();
    }


    //concat and concat with (flux and flux + mono and mono = output flux)

    public Flux<String> exploreConcat(){
        var firstFlux = Flux.just("A","B","C");
        var secondFlux = Flux.just("D","E","F");
        return Flux.concat(firstFlux, secondFlux).log();
    }

    public Flux<String> exploreConcatWith(){
        var firstFlux = Flux.just("A","B","C");
        var secondFlux = Flux.just("D","E","F");
        return firstFlux.concatWith(secondFlux).log();
    }

    public Flux<String> exploreConcatWithMono(){
        var firstMono = Mono.just("A");
        var secondMono = Mono.just("D");
        return firstMono.concatWith(secondMono).log();
    }

    //merge and merge with (flux and flux + mono and mono = output flux)

    public Flux<String> exploreMerge(){
        var firstFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(100));
        var secondFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(125));
        return Flux.merge(firstFlux, secondFlux).log();
    }

    public Flux<String> exploreMergeWith(){
        var firstFlux = Flux.just("A","B","C").delayElements(Duration.ofMillis(100));
        var secondFlux = Flux.just("D","E","F").delayElements(Duration.ofMillis(125));
        return firstFlux.concatWith(secondFlux).log();
    }

    public Flux<String> exploreMergeWithMono(){
        var firstMono = Mono.just("A");
        var secondMono = Mono.just("D");
        return firstMono.concatWith(secondMono).log();
    }

    public static void main(String[] args) {
        FluxAndMonoGenerator fluxAndMonoGenerator = new FluxAndMonoGenerator();
//        fluxAndMonoGenerator.namesFlux()
//                .subscribe( name -> {
//                    System.out.println(name);
//                });
//
//        fluxAndMonoGenerator.nameMono()
//                .subscribe(name -> {
//                    System.out.println("Mono name: "+name);
//                });

        fluxAndMonoGenerator.namesFluxMap(5)
                .subscribe( name -> {
                    System.out.println(name);
                });
    }
}
