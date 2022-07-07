package ru.example.auth.service.auth;

import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.entity.User;

import java.time.Instant;
import java.util.Optional;

public interface TokenService<T> {
    TokenInfoDTO process(TokenInfoDTO tokenInfo);

    Optional<T> findByToken(String token);

    T verifyExpiration(T token);

    T createToken(User user);

    void deleteToken(T token);

    String getTokenData(User user);

    Instant getNewExpiresAt();
}
