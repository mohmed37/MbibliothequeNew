package com.microservicelibrairie.entities;

import com.microservicelibrairie.dao.LivreReserveRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EmpruntAttenteTest {
    private List<ReserverLivre>reserveAttenteList;
    private LivreReserveRepository livreReserveRepository;

    @Before
    public void initReserveAttente(){
        this.livreReserveRepository = Mockito.mock(LivreReserveRepository.class);
        reserveAttenteList=new ArrayList<ReserverLivre>();

        Genre genre=new Genre.Builder().id(1).genre("Romans").build();
        Librairie livre = new Librairie.Builder().id(108L).preeserver(0).prereserveMax(4).genre(genre).titre("Test unitaire")
                .nExemplaire(2).resume("Mise en place des tests.").auteur("Moi").build();
        Date today = new Date();

        ReserverLivre reserverLivre =new ReserverLivre.Builder().id(1L).librairie(livre).nlistAttente(2)
                .mailEvoyel(false).idClient(1L).dateRetour(today).build();

        reserveAttenteList.add(reserverLivre);

        Mockito.when(livreReserveRepository.findAll()).thenReturn(reserveAttenteList);
        Mockito.when(livreReserveRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(reserverLivre));

    }

    @Test
    public void getListLivres_notNull() {
        assertThat(livreReserveRepository.findAll().isEmpty()).isNotNull();
        assertThat(livreReserveRepository.findAll().size()).isEqualTo(1);

    }
    @Test
    public void getById_whenReserveLivreExist(){
        assertThat(livreReserveRepository.findById(1L).isPresent()).isEqualTo(true);
    }

    @Test
    public void getById_whenReserveLivreNotExist(){
        assertThat(livreReserveRepository.findById(2L).isPresent()).isEqualTo(false);
    }

    @Test
    public void getId(){
        assertThat(livreReserveRepository.findAll().get(0).getId()).isEqualTo(1);
        livreReserveRepository.findAll().get(0).setId(2L);
        assertThat(livreReserveRepository.findAll().get(0).getId()).isEqualTo(2);
    }

    @Test
    public void getListeAttente(){
        assertThat(livreReserveRepository.findById(1L).get().getNlistAttente()).isEqualTo(2);
        livreReserveRepository.findById(1L).get().setNlistAttente(1);
        assertThat(livreReserveRepository.findById(1L).get().getNlistAttente()).isEqualTo(1);
    }
    @Test
    public void getMailEnvoiye(){
        assertThat(livreReserveRepository.findById(1L).get().getMailEnvoye()).isEqualTo(false);
        livreReserveRepository.findById(1L).get().setMailEnvoye(true);
        assertThat(livreReserveRepository.findById(1L).get().getMailEnvoye()).isEqualTo(true);
    }

    @Test
    public void getDateRetour(){
        Date today = new Date();
        assertThat(livreReserveRepository.findById(1L).get().getDateRetour()).isNotNull();
        livreReserveRepository.findById(1L).get().setDateRetour(today);
        assertThat(livreReserveRepository.findById(1L).get().getDateRetour()).isEqualTo(today);
    }

    @Test
    public void getDateMail(){
        Date today = new Date();
        assertThat(livreReserveRepository.findById(1L).get().getDateMail()).isNull();
        livreReserveRepository.findById(1L).get().setDateMail(today);
        assertThat(livreReserveRepository.findById(1L).get().getDateMail()).isEqualTo(today);
    }

    @Test
    public void getIdClient(){
        assertThat(livreReserveRepository.findById(1L).get().getIdClient()).isEqualTo(1);
        livreReserveRepository.findById(1L).get().setIdClient(2L);
        assertThat(livreReserveRepository.findById(1L).get().getIdClient()).isEqualTo(2);
    }
    @Test
    public void getlIvre(){
        assertThat(livreReserveRepository.findById(1L).get().getLibrairie().getPrereserve()).isEqualTo(0);
        livreReserveRepository.findById(1L).get().getLibrairie().setPrereserve(1);
        assertThat(livreReserveRepository.findById(1L).get().getLibrairie().getPrereserve()).isEqualTo(1);
    }


    @After
    public void undefCompteComptable() {
        reserveAttenteList.clear();

    }

}
