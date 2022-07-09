package ru.example.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class AuthRequestDTO implements Serializable {
    private static final long serialVersionUID = -8389039602490794576L;

    public static final String ERROR_MSG_EMPTY_VALUE = "Empty value";
    public static final String ERROR_MSG_NOT_VALID = "Invalid value";

    @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
    @Size(min = 2, max = 255, message = ERROR_MSG_NOT_VALID)
    private String username;
    @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
    @Size(min = 2, max = 255, message = ERROR_MSG_NOT_VALID)
    private String password;

    public static AuthRequestDTO clean(AuthRequestDTO authRequestDTO) {
        return authRequestDTO
                .setUsername(authRequestDTO.getUsername().trim())
                .setPassword(authRequestDTO.getPassword().trim());
    }
}
