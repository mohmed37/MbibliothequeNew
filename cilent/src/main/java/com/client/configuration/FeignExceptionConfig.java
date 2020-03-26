package com.client.configuration;

import com.client.exceptions.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FeignExceptionConfig {

   @Bean
    public CustomErrorDecoder nCustomErrorDecoder(){
        return new CustomErrorDecoder();
    }
}
