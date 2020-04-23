package com.microservicelibrairie.entities;


import com.microservicelibrairie.dao.LibrairieRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LibrairieTest {
    private List<Librairie> listLivre;
    private LibrairieRepository librairieRepository;


    @Before
    public void initLibrairie() {
        this.librairieRepository= Mockito.mock(LibrairieRepository.class);
        listLivre = new ArrayList<Librairie>();
        Genre genre=new Genre.Builder().id(1).genre("Romans").build();

        Librairie livre = new Librairie.Builder().id(108L).preeserver(0).prereserveMax(4).genre(genre).titre("Test unitaire")
                .nExemplaire(2).resume("Mise en place des tests.").auteur("Moi").build();

        listLivre.add(livre);

        Mockito.when(librairieRepository.findAll()).thenReturn(listLivre);
        Mockito.when(librairieRepository.findById(108L)).thenReturn(java.util.Optional.ofNullable(livre));

    }

    @Test
    public void getListLivres_notNull() {
        assertThat(librairieRepository.findAll().isEmpty()).isNotNull();
        assertThat(librairieRepository.findAll().size()).isEqualTo(1);

    }

    @Test
    public void getById_whenLivreExist(){
        assertThat(librairieRepository.findById(108L).isPresent()).isEqualTo(true);

    }
    @Test
    public void getByNumero_whenGenreNotExist(){
        assertThat(librairieRepository.findById(102L).isPresent()).isEqualTo(false);

    }

    @Test
    public void getId(){
        assertThat(librairieRepository.findAll().get(0).getId()).isEqualTo(108L);
        librairieRepository.findAll().get(0).setId(102L);
        assertThat(librairieRepository.findAll().get(0).getId()).isEqualTo(102L);
    }

    @Test
    public void getExemplaire(){
        assertThat(librairieRepository.findById(108L).get().getNExemplaire()).isEqualTo(2);
        librairieRepository.findById(108L).get().setNExemplaire(1);
        assertThat(librairieRepository.findById(108L).get().getNExemplaire()).isEqualTo(1);
    }

    @Test
    public void getPreserve(){
        assertThat(librairieRepository.findById(108L).get().getPrereserve()).isEqualTo(0);
        librairieRepository.findById(108L).get().setPrereserve(1);
        assertThat(librairieRepository.findById(108L).get().getPrereserve()).isEqualTo(1);
    }

    @Test
    public void getPrereserveMax(){
        assertThat(librairieRepository.findById(108L).get().getPrereserveMax()).isEqualTo(4);
        librairieRepository.findById(108L).get().setPrereserveMax(3);
        assertThat(librairieRepository.findById(108L).get().getPrereserveMax()).isEqualTo(3);
    }

    @Test
    public void getTitre(){
        assertThat(librairieRepository.findById(108L).get().getTitre()).isEqualTo("Test unitaire");
        librairieRepository.findById(108L).get().setTitre("Nouveau test unitaire");
        assertThat(librairieRepository.findById(108L).get().getTitre()).isEqualTo("Nouveau test unitaire");
    }

    @Test
    public void getResume(){
        assertThat(librairieRepository.findById(108L).get().getResume()).isEqualTo("Mise en place des tests.");
        librairieRepository.findById(108L).get().setResume("Mise en place des nouveaux tests.");
        assertThat(librairieRepository.findById(108L).get().getResume()).isEqualTo("Mise en place des nouveaux tests.");
    }

    @Test
    public void getAuteur(){
        assertThat(librairieRepository.findById(108L).get().getAuteur()).isEqualTo("Moi");
        librairieRepository.findById(108L).get().setAuteur("Nous");
        assertThat(librairieRepository.findById(108L).get().getAuteur()).isEqualTo("Nous");
    }

    @Test
    public void getGenre(){
        assertThat(librairieRepository.findById(108L).get().getGenre().getGenre()).isEqualTo("Romans");
        librairieRepository.findById(108L).get().getGenre().setGenre("BD");
        assertThat(librairieRepository.findById(108L).get().getGenre().getGenre()).isEqualTo("BD");
    }

    @Test
    public void getPhoto(){

        librairieRepository.findById(108L).get().setPhoto("Ma photo");
        assertThat(librairieRepository.findById(108L).get().getPhoto()).isEqualTo("Ma photo");
    }

    @After
    public void undefCompteComptable() {
        listLivre.clear();
    }

}
