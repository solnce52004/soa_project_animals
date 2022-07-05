package ru.example.auth.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.example.auth.dto.AuthRequestDTO;
import ru.example.auth.dto.ResponseDTO;
import ru.example.auth.dto.UserDTO;
import ru.example.auth.service.auth.AuthService;
import ru.example.auth.service.auth.SignInLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final SignInLogService signInLogService;

    @PostMapping("/registration")
    public ResponseEntity<ResponseDTO> registration(
            @Valid @ModelAttribute("user") UserDTO userDTO
    ) {
        //TODO: validate username
        ResponseDTO responseDTO = new ResponseDTO()
                .setUser(authService.registerAnonymous(userDTO))
                .setHttpStatus(HttpStatus.CREATED);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 10/h !!!
    @PostMapping( "/login")
    public ResponseEntity<ResponseDTO> login(
            @RequestBody AuthRequestDTO requestDTO,
            HttpServletRequest request
    ) {
        final String ip = request.getRemoteAddr();
        signInLogService.checkTriesSignIn(ip);
        final UserDTO user = authService.loginUser(requestDTO);
        signInLogService.deleteByIp(ip);

        return ResponseEntity.ok(new ResponseDTO()
                .setUser(user)
                .setHttpStatus(HttpStatus.CREATED));
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);
    }
}
