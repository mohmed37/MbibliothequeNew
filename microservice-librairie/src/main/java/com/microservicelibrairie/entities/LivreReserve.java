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
@AllArgsConstructor
@ToString

public class LivreReserve implements Serializable {

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
}
