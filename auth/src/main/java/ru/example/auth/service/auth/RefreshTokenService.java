package ru.example.auth.service.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.RefreshToken;
import ru.example.auth.entity.User;
import ru.example.auth.exception.custom_exception.refresh_token.EmptyRefreshTokenException;
import ru.example.auth.exception.custom_exception.refresh_token.ExpiredAndDeletedRefreshTokenException;
import ru.example.auth.exception.custom_exception.refresh_token.InvalidRefreshTokenException;
import ru.example.auth.exception.custom_exception.refresh_token.MissingRefreshTokenException;
import ru.example.auth.repo.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements TokenService<RefreshToken> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenService accessTokenService;
    public final long refreshExpirationInMs;

    @Autowired
    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            AccessTokenService accessTokenService,
            @Value("${jwt.refresh-expires-at}") long refreshExpirationInMs
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenService = accessTokenService;
        this.refreshExpirationInMs = refreshExpirationInMs;
    }

    @Override
    public TokenInfoDTO process(String tokenValue) {
        if (tokenValue == null || tokenValue.isEmpty()) {
            throw new EmptyRefreshTokenException();
        }

        final RefreshToken currentRefreshToken = findByToken(tokenValue)
                .orElseThrow(MissingRefreshTokenException::new);

        final User user = verifyExpiration(currentRefreshToken).getUser();
        if (user == null) {
            throw new InvalidRefreshTokenException();
        }

        final AccessToken accessToken = accessTokenService.createToken(user);
        final RefreshToken refreshToken = createToken(user);

        return new TokenInfoDTO()
                .setUsername(user.getUsername())
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            deleteToken(token.getToken());
            throw new ExpiredAndDeletedRefreshTokenException();
        }
        return token;
    }

    @Override
    public RefreshToken createToken(User user) {
        RefreshToken byUser = refreshTokenRepository.findByUserId(user.getId());
        if (byUser == null) {
            byUser = new RefreshToken().setUser(user);
        }
        return refreshTokenRepository.save(
                byUser
                        .setToken(getTokenValue(user))
                        .setExpiresAt(getNewExpiresAt()));
    }

    @Override
    public void deleteToken(String tokenValue) {
        refreshTokenRepository.deleteByToken(tokenValue);
    }

    @Override
    public String getTokenValue(User user) {
        return UUID.randomUUID().toString();
    }

    @Override
    public Instant getNewExpiresAt() {
        return Instant.now().plusMillis(this.refreshExpirationInMs);
    }
}
