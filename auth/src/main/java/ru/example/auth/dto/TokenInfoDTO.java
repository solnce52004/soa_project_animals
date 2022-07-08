package ru.example.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.RefreshToken;

import javax.persistence.Transient;
import java.io.Serializable;

@NoArgsConstructor
@Accessors(chain = true)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
final public class TokenInfoDTO implements Serializable {
    @Transient
    private static final long serialVersionUID = 14567L;

    private String username;
    private AccessToken accessToken;
    private RefreshToken refreshToken;
    private String tokenType = "Bearer";
}
