package ru.example.animals.service.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.example.animals.controller.util.AnimalUtil;
import ru.example.animals.dto.response.VerifyTokenResponseDTO;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class VerifyAccessTokenServiceTest {

    private static final String USERNAME_VERIFIED = AnimalUtil.USERNAME;
    private static final String BEARER_TOKEN = AnimalUtil.BEARER_TOKEN;

    @MockBean
    RestTemplate restTemplate;
    @Autowired
    VerifyAccessTokenService service;

    @Test
    void verifyToken() {
        final VerifyTokenResponseDTO verifiedDTO = new VerifyTokenResponseDTO()
                .setError(null)
                .setUsername(USERNAME_VERIFIED)
                .setHttpStatus(HttpStatus.OK);

        final ResponseEntity<VerifyTokenResponseDTO> ok = ResponseEntity.ok(verifiedDTO);

        Mockito.when(restTemplate.postForEntity(
                anyString(),
                any(),
                same(VerifyTokenResponseDTO.class)
        ))
                .thenReturn(ok);

        Assertions.assertThat(service.verifyToken(BEARER_TOKEN))
                .isEqualTo(USERNAME_VERIFIED);
    }
}