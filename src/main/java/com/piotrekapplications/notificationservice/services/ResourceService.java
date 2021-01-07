package com.piotrekapplications.notificationservice.services;


import com.piotrekapplications.notificationservice.data.ReservationReturn;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ResourceService {
    public final String RESERVATION_PATH = "/api/v1/reservation";

    private final RestTemplate restTemplate;

    @Value("${resource.service.host}")
    private String resourceServiceHost;

    public ResourceService(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    public List<ReservationReturn> getAllReservations() {
        ResponseEntity<ReservationReturn[]> response
                = restTemplate.getForEntity(resourceServiceHost + RESERVATION_PATH,
                ReservationReturn[].class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            return Collections.emptyList();
        }
    }

    public boolean setNotificationForReservationSent(String resourceName, String email) {
        ResponseEntity<String> response
                = restTemplate.exchange(resourceServiceHost + RESERVATION_PATH +
                        "?resourceName={resourceName}&ownerEmail={ownerEmail}",
                HttpMethod.PUT, null, String.class, resourceName,email);
        return response.getStatusCode() == HttpStatus.OK;
    }
}
