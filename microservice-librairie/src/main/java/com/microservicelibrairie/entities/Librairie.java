package com.microservicelibrairie.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "librairie")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@NoArgsConstructor


public class Librairie implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Column(name = "titre",length = 30)
    String titre;

    @NotNull
    String auteur;

    @NotNull
    String resume;

    @NotNull
    @Range(min=0, max=10)
    Integer nExemplaire;

    @NotNull
    @Range(min=0, max=(20))
    Integer prereserveMax;

    @NotNull
    @Range(min=0, max=(20))
    Integer prereserve;



    String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    Genre genre;

    private Librairie(Builder builder) {
        this.id=builder.id;
        this.titre=builder.titre;
        this.auteur=builder.auteur;
        this.resume=builder.resume;
        this.nExemplaire=builder.nExemplaire;
        this.prereserveMax=builder.prereserveMax;
        this.prereserve=builder.preeserver;
        this.genre=builder.genre;
    }

    public static class Builder{
        Long id;
        String titre;
        String auteur;
        String resume;
        Integer nExemplaire;
        Integer prereserveMax;
        Integer preeserver;
        Genre genre;

        public Librairie.Builder id(Long id){
            this.id= id;
            return this;}
        public Librairie.Builder titre(String titre){
            this.titre= titre;
            return this;}
        public Librairie.Builder auteur(String auteur){
            this.auteur= auteur;
            return this;}
        public Librairie.Builder nExemplaire(Integer nExemplaire){
            this.nExemplaire= nExemplaire;
            return this;}
        public Librairie.Builder resume(String resume){
            this.resume= resume;
            return this;}
        public Librairie.Builder prereserveMax(Integer prereserveMax){
            this.prereserveMax= prereserveMax;
            return this;}
        public Librairie.Builder preeserver(Integer preeserver){
            this.preeserver= preeserver;
            return this;}
        public Librairie.Builder genre(Genre genre){
            this.genre= genre;
            return this;}
        public Librairie build(){
            return new Librairie(this);}

    }
}
