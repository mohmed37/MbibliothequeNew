package com.microservicelibrairie.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "LivreReserveAttente")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LivreReserveAttente  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    Long idClient;

    @NotNull
    Integer nlistAttente;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateDemande;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateMail;


    Boolean mailEnvoye;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livre_id")
    Librairie librairie;

}
