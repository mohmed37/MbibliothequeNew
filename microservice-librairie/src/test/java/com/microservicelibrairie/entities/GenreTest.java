package com.microservicelibrairie.entities;

import com.microservicelibrairie.dao.GenresRepository;

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
public class GenreTest {
    private List<Genre> listGenre;
    private Genre genre;
    private GenresRepository genresRepository;


    @Before
    public void initgenre() {
        this.genresRepository=Mockito.mock(GenresRepository.class);
        listGenre = new ArrayList<Genre>();
        for (int i = 1; i < 3; i++) {
            genre=new Genre.Builder().id(i).genre("Romans"+i).build();
            listGenre.add(genre);
        }


        Mockito.when(genresRepository.findAll()).thenReturn(listGenre);
        Mockito.when(genresRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(listGenre.get(0)));
        Mockito.when(genresRepository.findById(2)).thenReturn(java.util.Optional.ofNullable(listGenre.get(1)));

    }


    @Test
    public void getListGenre_notNull() {
        assertThat(genresRepository.findAll().isEmpty()).isNotNull();
        assertThat(genresRepository.findAll().size()).isEqualTo(2);

    }

    @Test
    public void getByNumero_whenGenreExist(){
        assertThat(genresRepository.findById(1).isPresent()).isEqualTo(true);
        assertThat(genresRepository.findById(2).isPresent()).isEqualTo(true);
    }
    @Test
    public void getByNumero_whenGenreNotExist(){
        assertThat(genresRepository.findById(3).isPresent()).isEqualTo(false);
        genresRepository.findById(1).get().setId(10);
        assertThat(genresRepository.findById(1).get().getId()).isEqualTo(10);
    }

    @Test
    public void getGenre_whenGenreExist(){
        assertThat(genresRepository.findById(1).get().getGenre()).isEqualTo("Romans1");
        genresRepository.findById(1).get().setGenre("BD");
        assertThat(genresRepository.findById(1).get().getGenre()).isEqualTo("BD");
    }


    @After
    public void undefCompteComptable() {
        listGenre.clear();
    }
}




