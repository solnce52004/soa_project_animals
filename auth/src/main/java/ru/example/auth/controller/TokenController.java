package ru.example.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.dto.request.RequestTokenDTO;
import ru.example.auth.dto.response.ResponseDTO;
import ru.example.auth.dto.response.ResponseTokenDTO;
import ru.example.auth.service.auth.AccessTokenService;
import ru.example.auth.service.auth.RefreshTokenService;

import javax.validation.Valid;

@Api(tags = "Token info", value = "TokenController")
@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class TokenController {
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Operation(method = "POST",
            description = "Verify AccessToken",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "AccessToken is valid",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Token is invalid",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PostMapping("/verify/token")
    public ResponseEntity<ResponseDTO> verifyAccessToken(
            @RequestBody RequestTokenDTO requestTokenDTO
    ) {
        TokenInfoDTO tokenInfoDTO = accessTokenService.process(
                    requestTokenDTO.getTokenData());

        return ResponseEntity.ok(new ResponseDTO()
                .setUsername(tokenInfoDTO.getUsername())
                .setHttpStatus(HttpStatus.OK));
    }

    @Operation(method = "POST",
            description = "Refresh Token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Token Refresh was successful",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))}),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token is invalid",
                            content = {@Content(schema = @Schema(
                                    implementation = ResponseDTO.class))})})
    @PostMapping("/refresh-token")
    @Secured({"ROLE_USER"})
    public ResponseEntity<ResponseTokenDTO> refreshToken(
            @Valid @RequestBody RequestTokenDTO requestTokenDTO
    ) {
        final TokenInfoDTO refreshed = refreshTokenService.process(
                requestTokenDTO.getTokenData());

        return ResponseEntity.ok(new ResponseTokenDTO()
                .setAccessToken(refreshed.getAccessToken())
                .setRefreshToken(refreshed.getRefreshToken())
                .setUsername(refreshed.getUsername())
                .setHttpStatus(HttpStatus.OK));
    }
}
