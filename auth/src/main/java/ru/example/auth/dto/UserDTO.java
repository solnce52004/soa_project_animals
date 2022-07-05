package ru.example.auth.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import ru.example.auth.entity.RefreshToken;
import ru.example.auth.enums.UserStatusEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Setter @Getter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
final public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ERROR_MSG_EMPTY_VALUE = "Поле не заполнено";
    public static final String ERROR_MSG_NOT_VALID = "Указанное значение не валидно}";

    @ApiModelProperty(notes = "Имя пользователя")
    private String username;

    @ApiModelProperty(notes = "Пароль пользователя", required = true)
    @NotBlank(message = ERROR_MSG_EMPTY_VALUE)
    @Size(min = 2, max = 8, message = ERROR_MSG_NOT_VALID)
    private String password;
    private String status = UserStatusEnum.NOT_CONFIRMED.name();
    private String token;
    private Instant expiryDate;
    private RefreshToken refreshToken;
    private Set<String> roles = new HashSet<>();
}
