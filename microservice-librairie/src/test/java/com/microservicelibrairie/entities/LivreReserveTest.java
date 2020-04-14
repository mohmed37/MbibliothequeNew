package com.microservicelibrairie.entities;

import com.microservicelibrairie.dao.LivreRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LivreReserveTest {
    private List<LivreReserve>reserveList;
    private LivreRepository livreRepository;


    @Before
    public void initLivreReserve(){
        this.livreRepository= Mockito.mock(LivreRepository.class);

        reserveList=new ArrayList<LivreReserve>();

        Genre genre = new Genre.Builder().id(1).genre("Romans").build();

        Librairie livre = new Librairie.Builder().id(118L).auteur("Michele Lefevre").resume("Résumé")
                .nExemplaire(1).titre("La Casse ").genre(genre).prereserveMax(2).preeserver(0).build();
        Date today = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.MONTH,1);

        UserReservation userReservation = new UserReservation.Builder().id(12L).idClient(1L).nbLivre(1).build();

        LivreReserve livreReserve = new LivreReserve.Builder().id(20L).prolongation(false).librairie(livre).idClient(1L).
                dateDeb(today).dateFin(cal.getTime()).userReservation(userReservation).build();
        reserveList.add(livreReserve);
        Mockito.when(livreRepository.findAll()).thenReturn(reserveList);
        Mockito.when(livreRepository.findById(20L)).thenReturn(Optional.ofNullable(livreReserve));
    }

    @Test
    public void getListReserve_notNull() {
        assertThat(livreRepository.findAll().isEmpty()).isNotNull();
        assertThat(livreRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void getById_whenReserveLivreExist(){
        assertThat(livreRepository.findById(20L).isPresent()).isEqualTo(true);
    }

    @Test
    public void getById_whenReserveLivreNotExist(){
        assertThat(livreRepository.findById(21L).isPresent()).isEqualTo(false);
    }

    @Test
    public void getIdClient(){
        assertThat(livreRepository.findById(20L).get().getIdClient()).isEqualTo(1L);
        livreRepository.findById(20L).get().setIdClient(2L);
        assertThat(livreRepository.findById(20L).get().getIdClient()).isEqualTo(2L);
    }

    @Test
    public void getId(){
        assertThat(livreRepository.findAll().get(0).getId()).isEqualTo(20L);
        livreRepository.findAll().get(0).setId(21L);
        assertThat(livreRepository.findAll().get(0).getId()).isEqualTo(21L);
    }

    @Test
    public void getProlongation(){
        assertThat(livreRepository.findById(20L).get().getProlongation()).isEqualTo(false);
        livreRepository.findById(20L).get().setProlongation(true);
        assertThat(livreRepository.findById(20L).get().getProlongation()).isEqualTo(true);
    }

    @Test
    public void getDateDeb(){
        assertThat(livreRepository.findById(20L).get().getDateDeb()).isNotNull();
        Date today = new Date();
        livreRepository.findById(20L).get().setDateDeb(today);
        assertThat(livreRepository.findById(20L).get().getDateDeb()).isEqualTo(today);
    }


    @Test
    public void getDateFin(){
        assertThat(livreRepository.findById(20L).get().getDateFin()).isNotNull();
        Calendar cal = Calendar.getInstance();
        cal.setTime(livreRepository.findById(20L).get().getDateFin());
        cal.add(Calendar.MONTH,1);
        livreRepository.findById(20L).get().setDateFin(cal.getTime());
        assertThat(livreRepository.findById(20L).get().getDateFin()).isEqualTo(cal.getTime());
    }

    @Test
    public void getUserReservation(){
        assertThat(livreRepository.findById(20L).get().getUserReservation().getNbLivre()).isEqualTo(1);
        livreRepository.findById(20L).get().getUserReservation().setNbLivre(2);
        assertThat(livreRepository.findById(20L).get().getUserReservation().getNbLivre()).isEqualTo(2);

        assertThat(livreRepository.findById(20L).get().getUserReservation().getIdClient()).isEqualTo(1);
        livreRepository.findById(20L).get().getUserReservation().setIdClient(2L);
        assertThat(livreRepository.findById(20L).get().getUserReservation().getIdClient()).isEqualTo(2);

        assertThat(livreRepository.findById(20L).get().getUserReservation().getId()).isEqualTo(12);
        livreRepository.findById(20L).get().getUserReservation().setId(13L);
        assertThat(livreRepository.findById(20L).get().getUserReservation().getId()).isEqualTo(13);
    }

    @Test
    public void getlIvre(){
        assertThat(livreRepository.findById(20L).get().getLibrairie().getNExemplaire()).isEqualTo(1);
        livreRepository.findById(20L).get().getLibrairie().setNExemplaire(0);
        assertThat(livreRepository.findById(20L).get().getLibrairie().getNExemplaire()).isEqualTo(0);

    }

    @After
    public void undefLivreReserve(){
        reserveList.clear();
    }
}
