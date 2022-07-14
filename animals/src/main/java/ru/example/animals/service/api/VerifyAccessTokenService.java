package ru.example.animals.service.api;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.example.animals.dto.response.VerifyTokenResponseDTO;
import ru.example.animals.exception.custom_exception.auth.VerifyTokenException;

import java.util.Collections;

@Service
public class VerifyAccessTokenService {
    public final String HEADER_NAME_AUTHORIZATION = "Authorization";
    private final String authUrlVerifyToken;
    private final RestTemplate restTemplate;

    @Autowired
    public VerifyAccessTokenService(
            @Value("${verify-token-url}")
                    String authUrlVerifyToken,
            RestTemplate restTemplate
    ) {
        this.authUrlVerifyToken = authUrlVerifyToken;
        this.restTemplate = restTemplate;
    }

    public String verifyToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME_AUTHORIZATION, token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestToken = new HttpEntity<>(headers);

        ResponseEntity<VerifyTokenResponseDTO> response;
        try {
            response = restTemplate
                    .postForEntity(
                            this.authUrlVerifyToken,
                            requestToken,
                            VerifyTokenResponseDTO.class
                    );

        } catch (RuntimeException e) {
            throw new VerifyTokenException(deserializeException(e));
        }

        if (response == null || !response.hasBody() || response.getBody() == null) {
            throw new VerifyTokenException();
        }

        return response.getBody().getUsername();
    }

    private String deserializeException(RuntimeException e) {
        String eBody = null;

        if (e instanceof HttpClientErrorException.NotFound) {
            eBody = ((HttpClientErrorException.NotFound) e).getResponseBodyAsString();
        } else if (e instanceof HttpClientErrorException.Forbidden) {
            eBody = ((HttpClientErrorException.Forbidden) e).getResponseBodyAsString();
        } else if (e instanceof HttpClientErrorException.Unauthorized) {
            eBody = ((HttpClientErrorException.Unauthorized) e).getResponseBodyAsString();
        } else if (e instanceof HttpClientErrorException.BadRequest) {
            eBody = ((HttpClientErrorException.BadRequest) e).getResponseBodyAsString();
        }

        if (eBody != null && !eBody.equals("")) {
            final VerifyTokenResponseDTO baseError = new Gson().fromJson(
                    eBody,
//                    StringEscapeUtils.unescapeJson(eBody),
                    VerifyTokenResponseDTO.class);

            if (baseError != null && baseError.getError() != null) {
                return baseError.getError().getDetailMessage();
            }

            return "";
        }

        return e.getMessage() != null
                ? e.getMessage()
                : "";
    }
}
