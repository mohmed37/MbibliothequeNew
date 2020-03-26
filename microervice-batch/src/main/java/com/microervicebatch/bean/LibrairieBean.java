package com.microervicebatch.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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

public class LibrairieBean {
    Long id;
    String titre;
    String auteur;
    String resume;
    Integer nExemplaire;
    String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    GenreBean genre;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LibrairieBean{");
        sb.append("id=").append(id);
        sb.append(", genre='").append(genre).append('\'');
        sb.append(", titre='").append(titre).append('\'');
        sb.append(", auteur='").append(auteur).append('\'');
        sb.append(", resume='").append(resume).append('\'');
        sb.append(", nExemplaire=").append(nExemplaire);
        sb.append(", photo='").append(photo).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
