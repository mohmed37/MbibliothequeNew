package com.microservicelibrairie.entities;

import com.microservicelibrairie.dao.LivreReserveAttenteDao;
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
public class LivreReserveAttenteTest {
    private List<LivreReserveAttente>reserveAttenteList;
    private LivreReserveAttenteDao livreReserveAttenteDao;

    @Before
    public void initReserveAttente(){
        this.livreReserveAttenteDao= Mockito.mock(LivreReserveAttenteDao.class);
        reserveAttenteList=new ArrayList<LivreReserveAttente>();

        Genre genre=new Genre.Builder().id(1).genre("Romans").build();
        Librairie livre = new Librairie.Builder().id(108L).preeserver(0).prereserveMax(4).genre(genre).titre("Test unitaire")
                .nExemplaire(2).resume("Mise en place des tests.").auteur("Moi").build();
        Date today = new Date();

        LivreReserveAttente livreReserveAttente=new LivreReserveAttente.Builder().id(1L).librairie(livre).nlistAttente(2)
                .mailEvoyel(false).idClient(1L).dateRetour(today).build();

        reserveAttenteList.add(livreReserveAttente);

        Mockito.when(livreReserveAttenteDao.findAll()).thenReturn(reserveAttenteList);
        Mockito.when(livreReserveAttenteDao.findById(1L)).thenReturn(java.util.Optional.ofNullable(livreReserveAttente));

    }

    @Test
    public void getListLivres_notNull() {
        assertThat(livreReserveAttenteDao.findAll().isEmpty()).isNotNull();
        assertThat(livreReserveAttenteDao.findAll().size()).isEqualTo(1);

    }
    @Test
    public void getById_whenReserveLivreExist(){
        assertThat(livreReserveAttenteDao.findById(1L).isPresent()).isEqualTo(true);
    }

    @Test
    public void getById_whenReserveLivreNotExist(){
        assertThat(livreReserveAttenteDao.findById(2L).isPresent()).isEqualTo(false);
    }

    @Test
    public void getId(){
        assertThat(livreReserveAttenteDao.findAll().get(0).getId()).isEqualTo(1);
        livreReserveAttenteDao.findAll().get(0).setId(2L);
        assertThat(livreReserveAttenteDao.findAll().get(0).getId()).isEqualTo(2);
    }

    @Test
    public void getListeAttente(){
        assertThat(livreReserveAttenteDao.findById(1L).get().getNlistAttente()).isEqualTo(2);
        livreReserveAttenteDao.findById(1L).get().setNlistAttente(1);
        assertThat(livreReserveAttenteDao.findById(1L).get().getNlistAttente()).isEqualTo(1);
    }
    @Test
    public void getMailEnvoiye(){
        assertThat(livreReserveAttenteDao.findById(1L).get().getMailEnvoye()).isEqualTo(false);
        livreReserveAttenteDao.findById(1L).get().setMailEnvoye(true);
        assertThat(livreReserveAttenteDao.findById(1L).get().getMailEnvoye()).isEqualTo(true);
    }

    @Test
    public void getDateRetour(){
        Date today = new Date();
        assertThat(livreReserveAttenteDao.findById(1L).get().getDateRetour()).isNotNull();
        livreReserveAttenteDao.findById(1L).get().setDateRetour(today);
        assertThat(livreReserveAttenteDao.findById(1L).get().getDateRetour()).isEqualTo(today);
    }

    @Test
    public void getDateMail(){
        Date today = new Date();
        assertThat(livreReserveAttenteDao.findById(1L).get().getDateMail()).isNull();
        livreReserveAttenteDao.findById(1L).get().setDateMail(today);
        assertThat(livreReserveAttenteDao.findById(1L).get().getDateMail()).isEqualTo(today);
    }

    @Test
    public void getIdClient(){
        assertThat(livreReserveAttenteDao.findById(1L).get().getIdClient()).isEqualTo(1);
        livreReserveAttenteDao.findById(1L).get().setIdClient(2L);
        assertThat(livreReserveAttenteDao.findById(1L).get().getIdClient()).isEqualTo(2);
    }
    @Test
    public void getlIvre(){
        assertThat(livreReserveAttenteDao.findById(1L).get().getLibrairie().getPrereserve()).isEqualTo(0);
        livreReserveAttenteDao.findById(1L).get().getLibrairie().setPrereserve(1);
        assertThat(livreReserveAttenteDao.findById(1L).get().getLibrairie().getPrereserve()).isEqualTo(1);
    }


    @After
    public void undefCompteComptable() {
        reserveAttenteList.clear();

    }

}
