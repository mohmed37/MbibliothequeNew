package com.microserviceuser.web.controller;


import com.microserviceuser.dao.AppUserRepository;
import com.microserviceuser.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
public class UserController {

    @Autowired
    private AppUserRepository appUserRepository;


    /**
     * Rechercher un utilisateur par son id.
     * @param id utilisateur
     * @return
     */
    @GetMapping(value = "/users")
    public Optional<AppUser> findById(@RequestParam(name = "id") long id) {
        Optional<AppUser> appUsers = appUserRepository.findById(id);
        return appUsers;
    }

    /**
     * Enregistrer un utilisateur
     * @param username
     * @return
     */

    @PostMapping(value = "/username")
    public AppUser findUserByUsername(@RequestParam(name = "username", defaultValue = "") String username) {
        return appUserRepository.findByUsername(username);
    }



}


