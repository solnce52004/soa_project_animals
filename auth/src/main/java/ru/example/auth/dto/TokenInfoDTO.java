package ru.example.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.RefreshToken;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
final public class TokenInfoDTO implements Serializable {
    private static final long serialVersionUID = 8315152477573756314L;

    private String username;
    private AccessToken accessToken;
    private RefreshToken refreshToken;
    private String tokenType = "Bearer";
}
