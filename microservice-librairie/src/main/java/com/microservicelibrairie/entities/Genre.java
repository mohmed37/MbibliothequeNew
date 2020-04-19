package com.microservicelibrairie.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "genre")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "genre")
    String genre;


    private Genre(Builder builder) {
        this.id=builder.id;
        this.genre=builder.genre;
    }

    public static class Builder{
        int id;
        String genre;
        public Genre.Builder id(Integer id){
            this.id= id;
            return this;}
        public Genre.Builder genre(String genre){
            this.genre=genre;
            return this;}
        public Genre build(){
            return new Genre(this);
        }
    }
}
