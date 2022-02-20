package com.example.movieinfoservice.repository;

import com.example.movieinfoservice.domain.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieInfoRepository extends ReactiveMongoRepository<Movie, String> {
}
