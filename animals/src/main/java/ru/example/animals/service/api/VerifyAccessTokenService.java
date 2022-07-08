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
import ru.example.animals.dto.request.RequestVerifyTokenDTO;
import ru.example.animals.dto.response.ResponseDTO;
import ru.example.animals.exception.custom_exception.VerifyTokenException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@Service
public class VerifyAccessTokenService {
    private final String authUrlVerifyToken;

    @Autowired
    public VerifyAccessTokenService(
            @Value("${auth.server.url.verify-token}") String authVerifyTokenUrl) {
        this.authUrlVerifyToken = authVerifyTokenUrl;
    }

    public String verifyRequest(HttpServletRequest request) {
        final String accessTokenData = request.getHeader("Bearer");
        final RequestVerifyTokenDTO requestVerifyTokenDTO = new RequestVerifyTokenDTO().setTokenData(accessTokenData);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestVerifyTokenDTO> requestToken = new HttpEntity<>(requestVerifyTokenDTO, headers);

        ResponseEntity<ResponseDTO> response;
        try {
            response = new RestTemplate()
                    .postForEntity(
                            this.authUrlVerifyToken,
                            requestToken,
                            ResponseDTO.class
                    );

        } catch (RuntimeException e) {
            throw new VerifyTokenException(deserializeException(e));
        }

        if (!response.hasBody() || response.getBody() == null) {
            throw new VerifyTokenException();
        }

        return response.getBody().getUsername();
    }

    //403!!!
    private String deserializeException(RuntimeException e) {
        final ResponseDTO baseError = new Gson().fromJson(
                ((HttpClientErrorException.Forbidden) e).getResponseBodyAsString(),
                ResponseDTO.class);

        return baseError.getError() != null
                ? baseError.getError().getDetailMessage()
                : "";
    }
}
