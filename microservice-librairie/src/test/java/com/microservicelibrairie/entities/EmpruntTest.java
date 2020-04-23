package com.microservicelibrairie.entities;

import com.microservicelibrairie.dao.EmprunterRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EmpruntTest {
    private List<Emprunt>reserveList;
    private EmprunterRepository emprunterRepository;


    @Before
    public void initLivreReserve(){
        this.emprunterRepository = Mockito.mock(EmprunterRepository.class);

        reserveList=new ArrayList<Emprunt>();

        Genre genre = new Genre.Builder().id(1).genre("Romans").build();

        Librairie livre = new Librairie.Builder().id(118L).auteur("Michele Lefevre").resume("Résumé")
                .nExemplaire(1).titre("La Casse ").genre(genre).prereserveMax(2).preeserver(0).build();
        Date today = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.MONTH,1);

        UserReservation userReservation = new UserReservation.Builder().id(12L).idClient(1L).nbLivre(1).build();

        Emprunt emprunt = new Emprunt.Builder().id(20L).prolongation(false).librairie(livre).idClient(1L).
                dateDeb(today).dateFin(cal.getTime()).userReservation(userReservation).build();
        reserveList.add(emprunt);
        Mockito.when(emprunterRepository.findAll()).thenReturn(reserveList);
        Mockito.when(emprunterRepository.findById(20L)).thenReturn(Optional.ofNullable(emprunt));
    }

    @Test
    public void getListReserve_notNull() {
        assertThat(emprunterRepository.findAll().isEmpty()).isNotNull();
        assertThat(emprunterRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void getById_whenReserveLivreExist(){
        assertThat(emprunterRepository.findById(20L).isPresent()).isEqualTo(true);
    }

    @Test
    public void getById_whenReserveLivreNotExist(){
        assertThat(emprunterRepository.findById(21L).isPresent()).isEqualTo(false);
    }

    @Test
    public void getIdClient(){
        assertThat(emprunterRepository.findById(20L).get().getIdClient()).isEqualTo(1L);
        emprunterRepository.findById(20L).get().setIdClient(2L);
        assertThat(emprunterRepository.findById(20L).get().getIdClient()).isEqualTo(2L);
    }

    @Test
    public void getId(){
        assertThat(emprunterRepository.findAll().get(0).getId()).isEqualTo(20L);
        emprunterRepository.findAll().get(0).setId(21L);
        assertThat(emprunterRepository.findAll().get(0).getId()).isEqualTo(21L);
    }

    @Test
    public void getProlongation(){
        assertThat(emprunterRepository.findById(20L).get().getProlongation()).isEqualTo(false);
        emprunterRepository.findById(20L).get().setProlongation(true);
        assertThat(emprunterRepository.findById(20L).get().getProlongation()).isEqualTo(true);
    }

    @Test
    public void getDateDeb(){
        assertThat(emprunterRepository.findById(20L).get().getDateDeb()).isNotNull();
        Date today = new Date();
        emprunterRepository.findById(20L).get().setDateDeb(today);
        assertThat(emprunterRepository.findById(20L).get().getDateDeb()).isEqualTo(today);
    }


    @Test
    public void getDateFin(){
        assertThat(emprunterRepository.findById(20L).get().getDateFin()).isNotNull();
        Calendar cal = Calendar.getInstance();
        cal.setTime(emprunterRepository.findById(20L).get().getDateFin());
        cal.add(Calendar.MONTH,1);
        emprunterRepository.findById(20L).get().setDateFin(cal.getTime());
        assertThat(emprunterRepository.findById(20L).get().getDateFin()).isEqualTo(cal.getTime());
    }

    @Test
    public void getUserReservation(){
        assertThat(emprunterRepository.findById(20L).get().getUserReservation().getNbLivre()).isEqualTo(1);
        emprunterRepository.findById(20L).get().getUserReservation().setNbLivre(2);
        assertThat(emprunterRepository.findById(20L).get().getUserReservation().getNbLivre()).isEqualTo(2);

        assertThat(emprunterRepository.findById(20L).get().getUserReservation().getIdClient()).isEqualTo(1);
        emprunterRepository.findById(20L).get().getUserReservation().setIdClient(2L);
        assertThat(emprunterRepository.findById(20L).get().getUserReservation().getIdClient()).isEqualTo(2);

        assertThat(emprunterRepository.findById(20L).get().getUserReservation().getId()).isEqualTo(12);
        emprunterRepository.findById(20L).get().getUserReservation().setId(13L);
        assertThat(emprunterRepository.findById(20L).get().getUserReservation().getId()).isEqualTo(13);
    }

    @Test
    public void getlIvre(){
        assertThat(emprunterRepository.findById(20L).get().getLibrairie().getNExemplaire()).isEqualTo(1);
        emprunterRepository.findById(20L).get().getLibrairie().setNExemplaire(0);
        assertThat(emprunterRepository.findById(20L).get().getLibrairie().getNExemplaire()).isEqualTo(0);

    }

    @After
    public void undefLivreReserve(){
        reserveList.clear();
    }
}
