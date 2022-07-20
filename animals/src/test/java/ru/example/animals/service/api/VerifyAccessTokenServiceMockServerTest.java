package ru.example.animals.service.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.example.animals.config.mockserver.AbstractIntegrationTest;
import ru.example.animals.controller.util.AnimalUtil;
import ru.example.animals.dto.response.VerifyTokenResponseDTO;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
public class VerifyAccessTokenServiceMockServerTest extends AbstractIntegrationTest {

    private static final String USERNAME_VERIFIED = AnimalUtil.USERNAME;
    private static final String BEARER_TOKEN = AnimalUtil.BEARER_TOKEN;
    private static final String AUTH_API_VERIFY_TOKEN_PATH = "/auth/api/v1/verify/token";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    VerifyAccessTokenService service;

    @Test
    void verifyToken() throws Exception {
        final VerifyTokenResponseDTO verifiedDTO = new VerifyTokenResponseDTO()
                .setError(null)
                .setUsername(USERNAME_VERIFIED)
                .setHttpStatus(HttpStatus.OK);

        final String apiBaseUrl = String.format("%s%s", mockServer.getEndpoint(), AUTH_API_VERIFY_TOKEN_PATH);
        final MockRestServiceServer mockRestTemplateServer = MockRestServiceServer.bindTo(restTemplate).build();

        mockRestTemplateServer
                .expect(
                        ExpectedCount.once(),
                        requestTo(UriComponentsBuilder.fromHttpUrl(apiBaseUrl).build().toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        withSuccess(
                                new ObjectMapper().writeValueAsString(verifiedDTO),
                                MediaType.APPLICATION_JSON));

        Assertions.assertThat(service.verifyToken(BEARER_TOKEN))
                .isEqualTo(USERNAME_VERIFIED);

        mockRestTemplateServer.verify();
    }
}
