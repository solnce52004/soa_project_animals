package ru.example.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.example.auth.entity.RefreshToken;

@NoArgsConstructor
@Accessors(chain = true)
@Data
final public class TokenInfoDTO {
    private String accessToken;
    private RefreshToken refreshToken;
    private String tokenType = "Bearer";
}
