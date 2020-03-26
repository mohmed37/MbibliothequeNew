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
   /* public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }*/

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

}
