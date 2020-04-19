package com.microservicelibrairie.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "LivreReserve")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@ToString

public class Emprunt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    Long idClient;

    Boolean prolongation=false;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateDeb;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateFin;

    @ManyToOne(fetch = FetchType.EAGER)
    UserReservation userReservation;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livre_id")
    Librairie librairie;

    private Emprunt(Builder builder) {
        this.id=builder.id;
        this.idClient=builder.idClient;
        this.prolongation=builder.prolongation;
        this.dateDeb=builder.dateDeb;
        this.dateFin=builder.dateFin;
        this.userReservation=builder.userReservation;
        this.librairie=builder.librairie;
    }

    public static class Builder{

        Long id;
        Long idClient;
        Boolean prolongation=false;
        Date dateDeb;
        Date dateFin;
        UserReservation userReservation;
        Librairie librairie;

        public Emprunt.Builder id(Long id){
            this.id= id;
            return this;}
        public Emprunt.Builder idClient(Long idClient){
            this.idClient= idClient;
            return this;}
        public Emprunt.Builder prolongation(Boolean prolongation){
            this.prolongation= prolongation;
            return this;}
        public Emprunt.Builder dateFin(Date dateFin){
            this.dateFin= dateFin;
            return this;}
        public Emprunt.Builder dateDeb(Date dateDeb){
            this.dateDeb= dateDeb;
            return this;}
        public Emprunt.Builder userReservation(UserReservation userReservation){
            this.userReservation= userReservation;
            return this;}
        public Emprunt.Builder librairie(Librairie librairie){
            this.librairie= librairie;
            return this;}


        public Emprunt build(){
            return new Emprunt(this);
        }


    }

}
