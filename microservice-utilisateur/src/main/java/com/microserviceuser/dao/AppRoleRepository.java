package com.microserviceuser.dao;

import com.microserviceuser.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface AppRoleRepository extends JpaRepository<AppRole,Long> {

}
