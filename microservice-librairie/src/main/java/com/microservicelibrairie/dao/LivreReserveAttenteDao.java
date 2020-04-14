package com.microservicelibrairie.dao;

import com.microservicelibrairie.entities.LivreReserve;
import com.microservicelibrairie.entities.LivreReserveAttente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivreReserveAttenteDao extends JpaRepository<LivreReserveAttente,Long> {
    List<LivreReserveAttente> findByIdClient(long id);
    List<LivreReserveAttente>findByLibrairie_Id(long id);
}
