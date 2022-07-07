package ru.example.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.auth.dto.ResponseDTO;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.dto.UserDTO;
import ru.example.auth.service.auth.AuthService;
import ru.example.auth.service.auth.SignInLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = "Auth", value = "AuthController")
@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final SignInLogService signInLogService;

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
            @Valid UserDTO userDTO
    ) {
        // TODO validate ???
        userDTO = UserDTO.clean(userDTO);
        authService.checkIfExistsUsername(userDTO);

        ResponseDTO responseDTO = new ResponseDTO()
                .setUser(userDTO)
                .setHttpStatus(HttpStatus.ACCEPTED);

        return new ResponseEntity<>(responseDTO, HttpStatus.ACCEPTED);
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
    public ResponseEntity<ResponseDTO> registration(
            @Valid UserDTO userDTO
    ) {
        userDTO = UserDTO.clean(userDTO);
        authService.checkIfExistsUsername(userDTO);
        final UserDTO user = authService.registerUser(userDTO);

        ResponseDTO responseDTO = new ResponseDTO()
                .setUser(user)
                .setHttpStatus(HttpStatus.CREATED);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
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
    public ResponseEntity<ResponseDTO> login(
            @RequestBody UserDTO userDTO,
            HttpServletRequest request
    ) {
        final String ip = request.getRemoteAddr();
        signInLogService.checkTriesSignIn(ip);

        userDTO = UserDTO.clean(userDTO);
        final UserDTO user = authService.loginUser(userDTO);
        signInLogService.deleteByIp(ip);

        return ResponseEntity.ok(new ResponseDTO()
                .setUser(user)
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
            @Valid @RequestBody TokenInfoDTO tokenInfo
    ) {
        authService.logout(tokenInfo);

        final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);

        return ResponseEntity.ok(new ResponseDTO()
                .setHttpStatus(HttpStatus.OK));
    }
}
