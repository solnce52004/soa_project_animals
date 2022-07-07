package ru.example.animals.service.api;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.example.animals.dto.AccessTokenDTO;
import ru.example.animals.dto.ResponseDTO;
import ru.example.animals.dto.TokenInfoDTO;
import ru.example.animals.exception.custom_exception.UserUnauthorizedException;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class VerifyAccessTokenService {
    @Value("${auth.server.url.verify-token}")
    private final String authUrlVerifyToken;

    public ResponseDTO getVerifiedResponseDTO(HttpServletRequest request) {
        final String accessToken = request.getHeader("Bearer");
        final TokenInfoDTO tokenInfoDTO = new TokenInfoDTO()
                .setAccessToken(new AccessTokenDTO(accessToken));

        ResponseEntity<ResponseDTO> responseEntity = new RestTemplate()
                .getForEntity(
                        this.authUrlVerifyToken,
                        ResponseDTO.class,
                        tokenInfoDTO);

        final ResponseDTO responseDTO = responseEntity.getBody();
        if (responseDTO == null) {
            throw new UserUnauthorizedException();
        }
        return responseDTO;
    }
}
