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

    public UserReservation(Builder builder) {
        this.id=builder.id;
        this.idClient=builder.idClient;
        this.nbLivre=builder.nbLivre;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserReservation{");
        sb.append("id=").append(id);
        sb.append(", idClient=").append(idClient);
        sb.append('}');
        return sb.toString();
    }

    public static class Builder{
        Long id;
        Long idClient;
        Integer nbLivre=0;

        public UserReservation.Builder id(Long id){
            this.id=id;
            return this;}
        public UserReservation.Builder idClient(Long idClient){
            this.idClient=idClient;
            return this;}
        public UserReservation.Builder nbLivre(Integer nbLivre){
            this.nbLivre=nbLivre;
            return this;}

        public UserReservation build(){
            return new UserReservation(this);
        }


    }
}
