package com.sicred.votacoop.services;

import com.sicred.votacoop.exceptions.BusinessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
public class UserIntegrationServiceTest {

    private static final String CPF_OK = "88487711499";
    private static final String CPF_NOK = "62289608068";

    @Value("${external-user-service.base-url}")
    private String baseUrl;

    @Autowired
    private UserIntegrationService userIntegrationService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        System.out.println("baseUrl: "+baseUrl);
    }

    @Test
    public void testGetUserVotingStatus_ok() {
        mockServer.expect(requestTo(baseUrl+CPF_OK))
                .andRespond(withSuccess("{\"status\": \"ABLE_TO_VOTE\"}", MediaType.APPLICATION_JSON));

        String status = userIntegrationService.getUserVotingStatus(CPF_OK);
        assertEquals("ABLE_TO_VOTE", status);
    }
    @Test
    public void testGetUserVotingStatus_nok() {
        mockServer.expect(requestTo(baseUrl+CPF_NOK))
                .andRespond(withSuccess("{\"status\": \"UNABLE_TO_VOTE\"}", MediaType.APPLICATION_JSON));

        String status = userIntegrationService.getUserVotingStatus(CPF_NOK);
        assertEquals("UNABLE_TO_VOTE", status);
    }

    @Test
    public void testGetUserVotingStatus_notFound() {
        mockServer.expect(requestTo(baseUrl+CPF_OK))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(BusinessException.class, () -> {
            userIntegrationService.getUserVotingStatus(CPF_OK);
        });
    }

    @Test
    public void testGetUserVotingStatus_otherError() {
        mockServer.expect(requestTo(baseUrl+CPF_OK))
                .andRespond(withStatus(HttpStatus.BAD_GATEWAY));

        assertThrows(BusinessException.class, () -> {
            userIntegrationService.getUserVotingStatus(CPF_OK);
        });
    }

    @Test
    public void testGetUserVotingStatus_unknownError() {
        mockServer.expect(requestTo(baseUrl+CPF_OK))
                .andRespond(withServerError());

        assertThrows(BusinessException.class, () -> {
            userIntegrationService.getUserVotingStatus(CPF_OK);
        });
    }

    @AfterEach
    public void tearDown() {
        mockServer.verify();
    }
}

