package com.microserviceuser.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppUser implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long num;

    @NotNull
    String prenom;

    @NotNull
    String nom;

    @Column(unique = true)
    @NotNull
    String username;

    @NotNull
    String password;

    @NotNull
    String matchingPassword;

    @Column(unique = true)
    @NotNull
    @Email
    String email;

    @NotNull
    String phone;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;

    Boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    Collection<AppRole> roles =new ArrayList<>();

    private AppUser(Builder builder) {
        this.num=builder.num;
        this.prenom=builder.prenom;
        this.nom=builder.nom;
        this.username=builder.username;
        this.password=builder.password;
        this.matchingPassword=builder.matchingPassword;
        this.email=builder.email;
        this.phone=builder.phone;
        this.date=builder.date;
        this.active=builder.active;
    }

    public static class Builder{

        Long num;
        String prenom;
        String nom;
        String username;
        String password;
        String matchingPassword;
        String email;
        String phone;
        Date date;
        Boolean active;

        public  AppUser.Builder num(Long num){
            this.num= num;
            return this;}
        public AppUser.Builder prenom(String prenom){
            this.prenom= prenom;
            return this;}
        public AppUser.Builder nom(String nom){
            this.nom= nom;
            return this;}
        public AppUser.Builder username(String username){
            this.username= username;
            return this;}
        public AppUser.Builder password(String password){
            this.password= password;
            return this;}
        public AppUser.Builder matchingPassword(String matchingPassword){
            this.matchingPassword= matchingPassword;
            return this;}
        public AppUser.Builder email(String email){
            this.email= email;
            return this;}
        public AppUser.Builder phone(String phone){
            this.phone= phone;
            return this;}
        public AppUser.Builder date(Date date){
            this.date= date;
            return this;}
        public AppUser.Builder  active( Boolean active){
            this. active=  active;
            return this;}

        public AppUser build(){
            return new AppUser(this);}

    }

}
