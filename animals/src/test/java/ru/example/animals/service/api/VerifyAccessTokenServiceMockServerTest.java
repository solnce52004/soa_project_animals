package ru.example.animals.service.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.example.animals.config.mockserver.AbstractIntegrationTest;
import ru.example.animals.controller.util.AnimalUtil;
import ru.example.animals.dto.response.VerifyTokenResponseDTO;

import java.util.concurrent.TimeUnit;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@AutoConfigureMockMvc
public class VerifyAccessTokenServiceMockServerTest extends AbstractIntegrationTest {

    private static final String USERNAME_VERIFIED = AnimalUtil.USERNAME;
    private static final String BEARER_TOKEN = "Bearer token";
    private static final String AUTH_API_VERIFY_TOKEN_PATH = "/auth/api/v1/verify/token";

    @Autowired
    MockMvc mvc;
    @LocalServerPort
    private int port;
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

        final HttpRequest request = request()
                .withPath(AUTH_API_VERIFY_TOKEN_PATH)
                .withHeader("Authorization", BEARER_TOKEN)
                .withMethod("POST")
                .withBody("[]")
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        mockServerClient
                .when(request)
                .respond(response()
                        .withStatusCode(200)
                        .withBody(new ObjectMapper().writeValueAsString(verifiedDTO))
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withDelay(TimeUnit.SECONDS, 1));

        final MockRestServiceServer mockServerRestTemplate = MockRestServiceServer.createServer(restTemplate);
        mockServerRestTemplate
                .expect(
                        ExpectedCount.manyTimes(),
                        requestTo(UriComponentsBuilder.fromHttpUrl(
                                String.format("http://localhost:%d%s", port, AUTH_API_VERIFY_TOKEN_PATH))
                                .build().toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(
                        new ObjectMapper().writeValueAsString(verifiedDTO),
                        MediaType.APPLICATION_JSON));

        final ResponseEntity<VerifyTokenResponseDTO> entity = restTemplate.postForEntity(
                String.format("http://localhost:%d%s", port, AUTH_API_VERIFY_TOKEN_PATH),
                request,
                VerifyTokenResponseDTO.class
        );
        Assertions.assertThat(entity.getBody()).isNotNull();
        Assertions.assertThat(entity.getBody().getUsername()).isEqualTo(USERNAME_VERIFIED);
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

//        new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
//                .retrieveRecordedRequests(request);
//        mockServerClient.verify(request, VerificationTimes.exactly(1));
    }
}
