package ru.example.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.auth.config.security.jwt.JwtAccessTokenProvider;
import ru.example.auth.dto.request.CheckUsernameRequestDTO;
import ru.example.auth.service.auth.AuthService;
import ru.example.auth.service.auth.SignInLogService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.example.auth.controller.util.Util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    AuthService authService;
    @MockBean
    SignInLogService signInLogService;
    @MockBean
    JwtAccessTokenProvider jwtAccessTokenProvider;

    @Test
    @DisplayName("Should validate Username")
    void validateUsername() throws Exception {
        final CheckUsernameRequestDTO dto = new CheckUsernameRequestDTO()
                .setUsername(USERNAME);
        Mockito.doNothing().when(authService).checkIfExistsUsername(USERNAME);

        mvc.perform(post("/api/v1/validate/username")
                .content(new ObjectMapper().writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.ACCEPTED.name())))
                .andExpect(status().isAccepted());
    }

    @Test
    void registration() throws Exception {
        Mockito.doNothing().when(authService).checkIfExistsUsername(USERNAME);
        Mockito.when(authService.registerUser(authRequestDTO))
                .thenReturn(user);

        mvc.perform(post("/api/v1/registration")
                .content(new ObjectMapper().writeValueAsString(authRequestDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("$.accessToken.token").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.accessToken.expiresAt").value(EXPIRES_AT.toString()))
                .andExpect(jsonPath("$.refreshToken.token").value(REFRESH_TOKEN))
                .andExpect(jsonPath("$.refreshToken.expiresAt").value(EXPIRES_AT.toString()))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.CREATED.name())))
                .andExpect(status().isCreated());
    }

    @Test
    void login() throws Exception {
        Mockito.doNothing().when(signInLogService).checkTriesSignIn(IP);
        Mockito.when(authService.loginUser(authRequestDTO))
                .thenReturn(user);
        Mockito.doNothing().when(signInLogService).deleteByIp(IP);

        mvc.perform(post("/api/v1/login")
                .content(new ObjectMapper().writeValueAsString(authRequestDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("$.accessToken.token").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.accessToken.expiresAt").value(EXPIRES_AT.toString()))
                .andExpect(jsonPath("$.refreshToken.token").value(REFRESH_TOKEN))
                .andExpect(jsonPath("$.refreshToken.expiresAt").value(EXPIRES_AT.toString()))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.OK.name())))
                .andExpect(status().isOk());
    }

    @Test
    void logout() throws Exception {
        Mockito.when(jwtAccessTokenProvider.resolveToken(BEARER_TOKEN))
                .thenReturn(ACCESS_TOKEN);
        Mockito.doNothing().when(authService).logout(ACCESS_TOKEN, REFRESH_TOKEN);

        mvc.perform(post("/api/v1/logout")
                .header(AUTHORIZATION, BEARER_TOKEN)
                .content(new ObjectMapper().writeValueAsString(refreshTokenRequestDTO))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(jsonPath("$.httpStatus", CoreMatchers.is(HttpStatus.OK.name())))
                .andExpect(status().isOk());
    }
}