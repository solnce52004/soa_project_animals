package ru.example.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class LogoutRequestDTO implements Serializable {
    private static final long serialVersionUID = 979257402689658574L;

    private String accessToken;
    private String refreshToken;
}
