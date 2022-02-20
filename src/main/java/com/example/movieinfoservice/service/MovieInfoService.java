package com.example.movieinfoservice.service;

import ch.qos.logback.classic.Logger;
import com.example.movieinfoservice.domain.Movie;
import com.example.movieinfoservice.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    private MovieInfoRepository movieInfoRepository;

    public MovieInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<Movie> addMovie(Movie movie) {
        return movieInfoRepository.save(movie);
    }

    public Flux<Movie> getAllMovie() {
        return movieInfoRepository.findAll();
    }

    public Mono<Movie> getMovieById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<Movie> updateMovieInfo(String id, Movie updatedMovieInfo) {
        return movieInfoRepository.findById(id)
                .flatMap(movieInfo -> {
                    movieInfo.setName(updatedMovieInfo.getName());
                    movieInfo.setCast(updatedMovieInfo.getCast());
                    movieInfo.setRelease_date(updatedMovieInfo.getRelease_date());
                    movieInfo.setYear(updatedMovieInfo.getYear());
                    return movieInfoRepository.save(movieInfo);

                });
    }

    public Mono<Void> deleteMovieInfo(String id) {
        return movieInfoRepository.deleteById(id);
    }
}
