package ru.example.animals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessTokenDTO {
    private String accessTokenData;
}
