package com.microservicelibrairie;

import static org.assertj.core.api.Assertions.assertThat;

import com.microservicelibrairie.dao.LibrairieRepository;
import com.microservicelibrairie.dao.LivreReserveAttenteDao;
import com.microservicelibrairie.entities.Genre;
import com.microservicelibrairie.entities.Librairie;
import com.microservicelibrairie.entities.LivreReserveAttente;
import com.microservicelibrairie.web.controller.LibrairieController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;
@RunWith(MockitoJUnitRunner.class)
public class LivreAttenteTest {
    private LibrairieController librairieController;
    private LivreReserveAttenteDao livreReserveAttenteDao;
    private LibrairieRepository librairieRepository;

    @Test
    public void getPreReservation() {
        this.livreReserveAttenteDao = Mockito.mock(LivreReserveAttenteDao.class);
        this.librairieController = new LibrairieController();
        Genre genre = new Genre.Builder().id(1).genre("Romans").build();

        Librairie livre = new Librairie.Builder().id(Long.valueOf(118)).auteur("Michele Lefevre").resume("Résumé")
                .nExemplaire(1).titre("La Casse ").genre(genre).prereserveMax(2).preeserver(0).build();

        Date today = new Date();

        LivreReserveAttente livreReserveAttente = new LivreReserveAttente.Builder().id(1L).dateRetour(today)
                .idClient(1L).mailEvoyel(true).dateMail(today).nlistAttente(1).librairie(livre).build();


//        Mockito.when(this.livreReserveAttenteDao.findById(1)).thenReturn(java.util.Optional.ofNullable(livreReserveAttente));


        assertThat(livreReserveAttente.getLibrairie().getId()).isEqualTo(118);
    }
}

