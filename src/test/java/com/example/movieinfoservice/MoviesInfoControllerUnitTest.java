package com.example.movieinfoservice;

import com.example.movieinfoservice.controller.MoviesInfoController;
import com.example.movieinfoservice.domain.Movie;
import com.example.movieinfoservice.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;


@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
public class MoviesInfoControllerUnitTest {

    private String MOVIES_INFO_URL = "/v1/movieinfos";


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovieInfoService movieInfoServiceMock;

    @Test
    void getAllMovieInfo(){
        var movieInfos = List.of(
                new Movie(null, "Batman begins", 2005, List.of("Cristian Bale", "Michel Chale"), LocalDate.parse("2005-06-15")),
                new Movie(null, "The Dark Knights", 2008, List.of("Cristian Bale", "HealthLeadger"), LocalDate.parse("2008-08-19")),
                new Movie("abc", "Dark Knights Rises", 2012, List.of("Cristian Bale", "Michel Chale"), LocalDate.parse("2012-09-13"))
        );
        when(movieInfoServiceMock.getAllMovie()).thenReturn(Flux.fromIterable(movieInfos));

        webTestClient
                .get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Movie.class)
                .hasSize(3);
    }

}
