package ru.example.auth.controller.util;

import ru.example.auth.dto.UserDTO;
import ru.example.auth.dto.request.AuthRequestDTO;
import ru.example.auth.dto.request.RefreshTokenRequestDTO;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.RefreshToken;

import java.time.Instant;
import java.util.Date;

public final class Util {
    public static final String USERNAME = "rty";
    public static final String PASSWORD = "123";
    public static final String BEARER_TOKEN = "Bearer token";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final Instant EXPIRES_AT = new Date().toInstant();
    public static final String IP = "127.0.0.1";

    public static final AuthRequestDTO authRequestDTO = getAuthRequestDTO();
    public static final RefreshTokenRequestDTO refreshTokenRequestDTO = getRefreshTokenRequestDTO();
    public static final AccessToken accessToken = getAccessToken();
    public static final RefreshToken refreshToken = getRefreshToken();
    public static final UserDTO user = getUser();

    public static AuthRequestDTO getAuthRequestDTO() {
        return new AuthRequestDTO()
                .setUsername(USERNAME).setPassword(PASSWORD);
    }

    public static RefreshTokenRequestDTO getRefreshTokenRequestDTO() {
        return new RefreshTokenRequestDTO()
                .setRefreshToken(Util.REFRESH_TOKEN);
    }

    public static AccessToken getAccessToken() {
        return new AccessToken().setToken(ACCESS_TOKEN)
                .setExpiresAt(EXPIRES_AT);
    }

    public static RefreshToken getRefreshToken() {
        return new RefreshToken().setToken(REFRESH_TOKEN)
                .setExpiresAt(EXPIRES_AT);
    }

    public static UserDTO getUser() {
        return new UserDTO()
                .setUsername(USERNAME).setPassword(PASSWORD)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }
}
