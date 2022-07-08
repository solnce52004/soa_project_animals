package ru.example.auth.service.auth;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.example.auth.dto.UserDTO;
import ru.example.auth.dto.request.AuthRequestDTO;
import ru.example.auth.dto.request.LogoutRequestDTO;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.RefreshToken;
import ru.example.auth.entity.User;
import ru.example.auth.exception.custom_exception.RegistrationException;
import ru.example.auth.exception.custom_exception.UserNotFoundException;
import ru.example.auth.service.by_entities.UserService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;

    public void checkIfExistsUsername(String username) {
        User userExist = userService
                .findByUsername(username)
                .orElse(null);

        if (userExist != null) {
            throw new RegistrationException("Username already registered");
        }
    }

    public UserDTO registerUser(AuthRequestDTO authRequestDTO) {
        userService.createUser(new User()
                .setUsername(authRequestDTO.getUsername())
                .setPassword(authRequestDTO.getPassword()));

        return loginUser(authRequestDTO);
    }

    public UserDTO loginUser(AuthRequestDTO authRequestDTO) {
        final UsernamePasswordAuthenticationToken requestToken = new UsernamePasswordAuthenticationToken(
                authRequestDTO.getUsername(),
                authRequestDTO.getPassword()
        );
        final Authentication authentication = authenticationManager.authenticate(requestToken);

        final Set<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        final User user = userService.findByUsername(authRequestDTO.getUsername())
                .orElseThrow(UserNotFoundException::new);

        final AccessToken accessToken = accessTokenService.createToken(user);
        final RefreshToken refreshToken = refreshTokenService.createToken(user);

        autoLogin(requestToken, user.getUsername());

        return new UserDTO()
                .setUsername(user.getUsername())
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setRoles(authorities);
    }

    public void autoLogin(UsernamePasswordAuthenticationToken token, String username) {
        if (token.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(token);

            log.debug(String.format("Auto login %s successfully!", username));
        }
    }

    public void logout(LogoutRequestDTO logoutRequestDTO) {
        accessTokenService.deleteToken(logoutRequestDTO.getAccessToken());
        refreshTokenService.deleteToken(logoutRequestDTO.getRefreshToken());
    }
}
