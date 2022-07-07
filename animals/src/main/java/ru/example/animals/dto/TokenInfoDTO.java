package ru.example.animals.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Data
final public class TokenInfoDTO {
    private String username;
    private AccessTokenDTO accessToken;
    private String tokenType = "Bearer";
}
