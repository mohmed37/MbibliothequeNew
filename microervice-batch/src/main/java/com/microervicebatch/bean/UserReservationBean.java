package com.microervicebatch.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReservationBean {
    Long id;
    Long idClient;
    int nbLivre=0;



    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserReservationBean{");
        sb.append("id=").append(id);
        sb.append(", idClient=").append(idClient);
        sb.append('}');
        return sb.toString();
    }
}
