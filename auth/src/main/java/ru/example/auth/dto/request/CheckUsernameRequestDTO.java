package ru.example.auth.dto.request;

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
public class CheckUsernameRequestDTO implements Serializable {
    private static final long serialVersionUID = -5237340023310526014L;

    public static final String ERROR_MSG_EMPTY_VALUE = "Empty value";
    public static final String ERROR_MSG_NOT_VALID = "Invalid value";

    @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
    @Size(min = 2, max = 255, message = ERROR_MSG_NOT_VALID)
    private String username;


    public static CheckUsernameRequestDTO clean(CheckUsernameRequestDTO requestDTO) {
        return requestDTO.setUsername(requestDTO.getUsername().trim());
    }
}
