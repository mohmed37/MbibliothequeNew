package com.microervicebatch.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class LivreReserveBean {

    Long id;
    Long idClient;
    Boolean prolongation=false;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateDeb;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateFin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livre_id")
    LibrairieBean librairie;

    @ManyToOne(fetch = FetchType.EAGER)
    UserReservationBean userReservation;

    public String getDateCreatedString(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }


    public String afficher() {
        final StringBuffer sb = new StringBuffer("");
        sb.append("Titre: ").append(librairie.getTitre());
        sb.append(", Auteur: ").append(librairie.getAuteur());
        sb.append(", A rendre avant le: ").append(getDateCreatedString(dateFin));
        sb.append('\n');
        sb.append("=====================================================\n");
        return sb.toString();
    }

}
