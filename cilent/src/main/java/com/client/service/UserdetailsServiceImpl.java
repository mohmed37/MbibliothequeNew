package com.client.service;

import com.client.bean.UserBean;
import com.client.controller.ClientController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;

@Service("userDetailsService")

public class UserdetailsServiceImpl implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ClientController clientController;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserBean userBean=clientController.findUserByUsername(username);
        String passwordB = bCryptPasswordEncoder.encode(userBean.getPassword());
        if (userBean==null)throw new  UsernameNotFoundException("User not found.");
        Collection<GrantedAuthority> authorities=new ArrayList<>();
        userBean.getRoles().forEach(roleBean -> {authorities.add(new SimpleGrantedAuthority(roleBean.getRole()));
        });
        return new User(userBean.getUsername(),passwordB,authorities);


    }

}
