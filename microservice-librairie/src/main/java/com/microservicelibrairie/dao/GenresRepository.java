package com.microservicelibrairie.dao;

import com.microservicelibrairie.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenresRepository extends JpaRepository<Genre,Integer> {
    List<Genre>findByGenre(String genre);
    Optional<Genre> findById(Integer id);
}
