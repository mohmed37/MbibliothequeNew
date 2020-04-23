package com.microserviceuser.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class AppRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String role;

    public AppRole(Builder builder) {
        this.id=builder.id;
        this.role=builder.role;
    }

    public static class Builder{
        Long id;
        String role;

        public AppRole.Builder id(Long id){
            this.id= id;
            return this;}
        public AppRole.Builder role(String role){
            this.role= role;
            return this;}
        public AppRole build(){
            return new AppRole(this);}
    }


}
