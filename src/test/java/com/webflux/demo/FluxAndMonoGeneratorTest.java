package com.webflux.demo;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoGeneratorTest {
    FluxAndMonoGenerator fluxAndMonoGenerator = new FluxAndMonoGenerator();

    @Test
    void namesFlux(){
        //given

        //when
        var namesFlux = fluxAndMonoGenerator.namesFlux();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("Moudud","Jannat","Rakib") //success
//                .expectNext("Moudud","Jannat","Rakib2") // fail
                .verifyComplete();
    }

    @Test
    void namesFluxMap(){
        //given
        int stringLength = 5;

        //when
        var namesFlux = fluxAndMonoGenerator.namesFluxMap(stringLength);

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("MOUDUD","JANNAT","RAKIB") //success
//                .expectNext("MOUDUD NEW","JANNAT NEW","RAKIB") //length filter fail
                .expectNext("MOUDUD NEW","JANNAT NEW") //length filter success
//                .expectNext("MOUDUD","JANNAT","Rakib") // fail
                .verifyComplete();
    }

    @Test
    void namesFluxImmutability(){
        //given

        //when
        var namesFlux = fluxAndMonoGenerator.namesFluxImmutability();

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("MOUDUD","JANNAT","RAKIB") //fail for immutable nature of flux
                .expectNext("Moudud","Jannat","Rakib") //success
                .verifyComplete();
    }


    @Test
    void namesFluxFlatMap(){
        //given
        int stringLength = 5;

        //when
        var namesFlux = fluxAndMonoGenerator.namesFluxFlatMap(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("M","O","U","D","U","D"," ","N","E","W","J","A","N","N","A","T"," ","N","E","W")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapDelay(){
        //given
        int stringLength = 5;

        //when
        var namesFlux = fluxAndMonoGenerator.namesFluxFlatMapDelay(stringLength);

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("M","O","U","D","U","D"," ","N","E","W","J","A","N","N","A","T"," ","N","E","W")
                .expectNextCount(20)
                .verifyComplete();
    }

    @Test
    void namesFluxConcatMapDelay(){
        //given
        int stringLength = 5;

        //when
        var namesFlux = fluxAndMonoGenerator.namesFluxConcateMapDelay(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("M","O","U","D","U","D"," ","N","E","W","J","A","N","N","A","T"," ","N","E","W")
                .verifyComplete();
    }


    @Test
    void namesMonoFlatMap(){
        //given
        int stringLength = 5;

        //when
        var namesFlux = fluxAndMonoGenerator.namesMonoFlatMap(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext(List.of("M","O","U","D","U","D"," ","N","E","W"))
//                .expectNext(List.of("J","A","N","N","A","T"," ","N","E","W"))
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapMany(){
        //given
        int stringLength = 5;

        //when
        var namesFlux = fluxAndMonoGenerator.namesMonoFlatMapMany(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("M","O","U","D","U","D"," ","N","E","W")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform(){
        //given
        int stringLength = 5;

        //when
        var namesFlux = fluxAndMonoGenerator.namesFluxTransform(stringLength);

        //then
        StepVerifier.create(namesFlux)
//                .expectNext("M","O","U","D","U","D"," ","N","E","W","J","A","N","N","A","T"," ","N","E","W")
                .expectNextCount(20)
                .verifyComplete();
    }


    //defaultifempty or ifempty

    @Test
    void defaultifemptyTest(){
        //given
        int stringLength = 11;

        //when
        var namesFlux = fluxAndMonoGenerator.namesFluxTransform(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    //concat flux
    @Test
    void concatFluxTest(){
        //given

        //when
        var namesFlux = fluxAndMonoGenerator.exploreConcat();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    //merge flux
    @Test
    void mergeFluxTest(){
        //given

        //when
        var namesFlux = fluxAndMonoGenerator.exploreMerge();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }
}
