package com.microservicelibrairie.dao;

import com.microservicelibrairie.entities.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmprunterRepository extends JpaRepository<Emprunt,Long> {
List<Emprunt>findByIdClient(long id);
List<Emprunt>findByLibrairie_Id(long id);

}
