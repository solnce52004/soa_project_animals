package ru.example.auth.service.auth;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.example.auth.config.security.jwt.JwtAccessTokenProvider;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.dto.UserDTO;
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
    private final JwtAccessTokenProvider jwtAccessTokenProvider;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;

//    public boolean isAuthenticated() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
//            return false;
//        }
//        return authentication.isAuthenticated();
//    }

    public void checkIfExistsUsername(UserDTO userDTO) {
        User userExist = userService
                .findByUsername(userDTO.getUsername())
                .orElse(null);

        if (userExist != null) {
            throw new RegistrationException("Username already registered");
        }
    }

    public UserDTO registerUser(UserDTO userDTO) {
        userService.createUser(new User()
                .setUsername(userDTO.getUsername())
                .setPassword(userDTO.getPassword()));

        return loginUser(userDTO);
    }

    public UserDTO loginUser(UserDTO userDTO) {
        final UsernamePasswordAuthenticationToken requestToken = new UsernamePasswordAuthenticationToken(
                userDTO.getUsername(),
                userDTO.getPassword()
        );
        final Authentication authentication = authenticationManager.authenticate(requestToken);

        final Set<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        final User user = userService.findByUsername(userDTO.getUsername())
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

    public void logout(TokenInfoDTO tokenInfo) {
        accessTokenService.deleteToken(tokenInfo.getAccessToken());
        refreshTokenService.deleteToken(tokenInfo.getRefreshToken());
    }
}
