package com.client.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class LibrairieBean {
    Long id;
    String titre;
    String auteur;
    String resume;
    Integer nExemplaire;
    String photo;
    Integer prereserveMax;
    Integer prereserve;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    GenreBean genre;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LibrairieBean{");
        sb.append("id=").append(id);
        sb.append(", titre='").append(titre).append('\'');
        sb.append(", auteur='").append(auteur).append('\'');
        sb.append(", resume='").append(resume).append('\'');
        sb.append(", nExemplaire=").append(nExemplaire);
        sb.append(", photo='").append(photo).append('\'');
        sb.append(", prereserveMax=").append(prereserveMax);
        sb.append(", prereserve=").append(prereserve);
        sb.append(", genre=").append(genre);
        sb.append('}');
        return sb.toString();
    }

    public String getDateCreatedString(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(date);
    }
}
