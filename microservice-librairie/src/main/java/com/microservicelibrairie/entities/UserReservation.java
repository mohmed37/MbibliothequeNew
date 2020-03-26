package com.microservicelibrairie.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "userReservation")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserReservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long idClient;

    @Range(min=0, max=10)
    Integer nbLivre=0;


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserReservation{");
        sb.append("id=").append(id);
        sb.append(", idClient=").append(idClient);
        sb.append('}');
        return sb.toString();
    }


}
