package com.microservicelibrairie.dao;

import com.microservicelibrairie.entities.LivreReserve;
import com.microservicelibrairie.entities.LivreReserveAttente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivreReserveAttenteDao extends JpaRepository<LivreReserveAttente,Integer> {
    Optional<LivreReserveAttente> findById(Long id);
    List<LivreReserveAttente> findByIdClient(long id);
    Optional<LivreReserveAttente>findByLibrairie_Id(long id);

}
