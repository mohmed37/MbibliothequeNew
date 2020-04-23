package com.microserviceuser.entitiesTest;

import com.microserviceuser.dao.AppRoleRepository;
import com.microserviceuser.dao.AppUserRepository;
import com.microserviceuser.entities.AppRole;
import com.microserviceuser.entities.AppUser;
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
public class AppUserTest {
    private List<AppUser>userList;
    private AppUserRepository userRepository;



    @Before
    public void initUser(){
        this.userRepository= Mockito.mock(AppUserRepository.class);
        userList =new ArrayList<AppUser>();

        AppUser user = new AppUser.Builder().num(25L).prenom("Robert").nom("Vivier").email("robert.vivier@yahoo.fr").
                date(new Date()).active(true).username("robert").password("12345").matchingPassword("12345")
                .phone("0625659863").build();

        userList.add(user);

        Mockito.when(userRepository.findAll()).thenReturn(userList);
        Mockito.when(userRepository.findById(25L)).thenReturn(java.util.Optional.ofNullable(user));

    }

    @Test
    public void getListUser_notNull() {
        assertThat(userRepository.findAll().isEmpty()).isNotNull();
        assertThat(userRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void getByNumero_whenUserExist(){
        assertThat(userRepository.findById(25L).isPresent()).isEqualTo(true);
    }

    @Test
    public void getByNumero_whenUserNotExist(){
        assertThat(userRepository.findById(22L).isPresent()).isEqualTo(false);
    }

    @Test
    public void getId(){
       assertThat(userRepository.findAll().get(0).getNum()).isEqualTo(25L);
       userRepository.findAll().get(0).setNum(26L);
        assertThat(userRepository.findAll().get(0).getNum()).isEqualTo(26L);
    }

    @Test
    public void getNom(){
        assertThat(userRepository.findById(25L).get().getNom()).isEqualTo("Vivier");
        userRepository.findById(25L).get().setNom("Durant");
        assertThat(userRepository.findById(25L).get().getNom()).isEqualTo("Durant");
    }

    @Test
    public void getEmail(){
    assertThat(userRepository.findById(25L).get().getEmail()).isEqualTo("robert.vivier@yahoo.fr");
        userRepository.findById(25L).get().setEmail("vivier@yahoo.fr");
    assertThat(userRepository.findById(25L).get().getEmail()).isEqualTo("vivier@yahoo.fr");
    }

    @Test
    public void getPrenom(){
        assertThat(userRepository.findById(25L).get().getPrenom()).isEqualTo("Robert");
        userRepository.findById(25L).get().setPrenom("Jean");
        assertThat(userRepository.findById(25L).get().getPrenom()).isEqualTo("Jean");
    }

    @Test
    public void getDate(){
        Date today=new Date();
        assertThat(userRepository.findById(25L).get().getDate()).isNotNull();
        userRepository.findById(25L).get().setDate(today);
        assertThat(userRepository.findById(25L).get().getDate()).isEqualTo(today);
    }

    @Test
    public void getUserMan(){
        assertThat(userRepository.findById(25L).get().getUsername()).isEqualTo("robert");
        userRepository.findById(25L).get().setUsername("Jean");
        assertThat(userRepository.findById(25L).get().getUsername()).isEqualTo("Jean");
    }

    @Test
    public void getPassword(){
        assertThat(userRepository.findById(25L).get().getPassword()).isEqualTo("12345");
        assertThat(userRepository.findById(25L).get().getMatchingPassword()).isEqualTo(userRepository.findById(25L)
                .get().getPassword());

        userRepository.findById(25L).get().setPassword("45678");
        userRepository.findById(25L).get().setMatchingPassword(userRepository.findById(25L).get().getPassword());

        assertThat(userRepository.findById(25L).get().getPassword()).isEqualTo("45678");
        assertThat(userRepository.findById(25L).get().getMatchingPassword()).isEqualTo(userRepository.findById(25L)
                .get().getPassword());
    }

    @Test
    public void getPhone(){
        assertThat(userRepository.findById(25L).get().getPhone()).isEqualTo("0625659863");
        userRepository.findById(25L).get().setPhone("0659875945");
        assertThat(userRepository.findById(25L).get().getPhone()).isEqualTo("0659875945");
    }

    @After
    public void undefUser() {
        userList.clear();
    }

}
