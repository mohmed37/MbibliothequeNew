package com.microservicelibrairie.dao;

import com.microservicelibrairie.entities.Librairie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface LibrairieRepository extends JpaRepository<Librairie,Long> {
    List<Librairie>findByGenre_Genre(String genre);
    Page<Librairie>findByAuteurContainingIgnoreCaseAndTitreContainingIgnoreCase(String motClefAuteur, String motClefTitre,Pageable pageable);

}
