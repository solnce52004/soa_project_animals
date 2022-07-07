package ru.example.auth.service.auth;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.RefreshToken;
import ru.example.auth.entity.User;
import ru.example.auth.exception.custom_exception.RefreshTokenException;
import ru.example.auth.repo.RefreshTokenRepository;
import ru.example.auth.service.by_entities.UserService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService implements TokenService<RefreshToken> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenService accessTokenService;
    private final UserService userService;
    @Value("${jwt.refresh-expires-at}")
    public final long refreshExpirationInMs;

//    @Autowired
//    public RefreshTokenService(
//            RefreshTokenRepository refreshTokenRepository,
//            AccessTokenService accessTokenService, UserService userService,
//            @Value("${jwt.refresh-expires-at}")
//                    long refreshExpirationInMs
//    ) {
//        this.accessTokenService = accessTokenService;
//        this.refreshTokenRepository = refreshTokenRepository;
//        this.userService = userService;
//        this.refreshExpirationInMs = refreshExpirationInMs;
//    }

    @Override
    public TokenInfoDTO process(TokenInfoDTO tokenInfo) {
        final String requestRefreshToken = tokenInfo
                .getRefreshToken()
                .getRefreshTokenData();

        final RefreshToken currentRefreshToken = findByToken(requestRefreshToken)
                .orElseThrow(() -> new RefreshTokenException("Refresh-token is missing from the database"));

        final User user = verifyExpiration(currentRefreshToken).getUser();
        if (user == null) {
            throw new RefreshTokenException("Refresh-token in database is invalid");
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
        return refreshTokenRepository.findByRefreshTokenData(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            deleteToken(token);
            throw new RefreshTokenException("Refresh token was expired and deleted");
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
                        .setRefreshTokenData(getTokenData(user))
                        .setExpiresAt(getNewExpiresAt()));
    }

    @Override
    public void deleteToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    @Override
    public String getTokenData(User user) {
        return UUID.randomUUID().toString();
    }

    @Override
    public Instant getNewExpiresAt() {
        return Instant.now().plusMillis(this.refreshExpirationInMs);
    }
}
