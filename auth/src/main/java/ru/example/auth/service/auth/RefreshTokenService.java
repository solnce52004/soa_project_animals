package ru.example.auth.service.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.auth.config.security.jwt.JwtTokenProvider;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.entity.RefreshToken;
import ru.example.auth.entity.User;
import ru.example.auth.exception.custom_exception.TokenRefreshException;
import ru.example.auth.repo.RefreshTokenRepository;
import ru.example.auth.repo.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    public long refreshExpirationInMs;

    @Autowired
    public RefreshTokenService(
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            @Value("${jwt.refresh}")
            long refreshExpirationInMs
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.refreshExpirationInMs = refreshExpirationInMs;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken()
                .setUser(userRepository.findById(userId).orElse(null))
                .setExpiryDate(Instant.now().plusMillis(refreshExpirationInMs))
                .setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired");
        }
        return token;
    }

    public TokenInfoDTO getRefreshTokenInfo(TokenInfoDTO tokenInfo) {
        String requestRefreshToken = tokenInfo.getRefreshToken().getToken();

        final RefreshToken currentRefreshToken = findByToken(requestRefreshToken).orElse(null);
         if (currentRefreshToken == null) {
           throw new TokenRefreshException("Refresh-token is missing from the database");
         }

        final User user = verifyExpiration(currentRefreshToken).getUser();
        if (user == null) {
            throw new TokenRefreshException("Refresh-token in database is invalid");
        }

        final String newToken = jwtTokenProvider.createToken(
                user.getUsername(),
                user.getPassword()
        );
        final RefreshToken newRefreshToken = createRefreshToken(user.getId());

        return new TokenInfoDTO()
                .setAccessToken(newToken)
                .setRefreshToken(newRefreshToken);
    }
}
