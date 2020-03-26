package com.microervicebatch.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserBean {

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
    @ManyToMany(fetch = FetchType.EAGER)
    Collection<RoleBean> roles =new ArrayList<>();

    public UserBean(String username, String password) {

        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserBean{");
        sb.append("num=").append(num);
        sb.append(", prenom='").append(prenom).append('\'');
        sb.append(", nom='").append(nom).append('\'');
        sb.append(", userName='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", matchingPassword='").append(matchingPassword).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", date=").append(date);
        sb.append(", active=").append(active);
        sb.append('}');
        return sb.toString();
    }
}
