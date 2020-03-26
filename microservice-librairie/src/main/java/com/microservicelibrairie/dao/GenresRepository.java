package com.microservicelibrairie.dao;

import com.microservicelibrairie.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenresRepository extends JpaRepository<Genre,Integer> {
}
