package com.example.movieinfoservice;

import com.example.movieinfoservice.domain.Movie;
import com.example.movieinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@ActiveProfiles("test")
public class MovieInfoRepositoryIntegrationTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

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
    void findAll(){
        var allMovie = movieInfoRepository.findAll().log();

        StepVerifier.create(allMovie)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById(){
        var movieFindMono = movieInfoRepository.findById("abc").log();

        StepVerifier.create(movieFindMono)
                .assertNext(movie -> {
                    assertEquals("Dark Knights Rises", movie.getName());
                })
                .verifyComplete();
    }

    @Test
    void save(){
        var movie1 = new Movie(null, "Batman begins1", 2005, List.of("Cristian Bale", "Michel Chale"), LocalDate.parse("2005-06-15"));
        var movieSAveMono = movieInfoRepository.save(movie1).log();

        StepVerifier.create(movieSAveMono)
                .assertNext(movie -> {
                    assertNotNull(movie.getMovieInfoId());
                    assertEquals("Batman begins1", movie.getName());
                })
                .verifyComplete();
    }
}
