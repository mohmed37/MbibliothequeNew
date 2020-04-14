package com.microservicelibrairie.entities;

import lombok.*;
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
@ToString

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
