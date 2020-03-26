package com.microserviceuser.dao;

import com.microserviceuser.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource
public interface AppUserRepository extends JpaRepository<AppUser,Long> {
 AppUser findByUsername(String username);

}
