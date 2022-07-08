package ru.example.animals.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.example.animals.dto.request.RequestVerifyTokenDTO;

@NoArgsConstructor
@Accessors(chain = true)
@Data
final public class TokenInfoDTO {
    private String username;
    private RequestVerifyTokenDTO accessToken;
    private String tokenType = "Bearer";
}
