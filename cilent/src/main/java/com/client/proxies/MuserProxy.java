package com.client.proxies;


import com.client.bean.UserBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@FeignClient(name="zuul-server")
@RibbonClient(name = "microservice-utilisateur")
public interface MuserProxy {

    @GetMapping(value = "/microservice-utilisateur/users")
    Optional<UserBean> findById(@RequestParam(name = "id") long id);

    @PostMapping(value ="/microservice-utilisateur/username")
    UserBean findUserByUsername( @RequestParam(name = "username",defaultValue ="")String username);

}
