package com.microservicelibrairie;

import brave.sampler.Sampler;
import com.microservicelibrairie.dao.EmprunterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableConfigurationProperties
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients("com.microservicelibrairie")
public class MicroserviceLibrairieApplication {

    @Autowired
    EmprunterRepository emprunterRepository;

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceLibrairieApplication.class, args);

    }
    @Bean
    public Sampler defaultSampler(){
        return Sampler.ALWAYS_SAMPLE;
    }




}
