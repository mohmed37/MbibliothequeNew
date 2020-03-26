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
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

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
    String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    Genre genre;

}
