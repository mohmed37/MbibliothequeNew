package com.client.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReserverLivreBean {

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


    public String getDateCreatedString(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }


}
