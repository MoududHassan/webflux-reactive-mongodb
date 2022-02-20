package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.domain.Movie;
import com.example.movieinfoservice.service.MovieInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

    private MovieInfoService movieInfoService;

    public MoviesInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Movie> addMovieInfo(@RequestBody Movie movie){
        return movieInfoService.addMovie(movie).log();
    }

    @GetMapping("/movieinfos")
    public Flux<Movie> getAllMovieInfo(){
        return movieInfoService.getAllMovie().log();
    }

    @GetMapping("/movieinfos/{id}")
    public Mono<Movie> getAllMovieInfo(@PathVariable String id){
        return movieInfoService.getMovieById(id).log();
    }

    @PutMapping("/movieinfos/{id}")
    public Mono<Movie> getUpdateMovieInfo(@PathVariable String id, @RequestBody Movie movie){
        return movieInfoService.updateMovieInfo(id, movie).log();
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> getUpdateMovieInfo(@PathVariable String id){
        return movieInfoService.deleteMovieInfo(id).log();
    }

}
