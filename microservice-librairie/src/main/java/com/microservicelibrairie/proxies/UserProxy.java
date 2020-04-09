package com.microservicelibrairie.proxies;

import com.microservicelibrairie.bean.UserBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
@FeignClient(name="zuul-server")
@RibbonClient(name = "microservice-utilisateur")
public interface UserProxy {

        @GetMapping(value = "/microservice-utilisateur/users")
        Optional<UserBean> findById(@RequestParam(name = "id") long id);

   }

