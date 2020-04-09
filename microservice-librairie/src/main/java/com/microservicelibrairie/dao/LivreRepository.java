package com.microservicelibrairie.dao;

import com.microservicelibrairie.entities.LivreReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivreRepository  extends JpaRepository<LivreReserve,Long> {
List<LivreReserve>findByIdClient(long id);
List<LivreReserve>findById(long id);
List<LivreReserve>findByLibrairie_Id(long id);

}
