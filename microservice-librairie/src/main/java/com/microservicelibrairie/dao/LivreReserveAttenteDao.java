package com.microservicelibrairie.dao;

import com.microservicelibrairie.entities.LivreReserveAttente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivreReserveAttenteDao extends JpaRepository<LivreReserveAttente,Long> {
    List<LivreReserveAttente> findByIdClient(long id);
    List<LivreReserveAttente>findByLibrairie_Id(long id);
    List<LivreReserveAttente>findByIdClientAndLibrairie_Id(long idClient,long idLivre);
}
