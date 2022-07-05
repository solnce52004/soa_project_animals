package ru.example.auth.service.auth;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.auth.config.security.jwt.JwtTokenProvider;
import ru.example.auth.entity.User;
import ru.example.auth.exception.custom_exception.UserNotFoundException;
import ru.example.auth.exception.custom_exception.TokenInvalidException;
import ru.example.auth.service.by_entities.UserService;

@Service
@AllArgsConstructor
public class VerifyTokenService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public User verifyUserAccessToken(String token) {
        verifyJwtToken(token);

        final String username = jwtTokenProvider.getUsername(token);
        final User user = userService.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (user.getToken() != null && !user.getToken().equals(token)) {
            throw new TokenInvalidException();
        }

        return user;
    }

    private void verifyJwtToken(String token) {
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new TokenInvalidException();
        }
    }
}

