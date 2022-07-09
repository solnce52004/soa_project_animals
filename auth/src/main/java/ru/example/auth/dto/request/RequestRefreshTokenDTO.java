package ru.example.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class RequestRefreshTokenDTO implements Serializable {
    private static final long serialVersionUID = -3232348577199353604L;

    private String refreshToken;
}
