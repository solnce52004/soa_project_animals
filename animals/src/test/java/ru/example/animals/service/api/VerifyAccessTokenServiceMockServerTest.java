package ru.example.animals.service.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import ru.example.animals.config.mockserver.AbstractIntegrationTest;
import ru.example.animals.controller.util.AnimalUtil;
import ru.example.animals.dto.response.VerifyTokenResponseDTO;

import java.util.concurrent.TimeUnit;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
                        .withDelay(TimeUnit.SECONDS,1));

        mvc.perform(MockMvcRequestBuilders.post(AUTH_API_VERIFY_TOKEN_PATH)
                .header("Authorization", BEARER_TOKEN)
                .content("[]")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

//        final ResponseEntity<VerifyTokenResponseDTO> entity = restTemplate.postForEntity(
//                String.format("http://localhost:%d%s", port, AUTH_API_VERIFY_TOKEN_PATH),
//                request,
//                ArgumentMatchers.same(VerifyTokenResponseDTO.class)
//        );
//        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

//        mockServerClient.verify(request, VerificationTimes.exactly(1));
    }
}
