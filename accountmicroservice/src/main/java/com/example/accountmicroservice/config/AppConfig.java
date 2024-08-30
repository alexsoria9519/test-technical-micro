package com.example.accountmicroservice.config;

import com.example.accountmicroservice.exception.CustomErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new CustomErrorHandler());
//        return restTemplate;
//    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
