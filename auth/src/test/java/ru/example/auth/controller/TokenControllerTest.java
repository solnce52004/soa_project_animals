package ru.example.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.auth.config.security.jwt.JwtAccessTokenProvider;
import ru.example.auth.dto.TokenInfoDTO;
import ru.example.auth.service.auth.AccessTokenService;
import ru.example.auth.service.auth.RefreshTokenService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.example.auth.controller.util.Util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TokenControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AccessTokenService accessTokenService;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private JwtAccessTokenProvider jwtAccessTokenProvider;

    @Test
    void verifyAccessToken() throws Exception {
        final TokenInfoDTO dto = new TokenInfoDTO().setUsername(USERNAME);

        Mockito.when(jwtAccessTokenProvider.resolveToken(BEARER_TOKEN))
                .thenReturn(ACCESS_TOKEN);
        Mockito.when(accessTokenService.process(ACCESS_TOKEN))
                .thenReturn(dto);

        mvc.perform(post("/api/v1/verify/token")
                .header(AUTHORIZATION, BEARER_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.OK.name())))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = USERNAME, password = PASSWORD, roles = {"USER"})
    @Test
    void refreshToken() throws Exception {
        final TokenInfoDTO dto = new TokenInfoDTO()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setUsername(USERNAME);

        Mockito.when(refreshTokenService.process(REFRESH_TOKEN))
                .thenReturn(dto);

        mvc.perform(post("/api/v1/refresh-token")
                .header(AUTHORIZATION, BEARER_TOKEN)
                .content(new ObjectMapper().writeValueAsString(refreshTokenRequestDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("$.accessToken.token").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.accessToken.expiresAt").value(EXPIRES_AT.toString()))
                .andExpect(jsonPath("$.refreshToken.token").value(REFRESH_TOKEN))
                .andExpect(jsonPath("$.refreshToken.expiresAt").value(EXPIRES_AT.toString()))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.OK.name())))
                .andExpect(status().isOk());
    }
}