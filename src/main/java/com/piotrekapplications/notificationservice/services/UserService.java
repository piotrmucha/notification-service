package com.piotrekapplications.notificationservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
    private final String CONFIRMATION_PATH = "/api/v1/user/confirmation/";
    private final RestTemplate restTemplate;
    @Value("${user.service.host}")
    private String userServiceHost;

    public UserService(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    public boolean confirmUser(String email) {
        ResponseEntity<String> response
                = restTemplate.exchange(userServiceHost +CONFIRMATION_PATH + email, HttpMethod.PUT,null, String.class);
        return response.getStatusCode() == HttpStatus.OK;
    }
}
