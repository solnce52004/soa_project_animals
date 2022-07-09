package ru.example.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.RefreshToken;
import ru.example.auth.exception.custom_exception.BaseError;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
final public class TokenResponseDTO implements Serializable {
    private static final long serialVersionUID = -7615327438712912288L;

    private AccessToken accessToken;
    private RefreshToken refreshToken;
    private String tokenType = "Bearer";

    private BaseError error;
    private String username;
    private HttpStatus httpStatus;
}
