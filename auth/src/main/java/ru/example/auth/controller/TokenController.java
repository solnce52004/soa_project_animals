package ru.example.auth.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.example.auth.dto.ResponseDTO;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.dto.UserDTO;
import ru.example.auth.entity.User;
import ru.example.auth.service.auth.RefreshTokenService;
import ru.example.auth.service.auth.VerifyTokenService;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/v1/")
@Log4j2
@AllArgsConstructor
public class TokenController {
    private final VerifyTokenService verifyTokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/verify/token")
    public ResponseEntity<ResponseDTO> verifyToken(
            @Valid @RequestBody TokenInfoDTO tokenInfo
    ) {
        final User user = verifyTokenService.verifyUserAccessToken(tokenInfo.getAccessToken());

        final UserDTO verifiedUser = new UserDTO()
                .setToken(user.getToken())
                .setExpiryDate(user.getExpiryDate());

        return ResponseEntity.ok(new ResponseDTO()
                .setUser(verifiedUser)
                .setHttpStatus(HttpStatus.OK));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDTO> refreshToken(
            @Valid @RequestBody TokenInfoDTO tokenInfo
    ) {
        final TokenInfoDTO refreshInfo = refreshTokenService.getRefreshTokenInfo(tokenInfo);

        final UserDTO refreshedUser = new UserDTO()
                .setToken(refreshInfo.getAccessToken())
                .setExpiryDate(refreshInfo.getRefreshToken().getExpiryDate());

        return ResponseEntity.ok(new ResponseDTO()
                .setUser(refreshedUser)
                .setHttpStatus(HttpStatus.OK));
    }
}
