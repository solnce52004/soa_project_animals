package ru.example.auth.service.auth;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.auth.config.security.jwt.JwtAccessTokenProvider;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.User;
import ru.example.auth.exception.custom_exception.AccessTokenException;
import ru.example.auth.repo.AccessTokenRepository;
import ru.example.auth.service.by_entities.UserService;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccessTokenService implements TokenService<AccessToken> {
    private final UserService userService;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtAccessTokenProvider jwtAccessTokenProvider;
    @Value("${jwt.access-expires-at}")
    private final long accessExpirationInMs;

//    public AccessTokenService(
//            UserService userService,
//            AccessTokenRepository accessTokenRepository, JwtAccessTokenProvider jwtAccessTokenProvider,
//            @Value("${jwt.access-expires-at}")
//                    long accessExpirationInMs
//    ) {
//        this.userService = userService;
//        this.accessTokenRepository = accessTokenRepository;
//        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
//        this.accessExpirationInMs = accessExpirationInMs;
//    }

    @Override
    public TokenInfoDTO process(TokenInfoDTO tokenInfo) {
        String requestAccessToken = tokenInfo
                .getAccessToken()
                .getAccessTokenData();

        final AccessToken currentAccessToken = findByToken(requestAccessToken)
                .orElseThrow(() -> new AccessTokenException("Access-token is missing from the database"));

        final String accessToken = currentAccessToken.getAccessTokenData();
        if (accessToken == null || !jwtAccessTokenProvider.validateToken(accessToken)) {
            throw new AccessTokenException("Token is invalid");
        }

        final User user = verifyExpiration(currentAccessToken).getUser();
        if (user == null) {
            throw new AccessTokenException("Access-token in database is invalid");
        }

        return new TokenInfoDTO()
                .setUsername(user.getUsername())
                .setAccessToken(currentAccessToken);
    }

    @Override
    public Optional<AccessToken> findByToken(String token) {
        return accessTokenRepository.findByAccessTokenData(token);
    }

    @Override
    public AccessToken verifyExpiration(AccessToken token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            deleteToken(token);
            throw new AccessTokenException("Access token was expired and deleted");
        }
        return token;
    }

    @Override
    public AccessToken createToken(User user) {
        AccessToken byUser = accessTokenRepository.findByUserId(user.getId());
        if (byUser == null) {
            byUser = new AccessToken().setUser(user);
        }
        return accessTokenRepository.save(
                byUser
                        .setAccessTokenData(getTokenData(user))
                        .setExpiresAt(getNewExpiresAt()));
    }

    @Override
    public String getTokenData(User user) {
        return jwtAccessTokenProvider.createAccessToken(
                user.getUsername(),
                user.getPassword());
    }

    @Override
    public void deleteToken(AccessToken token) {
        accessTokenRepository.delete(token);
    }

    @Override
    public Instant getNewExpiresAt() {
        return Instant.now().plusMillis(this.accessExpirationInMs);
    }
}

