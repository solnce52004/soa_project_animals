package ru.example.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.auth.config.security.jwt.JwtAccessTokenProvider;
import ru.example.auth.dto.UserDTO;
import ru.example.auth.dto.request.AuthRequestDTO;
import ru.example.auth.dto.request.CheckUsernameRequestDTO;
import ru.example.auth.dto.request.RefreshTokenRequestDTO;
import ru.example.auth.dto.response.ResponseDTO;
import ru.example.auth.dto.response.TokenResponseDTO;
import ru.example.auth.service.auth.AuthService;
import ru.example.auth.service.auth.SignInLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = "Auth", value = "AuthController")
@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService authService;
    private final SignInLogService signInLogService;
    private final JwtAccessTokenProvider jwtAccessTokenProvider;

    public AuthController(
            AuthService authService,
            SignInLogService signInLogService,
            JwtAccessTokenProvider jwtAccessTokenProvider
    ) {
        this.authService = authService;
        this.signInLogService = signInLogService;
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
    }

    @Operation(method = "POST",
            description = "Validate Username",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Username is valid",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Username already registered",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PostMapping("/validate/username")
    public ResponseEntity<ResponseDTO> validateUsername(
            @Valid @RequestBody CheckUsernameRequestDTO requestDTO
    ) {
        requestDTO = CheckUsernameRequestDTO.clean(requestDTO);
        authService.checkIfExistsUsername(requestDTO.getUsername());

        return new ResponseEntity<>(new ResponseDTO()
                .setUsername(requestDTO.getUsername())
                .setHttpStatus(HttpStatus.ACCEPTED),
                HttpStatus.ACCEPTED);
    }

    @Operation(method = "POST",
            description = "Registration",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Registration was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Username already registered",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PostMapping("/registration")
    public ResponseEntity<TokenResponseDTO> registration(
            @Valid @RequestBody AuthRequestDTO authRequestDTO
    ) {
        authRequestDTO = AuthRequestDTO.clean(authRequestDTO);
        authService.checkIfExistsUsername(authRequestDTO.getUsername());
        final UserDTO user = authService.registerUser(authRequestDTO);

        return new ResponseEntity<>(new TokenResponseDTO()
                .setAccessToken(user.getAccessToken())
                .setRefreshToken(user.getRefreshToken())
                .setUsername(user.getUsername())
                .setHttpStatus(HttpStatus.CREATED),
                HttpStatus.CREATED);
    }

    @Operation(method = "POST",
            description = "Sign In",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sign In was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Too many signIn attempts",  // 10/h !!!
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(
            @Valid @RequestBody AuthRequestDTO authRequestDTO,
            HttpServletRequest request
    ) {
        final String ip = request.getRemoteAddr();
        signInLogService.checkTriesSignIn(ip);

        authRequestDTO = AuthRequestDTO.clean(authRequestDTO);
        final UserDTO user = authService.loginUser(authRequestDTO);
        signInLogService.deleteByIp(ip);

        return ResponseEntity.ok(new TokenResponseDTO()
                .setAccessToken(user.getAccessToken())
                .setRefreshToken(user.getRefreshToken())
                .setUsername(user.getUsername())
                .setHttpStatus(HttpStatus.OK));
    }

    @Operation(method = "POST",
            description = "Logout",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Logout was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO
    ) {
        final String token = request.getHeader("Authorization");
        authService.logout(
                jwtAccessTokenProvider.resolveToken(token),
                refreshTokenRequestDTO.getRefreshToken()
        );

        final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);

        return ResponseEntity.ok(new ResponseDTO()
                .setHttpStatus(HttpStatus.OK));
    }
}
