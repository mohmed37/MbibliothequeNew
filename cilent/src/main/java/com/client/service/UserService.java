package com.client.service;

import com.client.bean.LibrairieBean;
import com.client.bean.UserBean;
import com.client.controller.ClientController;
import com.client.proxies.MlibrairieProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService implements IUserService{
    @Autowired
    ClientController controller;
    @Autowired
    MlibrairieProxy mlibrairieProxy;


    @Override
    public UserBean getUserConnec() {
        Object objConnected = SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();

        if (objConnected instanceof UserDetails) {
            UserDetails connectedUser = (UserDetails) objConnected;
            UserBean userConnec= controller.findUserByUsername( connectedUser.getUsername());
            return userConnec;
        }
        return null;

    }

}
