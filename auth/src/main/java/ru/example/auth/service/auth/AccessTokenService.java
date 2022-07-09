package ru.example.auth.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.auth.config.security.jwt.JwtAccessTokenProvider;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.User;
import ru.example.auth.exception.custom_exception.AccessTokenException;
import ru.example.auth.repo.AccessTokenRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class AccessTokenService implements TokenService<AccessToken> {
    private final AccessTokenRepository accessTokenRepository;
    private final JwtAccessTokenProvider jwtAccessTokenProvider;
    private final long accessExpirationInMs;

    @Autowired
    public AccessTokenService(
            AccessTokenRepository accessTokenRepository,
            JwtAccessTokenProvider jwtAccessTokenProvider,
            @Value("${jwt.access-expires-at}") long accessExpirationInMs
    ) {
        this.accessTokenRepository = accessTokenRepository;
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.accessExpirationInMs = accessExpirationInMs;
    }

    @Override
    public TokenInfoDTO process(String tokenValue) {
        if (tokenValue == null || tokenValue.isEmpty()) {
            throw new AccessTokenException("Access-token is empty");
        }

        final AccessToken currentAccessToken = findByToken(tokenValue)
                .orElseThrow(() -> new AccessTokenException("Access-token is missing from the database"));

        final String accessToken = currentAccessToken.getToken();
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
        return accessTokenRepository.findByToken(token);
    }

    @Override
    public AccessToken verifyExpiration(AccessToken token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            deleteToken(token.getToken());
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
                        .setToken(getTokenValue(user))
                        .setExpiresAt(getNewExpiresAt()));
    }

    @Override
    public String getTokenValue(User user) {
        return jwtAccessTokenProvider.createAccessToken(
                user.getUsername(),
                user.getPassword());
    }

    @Override
    @Transactional
    public void deleteToken(String tokenValue) {
        accessTokenRepository.deleteByToken(tokenValue);
    }

    @Override
    public Instant getNewExpiresAt() {
        return Instant.now().plusMillis(this.accessExpirationInMs);
    }
}

