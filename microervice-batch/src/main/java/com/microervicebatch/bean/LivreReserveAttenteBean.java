package com.microervicebatch.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class LivreReserveAttenteBean {

     Long id;
     Long idClient;
    Integer nlistAttente;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateRetour;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateMail;
    Boolean mailEnvoye;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livre_id")
    LibrairieBean librairie;

}
