package com.example.accountmicroservice.service;

import com.example.accountmicroservice.models.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ClientHttpService {
    private final RestTemplate restTemplate;

    @Value("${external.api.url}")
    private String clientApiUrl;

    @Autowired
    public ClientHttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ClientResponse getClientByName(String name) {
        try{
            String url = clientApiUrl +  "clientes/find/name?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
            System.out.println("url " + url);
            return restTemplate.getForObject(url, ClientResponse.class);
        }catch (Exception e){
            System.err.println(e.getMessage());
            throw new IllegalArgumentException("Client not found");
        }
    }



}
