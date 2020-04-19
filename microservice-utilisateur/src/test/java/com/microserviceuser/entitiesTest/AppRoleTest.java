package com.microserviceuser.entitiesTest;

import com.microserviceuser.dao.AppRoleRepository;
import com.microserviceuser.entities.AppRole;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AppRoleTest {

    private List<AppRole> roleList;
    private AppRoleRepository appRoleRepository;

    @Before
    public void initRole(){
     this.appRoleRepository=Mockito.mock(AppRoleRepository.class);

        roleList=new ArrayList<AppRole>();
        AppRole role=new AppRole.Builder().id(1L).role("user").build();
        roleList.add(role);
        Mockito.when(appRoleRepository.findAll()).thenReturn(roleList);

    }
}
