package com.microervicebatch;

import brave.sampler.Sampler;
import com.microervicebatch.web.controleur.EnvoiMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableFeignClients("com.microervicebatch")
@EnableDiscoveryClient
@EnableScheduling
public class MicroerviceBatchApplication {

	@Autowired
	EnvoiMail envoiMail;

	public static void main(String[] args) {
		SpringApplication.run(MicroerviceBatchApplication.class, args);
	}
	@Bean
	public Sampler defaultSampler(){
		return Sampler.ALWAYS_SAMPLE;
	}

}
