package com.microervicebatch.proxies;

import com.microervicebatch.bean.LivreReserveBean;
import com.microervicebatch.bean.UserReservationBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name="zuul-server")
@RibbonClient("microservice-librairie")

public interface MlibrairieProxy {

 @GetMapping(value = "/microservice-librairie/userReservation")
 List<UserReservationBean>userReservations();

 @GetMapping(value = "/microservice-librairie/location")
 List<LivreReserveBean> findByLocation(@RequestParam(name = "num") long num);

}







