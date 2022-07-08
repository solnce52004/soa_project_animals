package ru.example.animals.service.api;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.example.animals.dto.request.RequestVerifyTokenDTO;
import ru.example.animals.dto.response.ResponseVerifyTokenDTO;
import ru.example.animals.exception.custom_exception.BaseError;
import ru.example.animals.exception.custom_exception.UserUnauthorizedException;
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
        final RequestVerifyTokenDTO requestVerifyTokenDTO = new RequestVerifyTokenDTO(accessTokenData);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RequestVerifyTokenDTO> requestToken = new HttpEntity<>(requestVerifyTokenDTO, headers);
        ResponseEntity<ResponseVerifyTokenDTO> response;

        try {
            response = new RestTemplate()
                    .exchange(
                            this.authUrlVerifyToken,
                            HttpMethod.POST,
                            requestToken,
                            ResponseVerifyTokenDTO.class
                    );

        } catch (RuntimeException e) {
            throw new VerifyTokenException(deserializeException(e));
        }

        if (!response.hasBody() || response.getBody() == null) {
            throw new UserUnauthorizedException();
        }
        if (!response.getBody().getError().getDetailMessage().isEmpty()) {
            throw new UserUnauthorizedException(
                    response.getBody().getError().getDetailMessage()
            );
        }

        return response.getBody().getUsername();
    }

    private String deserializeException(RuntimeException e) {
        final BaseError baseError = new Gson()
                .newBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create()
                .fromJson(e.getMessage(), BaseError.class);

        return baseError != null ? baseError.getDetailMessage() : "";
    }
}
