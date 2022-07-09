package ru.example.animals.service.api;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
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
import ru.example.animals.exception.custom_exception.VerifyTokenException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@Service
public class VerifyAccessTokenService {
    private static final String HEADER_NAME_AUTHORIZATION = "Authorization";
    private final String authUrlVerifyToken;

    @Autowired
    public VerifyAccessTokenService(
            @Value("${auth.server.url.verify-token}") String authVerifyTokenUrl) {
        this.authUrlVerifyToken = authVerifyTokenUrl;
    }

    public String verifyRequest(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME_AUTHORIZATION, request.getHeader(HEADER_NAME_AUTHORIZATION));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestToken = new HttpEntity<>(headers);

        ResponseEntity<VerifyTokenResponseDTO> response;
        try {
            response = new RestTemplate()
                    .postForEntity(
                            this.authUrlVerifyToken,
                            requestToken,
                            VerifyTokenResponseDTO.class
                    );

        } catch (RuntimeException e) {
            throw new VerifyTokenException(deserializeException(e));
        }

        if (!response.hasBody() || response.getBody() == null) {
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
