package ru.example.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import ru.example.auth.entity.AccessToken;
import ru.example.auth.entity.RefreshToken;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Setter @Getter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
final public class UserDTO implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    public static final String ERROR_MSG_EMPTY_VALUE = "Empty value";
    public static final String ERROR_MSG_NOT_VALID = "Invalid value";

    @ApiModelProperty(notes = "Username", required = true)
    @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
    @Size(min = 2, max = 255, message = ERROR_MSG_NOT_VALID)
    private String username;

    @ApiModelProperty(notes = "Password", required = true)
    @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
    @Size(min = 2, max = 8, message = ERROR_MSG_NOT_VALID)
    private String password;

    private AccessToken accessToken;
    private RefreshToken refreshToken;
    @JsonIgnore
    private Set<String> roles = new HashSet<>();

    //for mapping request
    public UserDTO(
            @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
            @Size(min = 2, max = 255, message = ERROR_MSG_NOT_VALID)
                    String username
    ) {
        this.username = username;
    }

    //for mapping request
    public UserDTO(
            @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
            @Size(min = 2, max = 255, message = ERROR_MSG_NOT_VALID)
                    String username,
            @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
            @Size(min = 2, max = 8, message = ERROR_MSG_NOT_VALID)
                    String password
    ) {
        this.username = username;
        this.password = password;
    }
}
