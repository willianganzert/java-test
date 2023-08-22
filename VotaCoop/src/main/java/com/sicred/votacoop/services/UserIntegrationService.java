package com.sicred.votacoop.services;

import com.sicred.votacoop.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
public class UserIntegrationService {

    @Value("${external-user-service.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public String getUserVotingStatus(String cpf) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + cpf, Map.class);

            if(response.getStatusCode() == HttpStatus.OK) {
                return (String) Objects.requireNonNull(response.getBody()).get("status");
            }

            throw new BusinessException("Error retrieving voting status from the user service.", HttpStatus.BAD_GATEWAY);

        } catch (HttpClientErrorException.NotFound e) {
            throw new BusinessException("CPF is invalid.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new BusinessException("Error connecting to the user service. Try again later.", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
