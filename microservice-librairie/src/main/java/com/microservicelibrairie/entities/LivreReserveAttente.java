package com.microservicelibrairie.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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
    Date dateRetour;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateMail;


    Boolean mailEnvoye;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livre_id")
    Librairie librairie;

    private LivreReserveAttente(Builder builder) {
        this.id=builder.id;
        this.idClient=builder.idClient;
        this.nlistAttente=builder.nlistAttente;
        this.dateRetour=builder.dateRetour;
        this.mailEnvoye=builder.mailEnvoye;
        this.librairie=builder.librairie;
    }


    public static class Builder{
        Long id;
        Long idClient;
        Integer nlistAttente;
        Date dateRetour;
        Boolean mailEnvoye;
        Librairie librairie;

        public LivreReserveAttente.Builder id(Long id){
            this.id= id;return this;}
        public LivreReserveAttente.Builder idClient(Long idClient){
            this.idClient= idClient;return this;}
        public LivreReserveAttente.Builder nlistAttente(Integer nlistAttente){
            this.nlistAttente= nlistAttente;return this;}
        public LivreReserveAttente.Builder dateRetour(Date dateRetour){
            this.dateRetour= dateRetour;return this;}
        public LivreReserveAttente.Builder mailEvoyel(Boolean mailEnvoye){
            this.mailEnvoye= mailEnvoye;return this;}
        public LivreReserveAttente.Builder librairie(Librairie librairie){
            this.librairie= librairie;return this;}
        public LivreReserveAttente build(){
            return new LivreReserveAttente(this);}


    }
}
