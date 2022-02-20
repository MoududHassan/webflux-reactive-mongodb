package com.example.movieinfoservice;


import com.example.movieinfoservice.domain.Movie;
import com.example.movieinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class MoviesInfoControllerIntgrTest {

    private String MOVIES_INFO_URL = "/v1/movieinfos";

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp(){
        var movieInfos = List.of(
                new Movie(null, "Batman begins", 2005, List.of("Cristian Bale", "Michel Chale"), LocalDate.parse("2005-06-15")),
                new Movie(null, "The Dark Knights", 2008, List.of("Cristian Bale", "HealthLeadger"), LocalDate.parse("2008-08-19")),
                new Movie("abc", "Dark Knights Rises", 2012, List.of("Cristian Bale", "Michel Chale"), LocalDate.parse("2012-09-13"))
        );

        movieInfoRepository.saveAll(movieInfos).blockLast();
    }

    @AfterEach
    void tearAll(){
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void addMovieInfo() {

        var movie = new Movie(null, "Batman begins1", 2009, List.of("Cristian Bale", "Michel Chale"), LocalDate.parse("2009-06-15"));

        webTestClient
                .post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    var savedMovie = movieEntityExchangeResult.getResponseBody();
                    assertNotNull(savedMovie);
                    assertNotNull(savedMovie.getMovieInfoId());
                });
    }

    @Test
    void findAll(){
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Movie.class)
                .hasSize(3);
    }

    @Test
    void findById(){
        var movieInfoId= "abc";
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    var savedMovie = movieEntityExchangeResult.getResponseBody();
                    assertEquals(movieInfoId,savedMovie.getMovieInfoId());
                    assertNotNull(savedMovie);
                });
    }

    @Test
    void updateMovieInfo() {

        var movieInfoId= "abc";
        var movie = new Movie(null, "Dark Knights Rises 1", 2012, List.of("Cristian Bale", "Michel Chale"), LocalDate.parse("2012-09-13"));

        webTestClient
                .put()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    var updatedMovie = movieEntityExchangeResult.getResponseBody();
                    assertNotNull(updatedMovie);
                    assertNotNull(updatedMovie.getMovieInfoId());
                    assertEquals("Dark Knights Rises 1",updatedMovie.getName());

                });
    }

    @Test
    void deleteById(){
        var movieInfoId= "abc";
        webTestClient
                .delete()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .consumeWith(movieEntityExchangeResult -> {
                    var deleteBody = movieEntityExchangeResult.getResponseBody();
                    assertEquals(null,deleteBody);
                });
    }
}
