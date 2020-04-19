package com.microservicelibrairie.dao;

import com.microservicelibrairie.entities.ReserverLivre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivreReserveRepository extends JpaRepository<ReserverLivre,Long> {
    List<ReserverLivre> findByIdClient(long id);
    List<ReserverLivre>findByLibrairie_Id(long id);
    List<ReserverLivre>findByIdClientAndLibrairie_Id(long idClient, long idLivre);
}
